package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.UserDto;
import com.techtricks.coe_auth.dtos.UserResponseDto;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.exceptions.UserAlreadyPresentException;
import com.techtricks.coe_auth.models.Role;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.UserDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }


//    @Override
//    public void save(User user) {
//        Optional<User> optionalUser = userDetailsRepository.findByUsername(user.getUsername());
//        if(optionalUser.isPresent()){
//            throw new IllegalArgumentException("Username already exists");
//        }
//        user.setUsername(user.getUsername());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(user.getRole() != null ? user.getRole() : Role.STUDENT);
//        userDetailsRepository.save(user);
//    }

    @Override
    public UserResponseDto saveUser(UserDto dto) throws UserAlreadyPresentException {
        Optional<User> optionalUser = userDetailsRepository.findByUsername(dto.getUsername());
        if(optionalUser.isPresent()){
            throw new UserAlreadyPresentException("user already present");
        }
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRegisterNumber(dto.getRegisterNumber());
            user.setRole(dto.getRole() != null ? dto.getRole() : Role.STUDENT);
            User saveduser = userDetailsRepository.save(user);
        return new UserResponseDto(saveduser.getId(), saveduser.getUsername(), saveduser.getEmail());
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        return userDetailsRepository.findAll()
                .stream()
                .map(u-> new UserResponseDto(u.getId() ,u.getUsername() , u.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long registerNumber) throws UserNotFoundException {
        Optional<User> optionalUser = userDetailsRepository.findByRegisterNumber(registerNumber);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UserNotFoundException("user not found");
    }

    @Override
    @Transactional
    public User updateUser(Long registerNumber, User user) throws UserNotFoundException {
        User existingUser = userDetailsRepository.findByRegisterNumber(registerNumber)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if (user.getUsername() != null) existingUser.setUsername(user.getUsername());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() != null) existingUser.setRole(user.getRole());
        if (user.getRegisterNumber() != null) existingUser.setRegisterNumber(user.getRegisterNumber());
        return userDetailsRepository.save(existingUser);
    }
}
