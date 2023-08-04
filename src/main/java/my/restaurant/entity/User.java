package my.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.dto.UserAuthProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Table(name = "user_info")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @SequenceGenerator(name = "userSeq", sequenceName = "user_info_user_id_seq")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private UserAuthProvider provider;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<UserAddress> addresses;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "is_locked")
    private boolean locked;

    @Column(name = "is_non_expired")
    private boolean nonExpired;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToOne(mappedBy = "user")
    private UserPreference userPreference;

    @OneToMany(mappedBy = "user")
    private List<UserPaymentMethod> paymentMethods = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.nonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
