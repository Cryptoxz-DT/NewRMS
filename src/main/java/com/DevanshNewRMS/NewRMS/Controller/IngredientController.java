package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.Ingredient;
import com.DevanshNewRMS.NewRMS.Service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@PreAuthorize("hasRole('ADMIN')")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @PostMapping
    public Ingredient create(@Valid @RequestBody Ingredient ingredient) {
        return ingredientService.save(ingredient);
    }

    @GetMapping
    public List<Ingredient> getAll() {
        return ingredientService.getAll();
    }

    @GetMapping("/{id}")
    public Ingredient getById(@PathVariable Long id) {
        return ingredientService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ingredientService.delete(id);
    }
}