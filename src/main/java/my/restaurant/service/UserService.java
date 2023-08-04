package my.restaurant.service;

import my.restaurant.dto.UserDTO;
import my.restaurant.modal.forms.RegistrationForm;
import my.restaurant.modal.forms.UserUpdateForm;

public interface UserService {
    void save(RegistrationForm registrationForm);
    boolean emailExists(String emailId);
    UserDTO me();
    void update(UserUpdateForm userUpdateForm);
}
