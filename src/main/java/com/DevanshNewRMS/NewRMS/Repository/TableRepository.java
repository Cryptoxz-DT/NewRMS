package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.model.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<TableInfo, Long> {
}
