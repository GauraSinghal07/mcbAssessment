package com.assignment.mcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assignment.mcb.model.FileDetails;

@Repository
public interface FileRepository extends JpaRepository<FileDetails, String> {

	@Query("SELECT f FROM FileDetails f WHERE f.status=:status")
	List<FileDetails> findAllByStatus(@Param("status") String status);
}
