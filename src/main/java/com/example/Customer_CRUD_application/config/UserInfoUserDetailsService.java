package com.example.Customer_CRUD_application.config;


import com.example.Customer_CRUD_application.model.UserInfo;
import com.example.Customer_CRUD_application.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<Object> userInfo = userInfoRepository.findByEmail(email);
//        return userInfo.map(UserInfoUserDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("user not found " + email));
//
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //Write Logic to get the user from the DB
        UserInfo user = userInfoRepository.findFirstByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
