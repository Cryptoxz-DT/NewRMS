package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Model.Ingredient;
import com.DevanshNewRMS.NewRMS.Repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Ingredient not found with id: " + id));
    }

    public Ingredient getIngredientByName(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Ingredient not found with name: " + name));
    }

    public List<Ingredient> searchIngredients(String name) {
        return ingredientRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Ingredient> getLowStockIngredients(double threshold) {
        return ingredientRepository.findLowStockIngredients(threshold);
    }

    public List<Ingredient> getIngredientsByUnit(String unit) {
        return ingredientRepository.findByUnit(unit);
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        if (ingredientRepository.existsByName(ingredient.getName())) {
            throw new GlobalExceptionHandler.BusinessException("Ingredient with name '" + ingredient.getName() + "' already exists");
        }
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, Ingredient ingredient) {
        Ingredient existingIngredient = getIngredientById(id);
        
        // Check if name is being changed and if new name already exists
        if (!existingIngredient.getName().equals(ingredient.getName()) && 
            ingredientRepository.existsByName(ingredient.getName())) {
            throw new GlobalExceptionHandler.BusinessException("Ingredient with name '" + ingredient.getName() + "' already exists");
        }
        
        existingIngredient.setName(ingredient.getName());
        existingIngredient.setQuantityInStock(ingredient.getQuantityInStock());
        existingIngredient.setUnit(ingredient.getUnit());
        
        return ingredientRepository.save(existingIngredient);
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
    }

    public Ingredient updateStock(Long id, double newQuantity) {
        Ingredient ingredient = getIngredientById(id);
        ingredient.setQuantityInStock(newQuantity);
        return ingredientRepository.save(ingredient);
    }

    public Ingredient addStock(Long id, double quantity) {
        Ingredient ingredient = getIngredientById(id);
        ingredient.setQuantityInStock(ingredient.getQuantityInStock() + quantity);
        return ingredientRepository.save(ingredient);
    }

    public Ingredient reduceStock(Long id, double quantity) {
        Ingredient ingredient = getIngredientById(id);
        double newQuantity = ingredient.getQuantityInStock() - quantity;
        
        if (newQuantity < 0) {
            throw new GlobalExceptionHandler.BusinessException("Insufficient stock for ingredient: " + ingredient.getName());
        }
        
        ingredient.setQuantityInStock(newQuantity);
        return ingredientRepository.save(ingredient);
    }
}