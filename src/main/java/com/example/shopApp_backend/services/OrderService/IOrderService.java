package com.example.shopApp_backend.services.OrderService;


import com.example.shopApp_backend.dtos.OrderDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException, Exception;

    Order getOrder(Long id) throws DataNotFoundException;

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id) throws DataNotFoundException;

    Page<Order> getOrders(String keyword, Long userId, Pageable pageable) throws DataNotFoundException;
}
