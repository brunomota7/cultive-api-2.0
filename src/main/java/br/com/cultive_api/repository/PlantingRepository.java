package br.com.cultive_api.repository;

import br.com.cultive_api.entites.Planting;
import br.com.cultive_api.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlantingRepository extends JpaRepository<Planting, Long> {
    List<Planting> findAllByUserUserId(UUID userId);
    List<Planting> findByUserUserIdAndHarvestedTrue(UUID userId);
    List<Planting> findByUserUserIdAndHarvestedFalse(UUID userId);
}
