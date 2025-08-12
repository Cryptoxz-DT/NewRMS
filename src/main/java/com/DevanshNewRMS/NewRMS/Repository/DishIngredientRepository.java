package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.DishIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishIngredientRepository extends JpaRepository<DishIngredient, Long> {
    
    List<DishIngredient> findByDishId(Long dishId);
    
    List<DishIngredient> findByIngredientId(Long ingredientId);
    
    @Query("SELECT di FROM DishIngredient di WHERE di.dish.id = :dishId AND di.ingredient.id = :ingredientId")
    DishIngredient findByDishIdAndIngredientId(@Param("dishId") Long dishId, @Param("ingredientId") Long ingredientId);
    
    @Query("SELECT di FROM DishIngredient di JOIN FETCH di.dish JOIN FETCH di.ingredient")
    List<DishIngredient> findAllWithDishAndIngredient();
    
    void deleteByDishId(Long dishId);
    
    void deleteByIngredientId(Long ingredientId);
}