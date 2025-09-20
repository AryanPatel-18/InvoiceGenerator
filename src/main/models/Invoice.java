package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Invoice {

    // Project details
    public String projectDetails = "";

    // Descriptions
    public String description1 = "";
    public String description2 = "";
    public String description3 = "";
    public String description4 = "";
    public String description5 = "";

    // Prices
    public double price1 = 0.0;
    public double price2 = 0.0;
    public double price3 = 0.0;
    public double price4 = 0.0;
    public double price5 = 0.0;

    // Quantities
    public int quantity1 = 0;
    public int quantity2 = 0;
    public int quantity3 = 0;
    public int quantity4 = 0;
    public int quantity5 = 0;
    public String formatedDate = "";


    /**
     * Single constructor to initialize all fields.
     * If you don't want to set a value, just pass null for Strings or 0 for numbers.
     */
    public Invoice(String projectDetails,
                   String description1, String description2, String description3, String description4, String description5,
                   double price1, double price2, double price3, double price4, double price5,
                   int quantity1, int quantity2, int quantity3, int quantity4, int quantity5) {

        this.projectDetails = projectDetails != null ? projectDetails : "";
        this.description1 = description1 != null ? description1 : "";
        this.description2 = description2 != null ? description2 : "";
        this.description3 = description3 != null ? description3 : "";
        this.description4 = description4 != null ? description4 : "";
        this.description5 = description5 != null ? description5 : "";

        this.price1 = price1;
        this.price2 = price2;
        this.price3 = price3;
        this.price4 = price4;
        this.price5 = price5;

        this.quantity1 = quantity1;
        this.quantity2 = quantity2;
        this.quantity3 = quantity3;
        this.quantity4 = quantity4;
        this.quantity5 = quantity5;
    }

    public Invoice(String projectDetails,
                   String description1, String description2, String description3, String description4, String description5,
                   double price1, double price2, double price3, double price4, double price5,
                   int quantity1, int quantity2, int quantity3, int quantity4, int quantity5, String date) {

        this.projectDetails = projectDetails != null ? projectDetails : "";
        this.description1 = description1 != null ? description1 : "";
        this.description2 = description2 != null ? description2 : "";
        this.description3 = description3 != null ? description3 : "";
        this.description4 = description4 != null ? description4 : "";
        this.description5 = description5 != null ? description5 : "";

        this.price1 = price1;
        this.price2 = price2;
        this.price3 = price3;
        this.price4 = price4;
        this.price5 = price5;

        this.quantity1 = quantity1;
        this.quantity2 = quantity2;
        this.quantity3 = quantity3;
        this.quantity4 = quantity4;
        this.quantity5 = quantity5;
        this.formatedDate = date;
    }

    /** Default constructor: everything empty/zero */
    public Invoice() {
        // all values are already initialized with defaults
    }
}
