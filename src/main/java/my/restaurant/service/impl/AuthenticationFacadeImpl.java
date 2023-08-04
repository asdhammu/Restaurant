package my.restaurant.service.impl;

import my.restaurant.dto.MyUserDetails;
import my.restaurant.service.AuthenticationFacade;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            return myUserDetails.getUsername();
        }
        return null;
    }

    @Override
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
}
