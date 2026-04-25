package billing;

import domain.user.User;
import money.Money;

import java.time.LocalDateTime;
import java.util.Objects;

public class Invoice {

    private final String invoiceNumber;
    private final LocalDateTime issueDate;
    private final User buyer;
    private final Money total;
    private final String itemDescription;

    public Invoice(String invoiceNumber,
                   LocalDateTime issueDate,
                   User buyer,
                   Money total,
                   String itemDescription) {

        if (invoiceNumber == null || invoiceNumber.isBlank())
            throw new IllegalArgumentException("Invoice number cannot be empty");

        if (issueDate == null)
            throw new IllegalArgumentException("Issue date cannot be null");

        if (buyer == null)
            throw new IllegalArgumentException("Buyer cannot be null");

        if (total == null)
            throw new IllegalArgumentException("Total cannot be null");

        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.buyer = buyer;
        this.total = total;
        this.itemDescription = itemDescription;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getTotal() {
        return total;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "number='" + invoiceNumber + '\'' +
                ", date=" + issueDate +
                ", buyer=" + buyer.getDisplayName() +
                ", total=" + total +
                ", item='" + itemDescription + '\'' +
                '}';
    }
}