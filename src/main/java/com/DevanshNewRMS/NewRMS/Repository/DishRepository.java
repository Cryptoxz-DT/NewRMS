package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.DTO.DishData;
import com.DevanshNewRMS.NewRMS.Model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query("SELECT d FROM Dish d WHERE d.price > :price")
    List<Dish> findExpensiveDishes(@Param("price") double price);


    @Query(value = "select new com.DevanshNewRMS.NewRMS.DTO.DishData(c.name, d.name, d.price) from Dish d\n" +
            "join Category c on d.category.id = c.id\n" +
            "where d.category.id =:categoryId")
    List<DishData> findDishDataByCategory(@Param("categoryId") Long categoryId);


    @Query(value = "select c.name, d.name, d.price from dish d\n" +
            "join category c on d.category_id = c.id\n" +
            "where d.category_id =:categoryId", nativeQuery = true)
    List<Object[]> findDishDataByCategoryNative(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT oi.dish FROM OrderItem oi " +
            "JOIN oi.dish d " +
            "JOIN d.category c " +
            "JOIN oi.order o " +
            "JOIN o.staff s " +
            "WHERE c.name = :categoryName AND s.id = :staffId")
    List<Dish> findDishesSoldByStaffInCategory(
            @Param("categoryName") String categoryName,
            @Param("staffId") Long staffId
    );
}
