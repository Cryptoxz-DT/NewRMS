package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.DevanshNewRMS.NewRMS.Model.Ingredient;
import com.DevanshNewRMS.NewRMS.Repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient getById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
    }

    public void deductStock(Long ingredientId, double quantity) {
        Ingredient ingredient = getById(ingredientId);
        if (ingredient.getQuantityInStock() < quantity) {
            throw new IllegalStateException("Not enough stock for ingredient: " + ingredient.getName());
        }
        ingredient.setQuantityInStock(ingredient.getQuantityInStock() - quantity);
        ingredientRepository.save(ingredient);
    }

    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }
}
