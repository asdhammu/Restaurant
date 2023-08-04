package my.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartSeq")
    @SequenceGenerator(name = "cartSeq", sequenceName = "cart_cart_id_seq")
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cartItemKey.cart", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<CartItem> cartItems = new ArrayList<>();

}
