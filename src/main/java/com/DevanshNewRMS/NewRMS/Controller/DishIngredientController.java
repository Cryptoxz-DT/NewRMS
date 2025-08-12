package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.DishIngredient;
import com.DevanshNewRMS.NewRMS.Service.DishIngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dish-ingredients")
@RequiredArgsConstructor
public class DishIngredientController {

    private final DishIngredientService dishIngredientService;

    @GetMapping
    public ResponseEntity<List<DishIngredient>> getAllDishIngredients() {
        return ResponseEntity.ok(dishIngredientService.getAllDishIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishIngredient> getDishIngredientById(@PathVariable Long id) {
        return ResponseEntity.ok(dishIngredientService.getDishIngredientById(id));
    }

    @GetMapping("/dish/{dishId}")
    public ResponseEntity<List<DishIngredient>> getIngredientsByDishId(@PathVariable Long dishId) {
        return ResponseEntity.ok(dishIngredientService.getIngredientsByDishId(dishId));
    }

    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<List<DishIngredient>> getDishesByIngredientId(@PathVariable Long ingredientId) {
        return ResponseEntity.ok(dishIngredientService.getDishesByIngredientId(ingredientId));
    }

    @GetMapping("/dish/{dishId}/ingredient/{ingredientId}")
    public ResponseEntity<DishIngredient> getDishIngredientByDishAndIngredient(
            @PathVariable Long dishId, 
            @PathVariable Long ingredientId) {
        return ResponseEntity.ok(dishIngredientService.getDishIngredientByDishAndIngredient(dishId, ingredientId));
    }

    @PostMapping
    public ResponseEntity<DishIngredient> createDishIngredient(@Valid @RequestBody DishIngredient dishIngredient) {
        DishIngredient savedDishIngredient = dishIngredientService.saveDishIngredient(dishIngredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDishIngredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishIngredient> updateDishIngredient(
            @PathVariable Long id, 
            @Valid @RequestBody DishIngredient dishIngredient) {
        return ResponseEntity.ok(dishIngredientService.updateDishIngredient(id, dishIngredient));
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<DishIngredient> updateQuantity(
            @PathVariable Long id, 
            @RequestBody Map<String, Double> request) {
        double newQuantity = request.get("quantity");
        return ResponseEntity.ok(dishIngredientService.updateQuantity(id, newQuantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDishIngredient(@PathVariable Long id) {
        dishIngredientService.deleteDishIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/dish/{dishId}")
    public ResponseEntity<Void> deleteByDishId(@PathVariable Long dishId) {
        dishIngredientService.deleteByDishId(dishId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ingredient/{ingredientId}")
    public ResponseEntity<Void> deleteByIngredientId(@PathVariable Long ingredientId) {
        dishIngredientService.deleteByIngredientId(ingredientId);
        return ResponseEntity.noContent().build();
    }
}