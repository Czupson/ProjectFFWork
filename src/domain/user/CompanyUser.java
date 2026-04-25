package domain.user;

public class CompanyUser extends User {
    private final String companyName;
    private final String taxId;

    public CompanyUser(String email, String companyName, String taxId) {
        super(email, companyName);

        if (taxId == null || taxId.isBlank()) {
            throw new IllegalArgumentException("Tax ID cannot be null or empty");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }

        this.companyName = companyName;
        this.taxId = taxId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    @Override
    public String toString() {
        return companyName + " (NIP: " + taxId + ") <" + getEmail() + ">";
    }
}
