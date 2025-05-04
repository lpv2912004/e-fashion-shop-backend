package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.model.Role;
import com.poly.model.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmailAndPassword(String email, String password);
    public User findByUsernameAndPassword(String username, String password);
    @Query("SELECT COUNT(u) FROM User u")
    Long countUsers();
    
    User findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    User findByRole(Role role);
    
    @Query("SELECT u FROM User u WHERE CAST(u.created_date AS date) = CAST(GETDATE() AS date)")
    List<User> findUsersCreatedToday();
}
