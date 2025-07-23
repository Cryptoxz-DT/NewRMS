package com.DevanshNewRMS.NewRMS.Service;


import com.DevanshNewRMS.NewRMS.Repository.DishRepository;
import com.DevanshNewRMS.NewRMS.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;

    public Dish save(Dish dish){
        return dishRepository.save(dish);
    }

    public List<Dish> getAll(){
        return dishRepository.findAll();
    }

    public Dish getById(Long id){
        return dishRepository.findById(id).orElse(null);
    }

    public void delete(Long id){
        dishRepository.deleteById(id);
    }

}
