package utils;


import Controllers.MainPageController;

import java.io.File;

public class PdfConverter {


    public static void convertExcelToPdf() {
        try {
            String libreOfficePath = "\"C:\\Program Files\\LibreOffice\\program\\soffice.exe\"";
            String command = String.format("%s --headless --convert-to pdf \"%s\" --outdir \"%s\"",
                    libreOfficePath, ExcelPlaceholderReplacer.outputFilePath, getFolderPath(ExcelPlaceholderReplacer.outputFilePath));

            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            deleteExcelFile(ExcelPlaceholderReplacer.outputFilePath);
        } catch (Exception e) {
            System.out.println("There was a problem in converting the excel file");
        }
    }

    public static String getFolderPath(String fullFilePath) {
        if (fullFilePath == null || fullFilePath.isEmpty()) {
            return "";
        }
        File file = new File(fullFilePath);
        String parentPath = file.getParent();
        return parentPath != null ? parentPath : "";
    }

    public static void deleteExcelFile(String filePath) throws Exception{
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                MainPageController.invoiceController.clearValues();
            } else {
                System.out.println("Failed to delete the file: " + filePath);
            }
        } else {
            System.out.println("File does not exist: " + filePath);
        }
    }
}
