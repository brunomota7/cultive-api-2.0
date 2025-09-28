package br.com.cultive_api.repository;

import br.com.cultive_api.entites.Planting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlantingRepository extends JpaRepository<Planting, Long> {
    Page<Planting> findAllByUserUserId(UUID userId, Pageable pageable);
    Page<Planting> findByUserUserIdAndHarvestedTrue(UUID userId,Pageable pageable);
    Page<Planting> findByUserUserIdAndHarvestedFalse(UUID userId, Pageable pageable);
}
