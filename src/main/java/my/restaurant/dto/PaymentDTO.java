package my.restaurant.dto;

public record PaymentDTO(long id, String creditCard, String cvv, String fullName, int expiryMonth, int expiryYear,
                         boolean myDefault) {
    public PaymentDTO(long id) {
        this(id, null, null, null, 0, 0, false);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", creditCard, expiryMonth + "/" + expiryYear);
    }
}
