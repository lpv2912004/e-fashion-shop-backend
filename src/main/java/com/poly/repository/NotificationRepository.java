package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	
	@Query("SELECT o FROM Notification o WHERE o.user.id = ?1")
	List<Notification> findNotification(Integer idUser);
}
