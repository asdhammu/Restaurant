package my.restaurant.security;

import my.restaurant.dto.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashSet;
import java.util.Set;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (withMockCustomUser.authorities() == null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        } else {
            for (String s : withMockCustomUser.authorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s));
            }
        }
        MyUserDetails principal =
                new MyUserDetails(withMockCustomUser.username(), withMockCustomUser.username(), grantedAuthorities);
        principal.setImgUrl("img_url");
        principal.setFirstName("first");
        principal.setLastName("last");
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
