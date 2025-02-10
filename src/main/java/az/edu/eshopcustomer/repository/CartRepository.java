package az.edu.eshopcustomer.repository;


import az.edu.eshopcustomer.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findCartByCustomerIdAndActive(Long customerId, Integer active);
}
