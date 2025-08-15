package com.main.user.services.repositories;

import com.main.user.services.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {



}
