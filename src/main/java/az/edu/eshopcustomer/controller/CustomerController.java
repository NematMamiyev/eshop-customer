package az.edu.eshopcustomer.controller;


import az.edu.eshopcustomer.dto.request.ReqCustomer;
import az.edu.eshopcustomer.dto.response.RespCustomer;
import az.edu.eshopcustomer.dto.response.RespStatus;
import az.edu.eshopcustomer.dto.response.Response;
import az.edu.eshopcustomer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Response<RespCustomer> register(@RequestBody @Valid ReqCustomer reqCustomer) {
        return customerService.register(reqCustomer);
    }

    @ResponseStatus(HttpStatus.OK)
   // @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')or hasAuthority('OPERATOR')")
    @GetMapping
    public Response<List<RespCustomer>> getCustomerList() {
        return customerService.getCustomerList();
    }

    @ResponseStatus(HttpStatus.OK)
   // @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')or hasAuthority('OPERATOR')")
    @GetMapping("/{id}")
    public Response<RespCustomer> getCustomerById(@PathVariable @NotNull(message = "Id is required") Long id) {
        return customerService.getCustomerById(id);
    }

    @ResponseStatus(HttpStatus.OK)
   // @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')or hasAuthority('CUSTOMER')")
    @PutMapping("/{id}")
    public Response<RespCustomer> updateCustomer(@PathVariable @NotNull(message = "Id is required") Long id, @RequestBody @Valid ReqCustomer reqCustomer) {
        return customerService.updateCustomer(id, reqCustomer);
    }

    @ResponseStatus(HttpStatus.OK)
   // @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public RespStatus deleteCustomer(@PathVariable @NotNull(message = "Id is required") Long id) {
        return customerService.deleteCustomer(id);
    }
}
