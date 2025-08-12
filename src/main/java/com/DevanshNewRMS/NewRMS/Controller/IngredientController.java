package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.Ingredient;
import com.DevanshNewRMS.NewRMS.Service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Ingredient> getIngredientByName(@PathVariable String name) {
        return ResponseEntity.ok(ingredientService.getIngredientByName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ingredient>> searchIngredients(@RequestParam String name) {
        return ResponseEntity.ok(ingredientService.searchIngredients(name));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Ingredient>> getLowStockIngredients(
            @RequestParam(defaultValue = "10.0") double threshold) {
        return ResponseEntity.ok(ingredientService.getLowStockIngredients(threshold));
    }

    @GetMapping("/by-unit/{unit}")
    public ResponseEntity<List<Ingredient>> getIngredientsByUnit(@PathVariable String unit) {
        return ResponseEntity.ok(ingredientService.getIngredientsByUnit(unit));
    }

    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(@Valid @RequestBody Ingredient ingredient) {
        Ingredient savedIngredient = ingredientService.saveIngredient(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable Long id, 
            @Valid @RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, ingredient));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Ingredient> updateStock(
            @PathVariable Long id, 
            @RequestBody Map<String, Double> request) {
        double newQuantity = request.get("quantity");
        return ResponseEntity.ok(ingredientService.updateStock(id, newQuantity));
    }

    @PatchMapping("/{id}/add-stock")
    public ResponseEntity<Ingredient> addStock(
            @PathVariable Long id, 
            @RequestBody Map<String, Double> request) {
        double quantity = request.get("quantity");
        return ResponseEntity.ok(ingredientService.addStock(id, quantity));
    }

    @PatchMapping("/{id}/reduce-stock")
    public ResponseEntity<Ingredient> reduceStock(
            @PathVariable Long id, 
            @RequestBody Map<String, Double> request) {
        double quantity = request.get("quantity");
        return ResponseEntity.ok(ingredientService.reduceStock(id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}