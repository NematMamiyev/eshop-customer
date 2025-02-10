package az.edu.eshopcustomer.repository;

import az.edu.eshopcustomer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface
CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsCustomerByEmailIgnoreCaseAndActiveAndIdNot(String email, Integer active, Long id);

    boolean existsCustomerByPhoneIgnoreCaseAndActiveAndIdNot(String phone, Integer active, Long id);

    boolean existsCustomerByEmailIgnoreCaseAndActive(String email, Integer active);

    boolean existsCustomerByPhoneIgnoreCaseAndActive(String phone, Integer active);

    boolean existsCustomerByIdAndActive(Long id, Integer active);

    List<Customer> findAllByActive(Integer active);

    Customer findCustomerByIdAndActive(Long id, Integer active);

    Customer findByEmailAndActive(String email, Integer active);

}
