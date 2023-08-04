package my.restaurant.exceptions;

public class ProductNotFountException extends RuntimeException {

    public ProductNotFountException(String message) {
        super(message);
    }
}
