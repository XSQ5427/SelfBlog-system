package com.example.selfblog.repository;

import com.example.selfblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface UserRepository extends JpaRepository<User, Integer>,
        JpaSpecificationExecutor<User> {
    User findOneByPhoneNumber(String phoneNumber);
}
