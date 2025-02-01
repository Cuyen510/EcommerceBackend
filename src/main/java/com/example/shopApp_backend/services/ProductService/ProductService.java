package com.example.shopApp_backend.services.ProductService;

import com.example.shopApp_backend.dtos.ProductDTO;
import com.example.shopApp_backend.dtos.ProductImageDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.exceptions.InvalidParamException;
import com.example.shopApp_backend.model.Category;
import com.example.shopApp_backend.model.Product;
import com.example.shopApp_backend.model.ProductImage;
import com.example.shopApp_backend.repositories.CategoryRepository;
import com.example.shopApp_backend.repositories.ProductImageRepository;
import com.example.shopApp_backend.repositories.ProductRepository;
import com.example.shopApp_backend.responses.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->new DataNotFoundException("Category not found with id:" +productDTO.getCategoryId()));
        Product newProduct = new Product();
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setThumbnail(productDTO.getThumbnail());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setCategory(existingCategory);


        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Can not find product with id: "+id));
    }

    @Override
    public Page<Product> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        Page<Product> products = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return products;

    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if(existingProduct!= null){
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()->new DataNotFoundException("Category not found with id:" +productDTO.getCategoryId()));

            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setCategory(existingCategory);
            return productRepository.save(existingProduct);
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()) {
            productRepository.deleteById(id);

        }
        else{
            throw new DataAccessException("product not found") {
            };
        }
    }


    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("product not found with id:" +productImageDTO.getProductId()));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_PER_PRODUCT){
            throw new InvalidParamException("number of image must be <=" +ProductImage.MAXIMUM_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<Product> findProductByIds(List<Long> productIds){
        List<Product> products = productRepository.findProductByIds(productIds);
        return products;
    }

    @Override
    public List<ProductImage> findProductImagesById(Long productId){
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        return productImages;
    }


}
