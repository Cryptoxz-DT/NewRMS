package com.DevanshNewRMS.NewRMS.Config;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        // Check if admin user already exists
        if (staffRepository.findByUsername("admin").isEmpty()) {
            Staff admin = Staff.builder()
                    .name("Administrator")
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles("ADMIN")
                    .build();
            
            staffRepository.save(admin);
            log.info("Created default admin user: admin/password");
        }

        // Check if manager user exists
        if (staffRepository.findByUsername("manager").isEmpty()) {
            Staff manager = Staff.builder()
                    .name("Restaurant Manager")
                    .username("manager")
                    .password(passwordEncoder.encode("manager123"))
                    .roles("MANAGER")
                    .build();
            
            staffRepository.save(manager);
            log.info("Created default manager user: manager/manager123");
        }

        // Check if waiter user exists
        if (staffRepository.findByUsername("waiter").isEmpty()) {
            Staff waiter = Staff.builder()
                    .name("John Waiter")
                    .username("waiter")
                    .password(passwordEncoder.encode("waiter123"))
                    .roles("WAITER")
                    .build();
            
            staffRepository.save(waiter);
            log.info("Created default waiter user: waiter/waiter123");
        }
    }
}