package my.restaurant.repository;

import my.restaurant.entity.Cart;
import my.restaurant.entity.CartItem;
import my.restaurant.entity.CartItemKey;
import my.restaurant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {

    Optional<CartItem> findByCartItemKey_CartAndCartItemKey_Product(Cart cart, Product product);
    List<CartItem> findByCartItemKey_Cart(Cart cart);
    @Modifying
    @Query(value = "delete from cart_item where cart_id = :cartId and product_id = :productId", nativeQuery = true)
    void deleteCartItem(@Param("cartId") long cartId, @Param("productId") UUID productId);
}
