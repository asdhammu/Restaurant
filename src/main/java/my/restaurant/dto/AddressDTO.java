package my.restaurant.dto;

public record AddressDTO(String firstName, String lastName, String emailId, long id, String street1, String street2,
                         String country,
                         String state, String city, String postalCode, boolean myDefault, String type,
                         String phoneNumber) {
    public AddressDTO(long id) {
        this(null, null, null, id, null, null, null, null, null, null, false, null, null);
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s, %s, %s, %s, %s, %s",firstName,lastName, street1, street2, city, state, postalCode, country);
    }
}
