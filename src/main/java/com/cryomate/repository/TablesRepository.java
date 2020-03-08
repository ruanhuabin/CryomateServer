package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cryomate.entity.Tables;


public interface TablesRepository extends JpaRepository<Tables, String> {
	
	Tables getByName(String name);

}
