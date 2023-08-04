package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.dto.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class Role extends BaseEntity implements GrantedAuthority {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_role_id_seq")
    @SequenceGenerator(name = "user_role_role_id_seq", sequenceName = "user_role_role_id_seq")
    private Integer id;

    @NotNull
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private UserRole name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    @Override
    public String getAuthority() {
        return this.name.toString();
    }
}
