package my.restaurant.exceptions;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException(String message){
        super(message);
    }
}
