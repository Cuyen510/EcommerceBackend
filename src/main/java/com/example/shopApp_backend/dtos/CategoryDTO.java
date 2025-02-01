package com.example.shopApp_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CategoryDTO {
   @NotNull(message = "Name can't be null")
   private String name;
}
