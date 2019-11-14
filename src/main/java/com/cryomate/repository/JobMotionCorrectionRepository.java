package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.JobMotionCorrection;
@Repository
public interface JobMotionCorrectionRepository extends JpaRepository<JobMotionCorrection, String> {

}
