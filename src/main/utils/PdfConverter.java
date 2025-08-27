package utils;

import com.microsoft.playwright.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfConverter {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            Path path = Paths.get("C:/Users/aryan/OneDrive/Desktop/Testing/finalTry.pdf");

            // Load your local HTML file
            page.navigate("file:///D:\\InvoiceGenerator\\InvoiceGenerator\\src\\Resources\\Templates\\Invoice.html");

            // Export directly to PDF
            Page.PdfOptions options = new Page.PdfOptions()
                    .setPath(path)
                    .setFormat("A4")
                    .setPrintBackground(true)
                    .setPreferCSSPageSize(true);

            page.pdf(options);

            browser.close();
        }
    }
}
