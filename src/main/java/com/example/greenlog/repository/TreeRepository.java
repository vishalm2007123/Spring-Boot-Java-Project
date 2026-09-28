package com.example.greenlog.repository;

import com.example.greenlog.model.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TreeRepository extends JpaRepository<Tree, Long> {
    List<Tree> findByPlantationDriveId(Long plantationDriveId);
    List<Tree> findBySpeciesIgnoreCase(String species);
    List<Tree> findByNextCheckInDateLessThanEqualAndStatusNotIgnoreCase(LocalDate date, String status);
    long countByVolunteerId(Long volunteerId);
    long countByPlantationDriveId(Long plantationDriveId);
    long countByPlantationDriveIdAndStatusIgnoreCase(Long plantationDriveId, String status);
    long countBySpeciesIgnoreCase(String species);
    long countBySpeciesIgnoreCaseAndStatusIgnoreCase(String species, String status);

    @Query("select t from Tree t where lower(t.species) = lower(:species) order by t.id")
    List<Tree> findTreesBySpecies(@Param("species") String species);
}
