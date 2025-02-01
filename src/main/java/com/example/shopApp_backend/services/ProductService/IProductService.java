package com.example.shopApp_backend.services.ProductService;

import com.example.shopApp_backend.dtos.ProductDTO;
import com.example.shopApp_backend.dtos.ProductImageDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.exceptions.InvalidParamException;
import com.example.shopApp_backend.model.Product;
import com.example.shopApp_backend.model.ProductImage;
import com.example.shopApp_backend.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(Long id) throws DataNotFoundException;

//    List<Product> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    Page<Product> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;

    List<Product> findProductByIds(List<Long> productIds);

    List<ProductImage> findProductImagesById(Long productId);
}
