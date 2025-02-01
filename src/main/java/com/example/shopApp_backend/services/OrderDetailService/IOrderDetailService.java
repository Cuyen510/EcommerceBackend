package com.example.shopApp_backend.services.OrderDetailService;

import com.example.shopApp_backend.dtos.CartItemDTO;
import com.example.shopApp_backend.dtos.OrderDetailDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(Long orderId, CartItemDTO cartItemDTO) throws DataNotFoundException;

    OrderDetail getOrderDetail(Long id);

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteById(Long id);

    List<OrderDetail> findByOrderId(Long orderId);

}
