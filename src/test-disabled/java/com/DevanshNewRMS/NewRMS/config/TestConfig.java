// package com.DevanshNewRMS.NewRMS.config;

// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @TestConfiguration
// @Configuration
// public class TestConfig {

//     @Bean
//     @Primary
//     public PasswordEncoder testPasswordEncoder() {
//         // Use a weaker encoder for faster tests
//         return new BCryptPasswordEncoder(4);
//     }
// }