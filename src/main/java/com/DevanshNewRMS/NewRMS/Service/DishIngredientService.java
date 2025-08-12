package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Model.DishIngredient;
import com.DevanshNewRMS.NewRMS.Repository.DishIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishIngredientService {

    private final DishIngredientRepository dishIngredientRepository;

    public List<DishIngredient> getAllDishIngredients() {
        return dishIngredientRepository.findAllWithDishAndIngredient();
    }

    public DishIngredient getDishIngredientById(Long id) {
        return dishIngredientRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("DishIngredient not found with id: " + id));
    }

    public List<DishIngredient> getIngredientsByDishId(Long dishId) {
        return dishIngredientRepository.findByDishId(dishId);
    }

    public List<DishIngredient> getDishesByIngredientId(Long ingredientId) {
        return dishIngredientRepository.findByIngredientId(ingredientId);
    }

    public DishIngredient getDishIngredientByDishAndIngredient(Long dishId, Long ingredientId) {
        DishIngredient dishIngredient = dishIngredientRepository.findByDishIdAndIngredientId(dishId, ingredientId);
        if (dishIngredient == null) {
            throw new GlobalExceptionHandler.ResourceNotFoundException(
                "DishIngredient not found for dish id: " + dishId + " and ingredient id: " + ingredientId);
        }
        return dishIngredient;
    }

    public DishIngredient saveDishIngredient(DishIngredient dishIngredient) {
        // Check if this dish-ingredient combination already exists
        DishIngredient existing = dishIngredientRepository.findByDishIdAndIngredientId(
            dishIngredient.getDish().getId(), 
            dishIngredient.getIngredient().getId()
        );
        
        if (existing != null) {
            throw new GlobalExceptionHandler.BusinessException(
                "This dish-ingredient combination already exists. Use update instead.");
        }
        
        return dishIngredientRepository.save(dishIngredient);
    }

    public DishIngredient updateDishIngredient(Long id, DishIngredient dishIngredient) {
        DishIngredient existingDishIngredient = getDishIngredientById(id);
        
        existingDishIngredient.setQuantity(dishIngredient.getQuantity());
        
        return dishIngredientRepository.save(existingDishIngredient);
    }

    public DishIngredient updateQuantity(Long id, double newQuantity) {
        DishIngredient dishIngredient = getDishIngredientById(id);
        dishIngredient.setQuantity(newQuantity);
        return dishIngredientRepository.save(dishIngredient);
    }

    public void deleteDishIngredient(Long id) {
        if (!dishIngredientRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("DishIngredient not found with id: " + id);
        }
        dishIngredientRepository.deleteById(id);
    }

    public void deleteByDishId(Long dishId) {
        dishIngredientRepository.deleteByDishId(dishId);
    }

    public void deleteByIngredientId(Long ingredientId) {
        dishIngredientRepository.deleteByIngredientId(ingredientId);
    }
}