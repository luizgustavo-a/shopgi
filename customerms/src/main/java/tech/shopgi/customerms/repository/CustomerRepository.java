package tech.shopgi.customerms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.shopgi.customerms.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerById(Long id);

    Optional<Customer> findCustomerByDocument(String document);
}
