package tech.shopgi.customerms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.shopgi.customerms.dto.CreateCustomerDto;
import tech.shopgi.customerms.dto.UpdateCostumerDto;
import tech.shopgi.customerms.model.Address;
import tech.shopgi.customerms.model.Customer;
import tech.shopgi.customerms.model.Gender;
import tech.shopgi.customerms.model.exception.CostumerNotFoundException;
import tech.shopgi.customerms.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

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

    public Customer update(UpdateCostumerDto updateCostumerDto) throws CostumerNotFoundException {
        Customer customer = findCustomerById(updateCostumerDto.id());

        if (updateCostumerDto.name()!= null && !updateCostumerDto.name().isEmpty()) {
            customer.setName(updateCostumerDto.name());
        }
        if (updateCostumerDto.document()!= null && !updateCostumerDto.document().isEmpty()) {
            customer.setDocument(updateCostumerDto.document());
        }
        if (updateCostumerDto.dateOfBirth()!= null && !updateCostumerDto.dateOfBirth().isEmpty()) {
            customer.setDateOfBirth(updateCostumerDto.dateOfBirth());
        }
        if (updateCostumerDto.gender()!= null && !updateCostumerDto.gender().isEmpty()) {
            customer.setGender(Gender.fromString(updateCostumerDto.gender()));
        }
        if (updateCostumerDto.email()!= null && !updateCostumerDto.email().isEmpty()) {
            customer.setEmail(updateCostumerDto.email());
        }
        if (updateCostumerDto.contactNumber()!= null && !updateCostumerDto.contactNumber().isEmpty()) {
            customer.setContactNumber(updateCostumerDto.contactNumber());
        }
        if (updateCostumerDto.address()!= null) {
            if (updateCostumerDto.address().street()!= null && !updateCostumerDto.address().street().isEmpty()) {
                customer.getAddress().setStreet(updateCostumerDto.address().street());
            }
            if (updateCostumerDto.address().number()!= null && !updateCostumerDto.address().number().isEmpty()) {
                customer.getAddress().setNumber(updateCostumerDto.address().number());
            }
            if (updateCostumerDto.address().complement()!= null && !updateCostumerDto.address().complement().isEmpty()) {
                customer.getAddress().setComplement(updateCostumerDto.address().complement());
            }
            if (updateCostumerDto.address().city()!= null && !updateCostumerDto.address().city().isEmpty()) {
                customer.getAddress().setCity(updateCostumerDto.address().city());
            }
            if (updateCostumerDto.address().state()!= null && !updateCostumerDto.address().state().isEmpty()) {
                customer.getAddress().setState(updateCostumerDto.address().state());
            }
            if (updateCostumerDto.address().postalCode()!= null && !updateCostumerDto.address().postalCode().isEmpty()) {
                customer.getAddress().setPostalCode(updateCostumerDto.address().postalCode());
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
