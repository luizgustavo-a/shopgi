package tech.shopgi.customerms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.shopgi.customerms.dto.CreateCustomerDto;
import tech.shopgi.customerms.dto.UpdateCustomerDto;
import tech.shopgi.customerms.model.Address;
import tech.shopgi.customerms.model.Customer;
import tech.shopgi.customerms.model.Gender;
import tech.shopgi.customerms.model.exception.CostumerNotFoundException;
import tech.shopgi.customerms.repository.CustomerRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer create(CreateCustomerDto createCustomerDto) {
        Customer customer = new Customer(
                createCustomerDto.name(),
                createCustomerDto.document(),
                createCustomerDto.dateOfBirth(),
                Gender.fromString(createCustomerDto.gender()),
                new Address(
                        createCustomerDto.address().street(),
                        createCustomerDto.address().number(),
                        createCustomerDto.address().complement(),
                        createCustomerDto.address().city(),
                        createCustomerDto.address().state(),
                        createCustomerDto.address().postalCode()
                ),
                createCustomerDto.email(),
                createCustomerDto.contactNumber()
        );

        customerRepository.save(customer);

        return customer;
    }

    public List<Customer> listAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerById(Long id) throws CostumerNotFoundException {
        return customerRepository.findCustomerById(id)
                .orElseThrow(CostumerNotFoundException::new);
    }

    public Customer findCustomerByDocument(String document) throws CostumerNotFoundException {
        return customerRepository.findCustomerByDocument(document)
                .orElseThrow(CostumerNotFoundException::new);
    }

    public Customer update(UpdateCustomerDto updateCustomerDto) throws CostumerNotFoundException {
        Customer customer = findCustomerById(updateCustomerDto.id());

        if (updateCustomerDto.name()!= null && !updateCustomerDto.name().isEmpty()) {
            customer.setName(updateCustomerDto.name());
        }
        if (updateCustomerDto.document()!= null && !updateCustomerDto.document().isEmpty()) {
            customer.setDocument(updateCustomerDto.document());
        }
        if (updateCustomerDto.dateOfBirth()!= null && !updateCustomerDto.dateOfBirth().isEmpty()) {
            customer.setDateOfBirth(updateCustomerDto.dateOfBirth());
        }
        if (updateCustomerDto.gender()!= null && !updateCustomerDto.gender().isEmpty()) {
            customer.setGender(Gender.fromString(updateCustomerDto.gender()));
        }
        if (updateCustomerDto.email()!= null && !updateCustomerDto.email().isEmpty()) {
            customer.setEmail(updateCustomerDto.email());
        }
        if (updateCustomerDto.contactNumber()!= null && !updateCustomerDto.contactNumber().isEmpty()) {
            customer.setContactNumber(updateCustomerDto.contactNumber());
        }
        if (updateCustomerDto.address()!= null) {
            if (updateCustomerDto.address().street()!= null && !updateCustomerDto.address().street().isEmpty()) {
                customer.getAddress().setStreet(updateCustomerDto.address().street());
            }
            if (updateCustomerDto.address().number()!= null && !updateCustomerDto.address().number().isEmpty()) {
                customer.getAddress().setNumber(updateCustomerDto.address().number());
            }
            if (updateCustomerDto.address().complement()!= null && !updateCustomerDto.address().complement().isEmpty()) {
                customer.getAddress().setComplement(updateCustomerDto.address().complement());
            }
            if (updateCustomerDto.address().city()!= null && !updateCustomerDto.address().city().isEmpty()) {
                customer.getAddress().setCity(updateCustomerDto.address().city());
            }
            if (updateCustomerDto.address().state()!= null && !updateCustomerDto.address().state().isEmpty()) {
                customer.getAddress().setState(updateCustomerDto.address().state());
            }
            if (updateCustomerDto.address().postalCode()!= null && !updateCustomerDto.address().postalCode().isEmpty()) {
                customer.getAddress().setPostalCode(updateCustomerDto.address().postalCode());
            }
        }

        customerRepository.save(customer);

        return customer;
    }

    public Customer delete(Long id) throws CostumerNotFoundException {
        Customer customer = findCustomerById(id);

        customerRepository.deleteById(id);

        return customer;
    }
}
