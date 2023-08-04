package my.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.modal.Theme;

@Entity
@Table(name = "user_preference")
@Getter
@Setter
public class UserPreference extends BaseEntity {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private Theme theme;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
