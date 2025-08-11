package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.Customer;
import com.DevanshNewRMS.NewRMS.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {

    private final CustomerRepository customerRepository;


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }


    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }


    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = getCustomerById(id);
        if (existing != null) {
            existing.setName(customer.getName());
            existing.setPhone(customer.getPhone());
            existing.setEmail(customer.getEmail());
            return customerRepository.save(existing);
        }
        return null;
    }


    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
