package com.example.shopApp_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1,message = "user id must be > 1")
    private long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number cannot be blank")
    @Size(min = 8, message = "phone number must be at least 8 characters")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private String status;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("total_money")
    @Min(value = 0,message = "total must be >= 0 ")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("cart_items")
    List<CartItemDTO> cartItems;


}
