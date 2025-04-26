package com.farmdora.farmdora.admin.repository;

import com.farmdora.farmdora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {
}
