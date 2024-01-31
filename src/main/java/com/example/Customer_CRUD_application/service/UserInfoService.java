package com.example.Customer_CRUD_application.service;

import com.example.Customer_CRUD_application.exception.RegistrationException;
import com.example.Customer_CRUD_application.model.UserInfo;
import com.example.Customer_CRUD_application.repo.UserInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    UserInfoRepository userInfoRepository;


    public void addUser(UserInfo userInfo) {
        if (userInfoRepository.findByEmail(userInfo.getEmail()).isPresent()) {
            throw new RegistrationException("Email address is already registered");
        }
        userInfo.setPassword(new BCryptPasswordEncoder().encode(userInfo.getPassword()));
//        userInfo.setRole("USER");
        userInfoRepository.save(userInfo);
    }

    public List<UserInfo> getAllUserInfo() {
        return userInfoRepository.findAll();
    }

    public UserInfo findByEmail(String email) {
        Optional<Object> byEmail = userInfoRepository.findByEmail(email);

        return (UserInfo) byEmail.get();
    }

    public String save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
        return "getAllUser";
    }

    public UserInfo getUserById(Long userId) {
        Optional<UserInfo> byId = userInfoRepository.findById(userId);
        return byId.get();
    }

    @Transactional
    public UserInfo saveUserInfo(@Valid UserInfo userInfo) {
        // Validation will be triggered here

        Optional<UserInfo> existingUserOptional = userInfoRepository.findById(userInfo.getId());

        if (existingUserOptional.isPresent()) {
            // Update the existing user details
            UserInfo existingUser = existingUserOptional.get();
            existingUser.setFirstName(userInfo.getFirstName());
            existingUser.setLastName(userInfo.getLastName());
            existingUser.setEmail(userInfo.getEmail());
            existingUser.setAddress(userInfo.getAddress());
            existingUser.setStreet(userInfo.getStreet());
            existingUser.setCity(userInfo.getCity());
//            existingUser.setPassword(userInfo.getPassword());
            existingUser.setPhone(userInfo.getPhone());

            // Save the updated user
            return userInfoRepository.save(existingUser);
        }

        throw new EntityNotFoundException("User not found with id: " + userInfo.getId());
    }


    public void deleteUser(Long userId) {
    }

    public boolean deleteEmp(int id) {
        UserInfo user = userInfoRepository.findById((long) id).get();
        if (user != null) {
            userInfoRepository.delete(user);
            return true;
        }
        return false;
    }

//    public void removeSessionMessage() {
//        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
//                .getSession();
//
//        session.removeAttribute("msg");
//
//    }

    public String removeSessionMessage() {
        // Clear the session message after displaying
        String msg = "Registration successful!";
        return msg;
    }

    private List<UserInfo> userInfoList = new ArrayList<>();







    public Page<UserInfo> getAllUsers(Pageable pageable) {
        return userInfoRepository.findAll(pageable);
    }




    @Transactional
    public List<UserInfo> getPaginatedUsers(int page, int size, String sortBy, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        if (search != null && !search.isEmpty()) {
            // If search parameter is provided, perform search based on multiple fields
            return userInfoRepository.findByFirstNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                    search, search, search, search, pageable).getContent();
        } else {
            // If search parameter is not provided, get all users with pagination and sorting
            Page<UserInfo> userPage = userInfoRepository.findAll(pageable);
            return userPage.getContent();
        }
    }

    public void addCustomer(UserInfo user) {
        user.setRole("USER");
        userInfoRepository.save(user);
    }
}
