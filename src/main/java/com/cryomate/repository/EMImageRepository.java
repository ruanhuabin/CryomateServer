package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.EMImage;;
@Repository
public interface EMImageRepository extends JpaRepository<EMImage, String> {

}
