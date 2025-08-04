package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Repository.TableRepository;
import com.DevanshNewRMS.NewRMS.Model.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public TableInfo save(TableInfo table){
        return  tableRepository.save(table);
    }

    public List<TableInfo> getAll(){
        return tableRepository.findAll();
    }

    public TableInfo getById(Long id){
        return tableRepository.findById(id).orElse(null);
    }

    public void delete(long id){
        tableRepository.deleteById(id);
    }
}
