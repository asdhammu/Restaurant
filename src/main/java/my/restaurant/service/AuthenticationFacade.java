package my.restaurant.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
    String getUsername();
    boolean isLoggedIn();
}
