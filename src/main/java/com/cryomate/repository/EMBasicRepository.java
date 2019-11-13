package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.EMBasic;
import com.cryomate.model.SystemKeyword;


@Repository
public interface EMBasicRepository extends JpaRepository<EMBasic, String> {

}
