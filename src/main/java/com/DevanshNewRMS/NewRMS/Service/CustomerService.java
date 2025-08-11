package com.DevanshNewRMS.NewRMS.Service;


import com.DevanshNewRMS.NewRMS.Model.Customer;
import com.DevanshNewRMS.NewRMS.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    public final CustomerRepository customerRepository;

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
        return List.of();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).get();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        return customerRepository.save(customer);
    }
}
