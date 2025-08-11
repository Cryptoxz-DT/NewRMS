package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.Customer;
import com.DevanshNewRMS.NewRMS.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll(); // Fixed: was returning empty list
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = getCustomerById(id);
        existing.setName(customer.getName());
        existing.setPhone(customer.getPhone());
        existing.setEmail(customer.getEmail());
        return customerRepository.save(existing);
    }

    // Added missing method that was being called in controller
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}