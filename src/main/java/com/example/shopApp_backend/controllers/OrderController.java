package com.example.shopApp_backend.controllers;

import com.example.shopApp_backend.components.LocalizationUtils;
import com.example.shopApp_backend.dtos.OrderDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.model.Order;
import com.example.shopApp_backend.model.Product;
import com.example.shopApp_backend.responses.*;
import com.example.shopApp_backend.services.OrderService.IOrderService;
import com.example.shopApp_backend.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;


    @PostMapping("")
    public ResponseEntity<ResponseObject> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) throws Exception{
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError :: getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder().message(String.join(";",errorMessages))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        Order orderResponse = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Insert order successfully")
                .data(orderResponse)
                .status(HttpStatus.OK)
                .build());
    }


    @GetMapping("/user")
    public ResponseEntity<OrderListResponse> getOrders(
            @RequestParam(defaultValue = "", name = "user_id") Long userId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) throws DataNotFoundException {

        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("orderDate").descending()
        );
        Page<Order> orderPage = orderService.getOrders(keyword, userId, pageRequest);
        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orders = new ArrayList<>();
        orderPage.forEach(order -> {
            orders.add(OrderResponse.fromOrder(order));
        });
        return ResponseEntity.ok(OrderListResponse
                .builder()
                .orders(orders)
                .totalPages(totalPages)
                .build());

//        List<Order> orders = orderService.findByUserId(userId);
//        List<OrderResponse> orderResponses = new ArrayList<>();
//        orders.forEach(order -> {
//            orderResponses.add(OrderResponse.fromOrder(order));
//
//        });
//        return ResponseEntity.ok(orderResponses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrder(@Valid @PathVariable("id") Long orderId) throws DataNotFoundException {
        Order existingOrder = orderService.getOrder(orderId);
        OrderResponse orderResponse = OrderResponse.fromOrder(existingOrder);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Get orders successfully")
                .data(orderResponse)
                .status(HttpStatus.OK)
                .build());
    }

    @PutMapping("/{id}")
    //công việc của admin
    public ResponseEntity<ResponseObject> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO) throws Exception {

        Order order = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(new ResponseObject("Update order successfully", HttpStatus.OK, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteOrder(@Valid @PathVariable Long id) throws DataNotFoundException {
        orderService.deleteOrder(id);
        String message = localizationUtils.getLocalizedMessage(
                MessageKeys.DELETE_ORDER_SUCCESSFULLY, id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message(message)
                        .build()
        );
    }
}
