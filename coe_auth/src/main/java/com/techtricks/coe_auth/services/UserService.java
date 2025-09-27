package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.models.Role;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            throw new IllegalArgumentException("Username cannot be empty");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole() != null ? user.getRole() : Role.STUDENT);
        userDetailsRepository.save(newUser);
    }


}
