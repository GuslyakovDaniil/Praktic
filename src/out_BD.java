import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class out_BD {
    // Параметры подключения к БД
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/online library";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "mysql";

    public static void main(String[] args) {
        try {
            // Создание объекта Scanner для чтения ввода из консоли
            Scanner scanner = new Scanner(System.in);

            // Ввод идентификатора (title_ID) из консоли
            System.out.print("Введите title_ID: ");
            String titleID = scanner.nextLine();

            // Установка соединения с БД
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Подготовка SQL-запроса
            String sql = "SELECT bytea FROM book WHERE title_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, titleID);

            // Выполнение запроса
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Получение байтов данных
                byte[] data = resultSet.getBytes("bytea");

                // Создание PDF файла
                String fileName = "output.pdf";
                try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                    outputStream.write(data);
                    System.out.println("PDF файл успешно создан: " + fileName);

                    // Открытие файла в браузере
                    File pdfFile = new File(fileName);
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("Открытие файла в браузере не поддерживается на этой платформе.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Запись с указанным title_ID не найдена.");
            }

            // Закрытие ресурсов
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
