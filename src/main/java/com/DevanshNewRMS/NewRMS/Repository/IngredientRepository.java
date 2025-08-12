package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    Optional<Ingredient> findByName(String name);
    
    List<Ingredient> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT i FROM Ingredient i WHERE i.quantityInStock < :threshold")
    List<Ingredient> findLowStockIngredients(@Param("threshold") double threshold);
    
    @Query("SELECT i FROM Ingredient i WHERE i.unit = :unit")
    List<Ingredient> findByUnit(@Param("unit") String unit);
    
    boolean existsByName(String name);
}