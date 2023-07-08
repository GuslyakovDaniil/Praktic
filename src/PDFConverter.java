import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;

public class PDFConverter {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            try {
                byte[] pdfBytes = convertToBytes(filePath);
                System.out.println("Файл успешно преобразован в байты.");
                saveAsPDF(pdfBytes, "новый_файл.pdf");
                System.out.println("Байты успешно преобразованы обратно в PDF файл.");
                openPDFFile("новый_файл.pdf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] convertToBytes(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }

    private static void saveAsPDF(byte[] pdfBytes, String fileName) throws IOException {
        Path path = Path.of(fileName);
        Files.write(path, pdfBytes);
    }

    private static void openPDFFile(String filePath) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new java.io.File(filePath));
        }
    }
}
