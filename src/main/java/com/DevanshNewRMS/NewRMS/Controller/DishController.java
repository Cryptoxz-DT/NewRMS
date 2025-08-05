package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Service.DishService;
import com.DevanshNewRMS.NewRMS.DTO.DishData;
import com.DevanshNewRMS.NewRMS.Model.Dish;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Dish create(@Valid @RequestBody Dish dish){
        return dishService.save(dish);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public List<Dish> getAll(){
        return dishService.getAll();
    }

    @GetMapping("/{id}")
    public Dish getById(@PathVariable Long id){
        return dishService.getById(id);
    }

    @GetMapping("/expensive")
    public List<Dish> getExpensiveDishes(@RequestParam("price") double price) {
        return dishService.getExpensiveDishes(price);
    }

    @GetMapping("/dish/{categoryId}")
    public List<DishData> getExpensiveDishes(@PathVariable Long categoryId) {
        return dishService.getDishDataByCategory(categoryId);
    }

    @GetMapping("/search/by-sale")
    public List<Dish> getDishesBySale(
            @RequestParam String categoryName,
            @RequestParam Long staffId) {
        return dishService.findDishesSoldByStaffInCategory(categoryName, staffId);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        dishService.delete(id);
    }
}
