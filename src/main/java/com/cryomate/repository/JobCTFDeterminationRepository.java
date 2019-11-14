package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.JobCTFDetermination;
@Repository
public interface JobCTFDeterminationRepository extends JpaRepository<JobCTFDetermination, String> {

}
