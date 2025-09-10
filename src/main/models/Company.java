package models;

public class Company {
    private final String client;
    private final String companyName;
    private final String email;
    private final String phoneNumber;
    private final String state;
    private final String city;

    public Company(String client, String companyName, String email, String phoneNumber, String state, String city) {
        this.client = client;
        this.companyName = companyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.city = city;
    }

    public String getClient() {
        return client;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }
}
