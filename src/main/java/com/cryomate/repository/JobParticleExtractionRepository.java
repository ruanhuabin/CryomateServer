package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.JobParticleExtraction;
@Repository
public interface JobParticleExtractionRepository extends JpaRepository<JobParticleExtraction, String> {

}
