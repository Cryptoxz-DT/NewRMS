package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Repository.CategoryRepository;
import com.DevanshNewRMS.NewRMS.Model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public Category getById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    public void delete(Long id){
        categoryRepository.deleteById(id);
    }
}
