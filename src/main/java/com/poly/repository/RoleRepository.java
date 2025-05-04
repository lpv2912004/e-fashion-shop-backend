package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.model.Role;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Integer>{
	
}
