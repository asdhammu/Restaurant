package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.UserDTO;
import my.restaurant.dto.UserRole;
import my.restaurant.entity.Role;
import my.restaurant.entity.User;
import my.restaurant.modal.EmailType;
import my.restaurant.modal.forms.RegistrationForm;
import my.restaurant.modal.forms.UserUpdateForm;
import my.restaurant.repository.UserRepository;
import my.restaurant.repository.UserRoleRepository;
import my.restaurant.service.AuthenticationFacade;
import my.restaurant.service.EmailService;
import my.restaurant.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final AuthenticationFacade authenticationFacade;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, AuthenticationFacade authenticationFacade) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public void save(RegistrationForm registrationForm) {
        Optional<Role> optionalRole = userRoleRepository.findRoleByName(UserRole.USER);
        User user = new User();
        user.setFirstName(registrationForm.getFirstName());
        user.setLastName(registrationForm.getLastName());
        user.setPassword(bCryptPasswordEncoder.encode(registrationForm.getPassword()));
        user.setEmailId(registrationForm.getEmailId());
        user.setUsername(registrationForm.getEmailId());
        // set default USER role
        optionalRole.ifPresent(role -> user.setRoles(new HashSet<>() {{
            add(role);
        }}));
        userRepository.save(user);
        this.emailService.sendEmail(EmailType.WELCOME, registrationForm.getEmailId());
    }

    @Override
    public boolean emailExists(String emailId) {
        return userRepository.findByEmailId(emailId).isPresent();
    }

    @Override
    public UserDTO me() {
        Optional<User> userOptional = this.userRepository.findByUsername(this.authenticationFacade.getUsername());
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();
        return new UserDTO(user.getFirstName(), user.getLastName(), user.getEmailId());
    }

    @Override
    public void update(UserUpdateForm userUpdateForm) {
        Optional<User> userOptional = this.userRepository.findByUsername(this.authenticationFacade.getUsername());
        if (userOptional.isEmpty()) return;
        User user = userOptional.get();
        user.setFirstName(userUpdateForm.firstName());
        user.setLastName(userUpdateForm.lastName());
        this.userRepository.save(user);
    }
}
