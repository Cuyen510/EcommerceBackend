package com.example.shopApp_backend.services.CategoryService;
import com.example.shopApp_backend.dtos.CategoryDTO;
import com.example.shopApp_backend.model.Category;
import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}
