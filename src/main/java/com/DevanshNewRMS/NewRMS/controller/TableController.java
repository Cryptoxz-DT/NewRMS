package com.DevanshNewRMS.NewRMS.controller;

import com.DevanshNewRMS.NewRMS.Service.TableService;
import com.DevanshNewRMS.NewRMS.model.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    @Autowired
    private TableService tableService;

    @PostMapping
    public TableInfo create(@RequestBody TableInfo tableInfo){
        return tableService.save(tableInfo);
    }

    @GetMapping
    public List<TableInfo> getAll(){
        return tableService.getAll();
    }

    @GetMapping("/{id}")
    public TableInfo getById(Long id){
        return tableService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(Long id){
        tableService.delete(id);
    }
}
