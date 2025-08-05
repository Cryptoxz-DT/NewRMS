package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public JpaUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Staff staff =  new Staff(); staffRepository.findByUsername(username);

        return User.builder()
                .username(staff.getUsername())
                .password(staff.getPassword())
                .roles(staff.getRole())
                .build();
    }
}