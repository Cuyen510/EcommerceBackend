package com.example.shopApp_backend.controllers;


import com.example.shopApp_backend.components.LocalizationUtils;
import com.example.shopApp_backend.dtos.CartItemDTO;
import com.example.shopApp_backend.dtos.OrderDetailDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.model.OrderDetail;
import com.example.shopApp_backend.responses.OrderDetailResponse;
import com.example.shopApp_backend.responses.ResponseObject;
import com.example.shopApp_backend.services.OrderDetailService.OrderDetailService;
import com.example.shopApp_backend.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createOrderDetail(
            @RequestParam("order_id") Long orderId,
            @Valid  @RequestBody List<CartItemDTO> cartItemDTOs) throws Exception {

        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        cartItemDTOs.stream().forEach(cartItemDTO -> {
            OrderDetail newOrderDetail = null;
            try {
                newOrderDetail = orderDetailService.createOrderDetail(orderId, cartItemDTO);
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOrderDetail(newOrderDetail);
            orderDetailResponses.add(orderDetailResponse);
        });

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Create order detail successfully")
                        .status(HttpStatus.CREATED)
                        .data(orderDetailResponses)
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOrderDetail(orderDetail);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get order detail successfully")
                        .status(HttpStatus.OK)
                        .data(orderDetailResponse)
                        .build()
        );
    }



    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseObject> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId
    ) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get order details by orderId successfully")
                        .status(HttpStatus.OK)
                        .data(orderDetailResponses)
                        .build()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO) throws DataNotFoundException, Exception {
        OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok().body(ResponseObject
                .builder()
                .data(orderDetail)
                .message("Update order detail successfully")
                .status(HttpStatus.OK)
                .build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .message(localizationUtils
                                .getLocalizedMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY))
                        .build());
    }
}
