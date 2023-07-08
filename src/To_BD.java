import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class To_BD {
    public static void main(String[] args) {
        String titleID = JOptionPane.showInputDialog(null, "Введите title_ID:");

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            try {
                byte[] pdfBytes = convertToBytes(filePath);
                saveToDatabase(titleID, pdfBytes);
                System.out.println("Байты успешно сохранены в базе данных.");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] convertToBytes(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }

    private static void saveToDatabase(String titleID, byte[] pdfBytes) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/online library";
        String username = "postgres";
        String password = "mysql";

        String sql = "INSERT INTO book (title_ID, bytea) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, titleID);
            statement.setBytes(2, pdfBytes);
            statement.executeUpdate();
        }
    }
}
