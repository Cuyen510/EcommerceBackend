package com.example.shopApp_backend.services.OrderDetailService;

import com.example.shopApp_backend.dtos.CartItemDTO;
import com.example.shopApp_backend.dtos.OrderDetailDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.model.Order;
import com.example.shopApp_backend.model.OrderDetail;
import com.example.shopApp_backend.model.Product;
import com.example.shopApp_backend.repositories.OrderDetailRepository;
import com.example.shopApp_backend.repositories.OrderRepository;
import com.example.shopApp_backend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public OrderDetail createOrderDetail(Long orderId, CartItemDTO cartItemDTO) throws DataNotFoundException {
        Optional<Order> order = orderRepository.findById(orderId);
        Optional <Product> product= productRepository.findById(cartItemDTO.getProductId());

        if(order.isPresent() && product.isPresent()){
            OrderDetail orderDetail = OrderDetail.builder()
                        .order(order.get())
                        .product(product.get())
                        .numberOfProduct(cartItemDTO.getQuantity())
                        .price(product.get().getPrice())
                        .totalMoney(product.get().getPrice()*cartItemDTO.getQuantity())
                        .build();
            orderDetailRepository.save(orderDetail);
            return orderDetail;
        }
        return null;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
        return orderDetail;
    }


    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("order detail not found"));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("product not found"));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("order not found"));

        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProduct());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        return orderDetails;
    }
}
