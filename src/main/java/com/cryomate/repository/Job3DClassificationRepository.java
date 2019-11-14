package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.Job3DClassification;;
@Repository
public interface Job3DClassificationRepository extends JpaRepository<Job3DClassification, String> {

}
