package com.example.shopApp_backend.dtos;

import com.example.shopApp_backend.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;

    @NotBlank(message = "password cannot be blank")
    private String password;

    @JsonProperty("role_id")
    private Long roleId;

    @Table(name = "cart")
    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItemDTO {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "product_name", nullable = false)
        private String productName;

        @Column(name = "quantity", nullable = false)
        private int quantity;

        @Column(name = "price", nullable = false)
        private Long price;

        @Column(name = "total", nullable = false)
        private Long total;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
    }
}
