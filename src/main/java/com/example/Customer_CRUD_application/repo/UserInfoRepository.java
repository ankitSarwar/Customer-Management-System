package com.example.Customer_CRUD_application.repo;

import com.example.Customer_CRUD_application.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {


    Optional<UserInfo> findByFirstName(String username);

    Optional<Object> findByEmail(String email);

    UserInfo findFirstByEmail(String email);

    Page<UserInfo> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<UserInfo> findByFirstNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String firstName, String city, String email, String phone, Pageable pageable);
}