package tech.shopgi.customerms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.shopgi.customerms.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
