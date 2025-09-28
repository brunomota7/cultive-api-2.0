package br.com.cultive_api.repository;

import br.com.cultive_api.entites.Harvest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    Page<Harvest> findAllByUserUserId(UUID userId, Pageable pageable);
}
