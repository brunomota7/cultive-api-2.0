package br.com.cultive_api.repository;

import br.com.cultive_api.entites.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    List<Harvest> findAllByUserUserId(UUID userId);
}
