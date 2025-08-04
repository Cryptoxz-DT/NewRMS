package com.DevanshNewRMS.NewRMS.Service;


import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Repository.DishRepository;
import com.DevanshNewRMS.NewRMS.DTO.DishData;
import com.DevanshNewRMS.NewRMS.Model.Dish;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class    DishService {
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

    public List<Dish> getExpensiveDishes(double price) {

        return dishRepository.findExpensiveDishes(price);
    }

    public List<Dish> findDishesSoldByStaffInCategory(String categoryName, Long staffId) {
        return dishRepository.findDishesSoldByStaffInCategory(categoryName, staffId);
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Dish not found with id: " + id));
    }

    public List<DishData> getDishDataByCategory(Long categoryId) {

        List<DishData> resultList = new ArrayList<>();

        List<Object[]> nativeList = dishRepository.findDishDataByCategoryNative(categoryId);

// return dishRepository.findDishDataByCategory(categoryId);

        ObjectMapper objectMapper = new ObjectMapper();

        for(Object[] data : nativeList) {

            DishData dishData = new DishData();

            dishData.setCategoryName((String) data[0]);

            dishData.setDishName((String) data[1]);

            dishData.setDishPrice((Double) data[2]);

            resultList.add(dishData);

        }
        return resultList;
    }
}
