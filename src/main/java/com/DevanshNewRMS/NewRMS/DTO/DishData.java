package com.DevanshNewRMS.NewRMS.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DishData {
    @NotBlank(message = "This Field is Required")
    private String categoryName;
    @NotBlank(message = "Dish name cannot be empty")
    private String dishName;
    @Positive(message = "Price needs to be positive")
    @NotNull(message = "this cannot be null")
    private double dishPrice;

    public DishData(String categoryName, String dishName, double dishPrice) {
        this.categoryName = categoryName;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
    }
}
