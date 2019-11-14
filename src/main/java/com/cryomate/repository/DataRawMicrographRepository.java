package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.DataRawMicrograph;;
@Repository
public interface DataRawMicrographRepository extends JpaRepository<DataRawMicrograph, String> {

}
