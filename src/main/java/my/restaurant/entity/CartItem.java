package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity {

    @EmbeddedId
    private CartItemKey cartItemKey;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

}
