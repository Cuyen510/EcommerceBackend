package com.example.shopApp_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order id must be > 1")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product id must be > 1")
    private Long productId;

    @Min(value = 0, message = "order id must be >= 0")
    private Float price;

    @JsonProperty("number_of_product")
    @Min(value = 1, message = "number of product must be > 1")
    private int numberOfProduct;

    @JsonProperty("total_money")
    @Min(value = 0, message = "total money must be >= 0")
    private Float totalMoney;

//    @JsonProperty("payment_method")
//    private String paymentMethod;
}
