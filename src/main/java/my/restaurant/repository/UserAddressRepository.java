package my.restaurant.repository;

import my.restaurant.entity.User;
import my.restaurant.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> getAddressByUserUsername(String userName);

    UserAddress getAddressByAddressIdAndUserUsername(long id, String username);

    UserAddress getAddressByMyDefaultIsTrueAndUserUsername(String username);
}

