package my.restaurant.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class CartItemKey implements Serializable {

    @JoinColumn(name = "cart_id", nullable = false)
    @ManyToOne
    private Cart cart;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    public CartItemKey(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public CartItemKey() {

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
