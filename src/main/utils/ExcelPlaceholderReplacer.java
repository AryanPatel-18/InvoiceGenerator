package utils;

import Controllers.MainPageController;
import models.Company;
import models.Invoice;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ExcelPlaceholderReplacer {

    public static String inputFilePath = "src/Resources/Templates/default_invoice.xlsx";
    public static String outputFilePath;

    public static void replace(Invoice obj, Company comp) throws Exception {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);

        double amount1 = obj.price1*obj.quantity1;
        double amount2 = obj.price2*obj.quantity2;
        double amount3 = obj.price3*obj.quantity3;
        double amount4 = obj.price4*obj.quantity4;
        double amount5 = obj.price5*obj.quantity5;

        double totalPrice = amount1 + amount2 + amount3 + amount4 + amount5;

        // Define placeholder replacements
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{Contact Name}}", safe(comp.getClient()));
        replacements.put("{{Company Name}}", safe(comp.getCompanyName()));
        replacements.put("{{Address}}", safe(comp.getCity()) + "," + safe(comp.getState()));
        replacements.put("{{Phone}}", safe(comp.getPhoneNumber()));
        replacements.put("{{Email}}", safe(comp.getEmail()));
        replacements.put("{{Project Name}}", safe(obj.projectDetails));
        replacements.put("{{DATE}}", obj.formatedDate.trim().isEmpty()?formattedDate:obj.formatedDate);
        replacements.put("{{Invoice_number}}", MainPageController.invoiceId);

        // Descriptions
        replacements.put("{{description1}}", safe(obj.description1));
        replacements.put("{{description2}}", safe(obj.description2));
        replacements.put("{{description3}}", safe(obj.description3));
        replacements.put("{{description4}}", safe(obj.description4));
        replacements.put("{{description5}}", safe(obj.description5));

        // Prices: replace 0 with empty string
        replacements.put("{{price1}}", obj.price1 != 0 ? String.valueOf(obj.price1) : "");
        replacements.put("{{price2}}", obj.price2 != 0 ? String.valueOf(obj.price2) : "");
        replacements.put("{{price3}}", obj.price3 != 0 ? String.valueOf(obj.price3) : "");
        replacements.put("{{price4}}", obj.price4 != 0 ? String.valueOf(obj.price4) : "");
        replacements.put("{{price5}}", obj.price5 != 0 ? String.valueOf(obj.price5) : "");

        // Quantities: replace 0 with empty string
        replacements.put("{{quantity1}}", obj.quantity1 != 0 ? String.valueOf(obj.quantity1) : "");
        replacements.put("{{quantity2}}", obj.quantity2 != 0 ? String.valueOf(obj.quantity2) : "");
        replacements.put("{{quantity3}}", obj.quantity3 != 0 ? String.valueOf(obj.quantity3) : "");
        replacements.put("{{quantity4}}", obj.quantity4 != 0 ? String.valueOf(obj.quantity4) : "");
        replacements.put("{{quantity5}}", obj.quantity5 != 0 ? String.valueOf(obj.quantity5) : "");

        replacements.put("{{amount1}}", amount1 != 0? String.valueOf(amount1):"");
        replacements.put("{{amount2}}", amount2 != 0? String.valueOf(amount2):"");
        replacements.put("{{amount3}}", amount3 != 0? String.valueOf(amount3):"");
        replacements.put("{{amount4}}", amount4 != 0? String.valueOf(amount4):"");
        replacements.put("{{amount5}}", amount5 != 0? String.valueOf(amount5):"");

        // Total price
        String rupeeSymbol = "₹";
        replacements.put("{{priceDue}}", totalPrice != 0 ? rupeeSymbol + " " + totalPrice : "");

        // ✅ Open and modify Excel using Apache POI
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.STRING) {
                            String cellValue = cell.getStringCellValue();
                            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                                if (cellValue.contains(entry.getKey())) {
                                    cellValue = cellValue.replace(entry.getKey(), entry.getValue());
                                }
                            }
                            cell.setCellValue(cellValue);
                        }
                    }
                }
            }

            // Write updated file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }
        }
        // Optionally call PDF converter
        PdfConverter.convertExcelToPdf();
    }

    private static String safe(String val) {
        return val != null ? val : "";
    }

    public static String generateInvoiceNumber() {
        Random random = new Random();
        char firstChar = (char) ('A' + random.nextInt(26));
        char secondChar = (char) ('A' + random.nextInt(26));
        int numberPart = 1000 + random.nextInt(9000);
        return "" + firstChar + secondChar + numberPart;
    }

}
