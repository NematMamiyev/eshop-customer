package az.edu.eshopcustomer.service.impl;

import az.edu.eshopcustomer.dto.request.ReqCustomer;
import az.edu.eshopcustomer.dto.response.RespCustomer;
import az.edu.eshopcustomer.dto.response.RespStatus;
import az.edu.eshopcustomer.dto.response.Response;
import az.edu.eshopcustomer.entity.Cart;
import az.edu.eshopcustomer.entity.Customer;
import az.edu.eshopcustomer.entity.Wishlist;
import az.edu.eshopcustomer.enums.EnumAvailableStatus;
import az.edu.eshopcustomer.exception.EshopException;
import az.edu.eshopcustomer.exception.ExceptionConstants;
import az.edu.eshopcustomer.mapper.CustomerMapper;
import az.edu.eshopcustomer.repository.CartRepository;
import az.edu.eshopcustomer.repository.CustomerRepository;
import az.edu.eshopcustomer.repository.OrderRepository;
import az.edu.eshopcustomer.repository.WishlistRepository;
import az.edu.eshopcustomer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Response<RespCustomer> register(ReqCustomer reqCustomer) {
        Response<RespCustomer> response = new Response<>();
        boolean uniqueEmail = customerRepository.existsCustomerByEmailIgnoreCaseAndActive(reqCustomer.getEmail(), EnumAvailableStatus.ACTIVE.getValue());
        boolean uniquePhone = customerRepository.existsCustomerByPhoneIgnoreCaseAndActive(reqCustomer.getPhone(), EnumAvailableStatus.ACTIVE.getValue());
        if (uniquePhone || uniqueEmail) {
            throw new EshopException(ExceptionConstants.INVALID_REQUEST_DATA, "Email or phone number available in the database");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(reqCustomer.getPassword());
        reqCustomer.setPassword(encodedPassword);
        Customer customer = customerMapper.toCustomer(reqCustomer);
        customerRepository.save(customer);
        Cart cart = Cart.builder()
                .customer(customer)
                .amount(BigDecimal.ZERO)
                .build();
        cartRepository.save(cart);
        Wishlist wishlist = Wishlist.builder()
                .customer(customer)
                .build();
        wishlistRepository.save(wishlist);
        customer.setCart(cart);
        customer.setWishlist(wishlist);
        customerRepository.save(customer);
        response.setT(customerMapper.toRespCustomer(customer));
        response.setStatus(RespStatus.getSuccessMessage());
        return response;
    }

    @Override
    public Response<List<RespCustomer>> getCustomerList() {
        Response<List<RespCustomer>> response = new Response<>();
        List<Customer> customerList = customerRepository.findAllByActive(EnumAvailableStatus.ACTIVE.getValue());
        if (customerList.isEmpty()) {
            throw new EshopException(ExceptionConstants.INVALID_REQUEST_DATA, "Customer list empty");
        }
        response.setT(customerMapper.toRespCustomerList(customerList));
        response.setStatus(RespStatus.getSuccessMessage());
        return response;
    }

    @Override
    public Response<RespCustomer> getCustomerById(Long id) {
        Response<RespCustomer> response = new Response<>();
        Customer customer = getCustomer(id);
        response.setT(customerMapper.toRespCustomer(customer));
        response.setStatus(RespStatus.getSuccessMessage());
        return response;
    }

    @Override
    public Response<RespCustomer> updateCustomer(Long id, ReqCustomer reqCustomer) {
        Response<RespCustomer> response = new Response<>();
        Customer customer = getCustomer(id);
        boolean uniqueEmail = customerRepository.existsCustomerByEmailIgnoreCaseAndActiveAndIdNot(reqCustomer.getEmail(), EnumAvailableStatus.ACTIVE.getValue(), id);
        boolean uniquePhone = customerRepository.existsCustomerByPhoneIgnoreCaseAndActiveAndIdNot(reqCustomer.getPhone(), EnumAvailableStatus.ACTIVE.getValue(), id);
        if (uniquePhone || uniqueEmail) {
            throw new EshopException(ExceptionConstants.INVALID_REQUEST_DATA, "Email or phone number available in the database");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(reqCustomer.getPassword());
        reqCustomer.setPassword(encodedPassword);
        customerMapper.updateCustomerFromReqCustomer(customer, reqCustomer);
        customerRepository.save(customer);
        response.setT(customerMapper.toRespCustomer(customer));
        response.setStatus(RespStatus.getSuccessMessage());
        return response;
    }

    @Override
    public RespStatus deleteCustomer(Long id) {
        Customer customer = getCustomer(id);
        customer.setActive(EnumAvailableStatus.DEACTIVATED.getValue());
        customerRepository.save(customer);
        return RespStatus.getSuccessMessage();
    }

    private Customer getCustomer(Long id) {
        Customer customer = customerRepository.findCustomerByIdAndActive(id, EnumAvailableStatus.ACTIVE.getValue());
        if (customer == null) {
            throw new EshopException(ExceptionConstants.CUSTOMER_NOT_FOUND, "Customer not found");
        }
        return customer;
    }
}
