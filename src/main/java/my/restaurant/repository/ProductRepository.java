package my.restaurant.repository;

import my.restaurant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> getProductByProductId(UUID productId);
    List<Product> getProductsByFavoriteTrue();
    @Query(value = "select * from product where to_tsvector('english', name) @@ to_tsquery('english', :query) " +
            "or to_tsvector('english', description) @@ to_tsquery('english', :query)", nativeQuery = true)
    List<Product> searchProduct(String query);
}
