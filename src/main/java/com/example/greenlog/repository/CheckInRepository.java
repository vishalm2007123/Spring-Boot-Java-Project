package com.example.greenlog.repository;

import com.example.greenlog.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByTreeIdOrderByCheckInDateDesc(Long treeId);
}
