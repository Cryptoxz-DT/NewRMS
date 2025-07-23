package com.DevanshNewRMS.NewRMS.controller;

import com.DevanshNewRMS.NewRMS.Service.DishService;
import com.DevanshNewRMS.NewRMS.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public Dish create(@RequestBody Dish dish){
        return dishService.save(dish);
    }

    @GetMapping
    public List<Dish> getAll(){
        return dishService.getAll();
    }

    @GetMapping("/{id}")
    public Dish getById(@PathVariable Long id){
        return dishService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        dishService.delete(id);
    }
}
