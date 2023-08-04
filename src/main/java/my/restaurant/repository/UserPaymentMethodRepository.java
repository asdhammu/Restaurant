package my.restaurant.repository;

import my.restaurant.entity.UserPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPaymentMethodRepository extends JpaRepository<UserPaymentMethod, Long> {
    List<UserPaymentMethod> findByUserUsername(String username);

    UserPaymentMethod findByMyDefaultIsTrueAndUserUsername(String username);

    Optional<UserPaymentMethod> findByPaymentMethodIdAndUserUsername(long paymentId, String username);
}
