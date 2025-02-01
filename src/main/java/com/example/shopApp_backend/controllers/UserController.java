package com.example.shopApp_backend.controllers;


import com.example.shopApp_backend.components.LocalizationUtils;
import com.example.shopApp_backend.dtos.UserDTO;
import com.example.shopApp_backend.dtos.UserLoginDTO;
import com.example.shopApp_backend.model.User;
import com.example.shopApp_backend.responses.user.LoginResponse;
import com.example.shopApp_backend.responses.user.RegisterResponse;
import com.example.shopApp_backend.responses.ResponseObject;
import com.example.shopApp_backend.responses.user.UserResponse;
import com.example.shopApp_backend.services.UserService.IUserService;
import com.example.shopApp_backend.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    //can we register an "admin" user ?
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            registerResponse.setMessage(errorMessages.toString());
            return ResponseEntity.badRequest().body(registerResponse);
        }

        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
            return ResponseEntity.badRequest().body(registerResponse);
        }

        try {
            User user = userService.createUser(userDTO);
            registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY));
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId()
            );
            //String userAgent = request.getHeader("User-Agent");
            User userDetail = userService.getUserDetailsFromToken(token);
            //Token jwtToken = tokenService.addToken(userDetail, token, isMobileDevice(userAgent));

            LoginResponse loginResponse = LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .username(userDetail.getUsername())
                    .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                    .id(userDetail.getId())
                    .build();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Login successfully")
                    .data(loginResponse)
                    .status(HttpStatus.OK)
                    .build());

            // Trả về token trong response
//            return ResponseEntity.ok(LoginResponse.builder()
//                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
//                            .token(token)
//                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Login fail")
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @PostMapping("/details")
    public ResponseEntity<ResponseObject> getUserDetails(
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {
        String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
        User user = userService.getUserDetailsFromToken(extractedToken);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
