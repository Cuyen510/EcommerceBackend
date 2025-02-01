package com.example.shopApp_backend.services.UserService;

import com.example.shopApp_backend.components.JwtTokenUtils;
import com.example.shopApp_backend.components.LocalizationUtils;
import com.example.shopApp_backend.dtos.UserDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.exceptions.PermissionDenyException;
import com.example.shopApp_backend.model.Role;
import com.example.shopApp_backend.model.User;
import com.example.shopApp_backend.repositories.RoleRepository;
import com.example.shopApp_backend.repositories.UserRepository;
import com.example.shopApp_backend.utils.MessageKeys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role id not found"));
        if(role.getName().equals(Role.ADMIN)){
            throw new PermissionDenyException("you can't register an admin account");
        }

        User newUser = new User();
        newUser.setFullname(userDTO.getFullName());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setPassword(userDTO.getPassword());
        newUser.setAddress(userDTO.getAddress());
        newUser.setDateOfBirth(userDTO.getDateOfBirth());
        newUser.setFacebookAccountId(userDTO.getFacebookAccountId());
        newUser.setGoogleAccountId(userDTO.getGoogleAccountId());


        newUser.setRole(role);
        if(userDTO.getFacebookAccountId()==0 && userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()) {
                throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
        User existingUser = optionalUser.get();
        if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
            }
        }
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isEmpty() || !roleId.equals(existingUser.getRole().getId())){
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS));
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password
        );
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new BadCredentialsException("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }
}
