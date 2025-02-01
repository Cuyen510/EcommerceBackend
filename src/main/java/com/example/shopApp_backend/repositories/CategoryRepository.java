package com.example.shopApp_backend.repositories;

import com.example.shopApp_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
