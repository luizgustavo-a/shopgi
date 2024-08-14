package tech.shopgi.customerms.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.shopgi.customerms.dto.CreateCustomerDto;
import tech.shopgi.customerms.dto.PublicCustomerDto;
import tech.shopgi.customerms.dto.UpdateCustomerDto;
import tech.shopgi.customerms.model.exception.CostumerNotFoundException;
import tech.shopgi.customerms.service.CustomerService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<PublicCustomerDto> createCustomer(@RequestBody CreateCustomerDto createCustomerDto,
                                                            UriComponentsBuilder builder) {
        var customer = customerService.create(createCustomerDto);
        var uri = builder.path("/customer/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(new PublicCustomerDto(customer));
    }

    @GetMapping
    public ResponseEntity<List<PublicCustomerDto>> listAllCustomers() {
        var customerList = customerService.listAllCustomers()
                .stream()
                .map(PublicCustomerDto::new)
                .toList();

        return ResponseEntity.ok(customerList);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<PublicCustomerDto> findCustomer(@PathVariable String identifier) throws CostumerNotFoundException {
        try {
            Long id = Long.parseLong(identifier);
            var customer = customerService.findCustomerById(id);
            return ResponseEntity.ok(new PublicCustomerDto(customer));
        } catch (NumberFormatException e) {
            // if the identifier is not the id, it is the document
        }
        var customer = customerService.findCustomerByDocument(identifier);
        return ResponseEntity.ok(new PublicCustomerDto(customer));
    }

    @PutMapping
    public ResponseEntity<PublicCustomerDto> updateCustomer(@RequestBody UpdateCustomerDto updateCustomerDto) throws CostumerNotFoundException {
        var customer = customerService.update(updateCustomerDto);

        return ResponseEntity.ok(new PublicCustomerDto(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PublicCustomerDto> deleteCustomer(@PathVariable Long id) throws CostumerNotFoundException {
        var customer = customerService.delete(id);

        return ResponseEntity.ok(new PublicCustomerDto(customer));
    }
}
