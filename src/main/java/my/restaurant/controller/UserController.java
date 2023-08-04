package my.restaurant.controller;

import jakarta.validation.Valid;
import my.restaurant.dto.UserDTO;
import my.restaurant.modal.forms.RegistrationForm;
import my.restaurant.modal.forms.UserUpdateForm;
import my.restaurant.service.UserService;
import my.restaurant.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String login(Authentication authentication, Model model) {
        model.addAttribute(Constants.PageTitle, "Login");
        model.addAttribute(Constants.LoginPage, true);
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        return "user/login";
    }

    @GetMapping("registration")
    public String registration(Model model) {
        model.addAttribute(Constants.PageTitle, "Registration");
        model.addAttribute("registrationForm", new RegistrationForm());
        model.addAttribute(Constants.LoginPage, true);
        return "user/registration";
    }

    @PostMapping("registration")
    public String registration(@Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               Model model) {
        model.addAttribute(Constants.LoginPage, true);
        if (userService.emailExists(registrationForm.getEmailId())) {
            bindingResult.rejectValue("emailId", "", "account exists for email");
        }

        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        this.userService.save(registrationForm);

        redirectAttributes.addFlashAttribute("message", "You have successfully registered.");

        return "redirect:/login";
    }

    @GetMapping("user/profile")
    @PreAuthorize("hasAuthority('USER')")
    public String profile(Model model) {
        UserDTO userDTO = this.userService.me();
        model.addAttribute("emailId", userDTO.emailId());
        model.addAttribute("userForm", new UserUpdateForm(userDTO.firstName(), userDTO.lastName()));
        model.addAttribute(Constants.PageTitle, "User Details ");
        return "user/profile";
    }

    @PostMapping("user/profile")
    @PreAuthorize("hasAuthority('USER')")
    public String updateProfile(@Valid @ModelAttribute("userForm") UserUpdateForm userUpdateForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/profile";
        }
        redirectAttributes.addFlashAttribute("message", "Profile updated");
        this.userService.update(userUpdateForm);

        return "redirect:/user/profile";
    }
}
