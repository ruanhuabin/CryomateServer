package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.Job2DClassification;;
@Repository
public interface Job2DClassificationRepository extends JpaRepository<Job2DClassification, String> {

}
