package com.example.Customer_CRUD_application.controller;

import com.example.Customer_CRUD_application.config.UserInfoUserDetailsService;
import com.example.Customer_CRUD_application.dto.AuthRequest;
import com.example.Customer_CRUD_application.dto.AuthenticationResponse;
import com.example.Customer_CRUD_application.exception.RegistrationException;
import com.example.Customer_CRUD_application.model.UserInfo;
import com.example.Customer_CRUD_application.service.JwtService;
import com.example.Customer_CRUD_application.service.UserInfoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

@Controller
//@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userInfoUserDetailsService;


    @GetMapping("/welcome")
    public String welcome() {
        return "registration";
    }

    @PostMapping("/addNewUser")
    @ResponseBody
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        try {
            userInfoService.addUser(userInfo);
            return ResponseEntity.ok("{\"message\":\"User registered successfully.\"}");
        } catch (RegistrationException e) {
            return ResponseEntity.status(400).body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\":\"An error occurred. Please try again.\"}");
        }
    }


    @GetMapping("/login")
    public String log(){
        return "login";
    }

    @PostMapping("/logIn")
    public ResponseEntity<AuthenticationResponse> logInUser(@RequestBody AuthRequest authRequest, HttpServletResponse response, Model m)
            throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password!");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final UserDetails userDetails = userInfoUserDetailsService.loadUserByUsername(authRequest.getEmail());
        UserInfo userInfo=userInfoService.findByEmail(authRequest.getEmail());
        String userRole = userInfo.getRole();

        final String jwt = jwtService.generateToken(userDetails.getUsername());

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, userRole);

        return ResponseEntity.ok(authenticationResponse);
    }


    @GetMapping("/getAllUser")
    public String getAllUser(Model m){
        List<UserInfo>list=userInfoService.getAllUserInfo();
        m.addAttribute("userInfoList", list);
        return "index";
    }



    @GetMapping("/add_customer")
    public String showAddCustomerForm() {
        return "add_customer";
    }

    @PostMapping("/add")
    public String addUser(UserInfo user, Model model, HttpSession session) {
        userInfoService.addCustomer(user);
        session.setAttribute("msg", "Registration successful!");
        return "redirect:/api/getAllUser";
    }


    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        try {
            UserInfo userInfo = userInfoService.getUserById(id);
            model.addAttribute("user", userInfo);
            return "edit_customer";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }

    @PostMapping("/updateUserInfo")
    public String updateEmp(@ModelAttribute UserInfo userInfo, HttpSession session) {
        System.out.println("Received UserInfo: " + userInfo);

        UserInfo updateUserInfo = userInfoService.saveUserInfo(userInfo);

        if (updateUserInfo != null) {
            // System.out.println("save success");
            session.setAttribute("msg", "Update sucessfully");
        } else {
            // System.out.println("something wrong on server");
            session.setAttribute("msg", "something wrong on server");
        }

        return "redirect:/api/getAllUser";
    }



    @GetMapping("/deleteUser/{userId}")
    public String loadEmpSave(@PathVariable int userId, HttpSession session) {
        boolean f = userInfoService.deleteEmp(userId);
        if (f) {
            session.setAttribute("msg", "Delete successfully");
        } else {
            session.setAttribute("msg", "Something went wrong on the server");
        }
        return "redirect:/api/getAllUser";
    }



    @GetMapping("/list")
    public ResponseEntity<?> getPaginatedUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id") String sortBy,
                                               @RequestParam(required = false) String search) {
        try {
            // Get authenticated user's username

            // Call the service method to get paginated, sorted, and searched users
            List<UserInfo> users = userInfoService.getPaginatedUsers(page, size, sortBy, search);

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching users: " + e.getMessage());
        }
    }


 @GetMapping("/search")
public String searchUsers(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "id") String sortBy,
                          @RequestParam(required = false) String search,
                          Model model) {
    try {
        // Get paginated, sorted, and searched users
        List<UserInfo> users = userInfoService.getPaginatedUsers(page, size, sortBy, search);

        // Add the user list to the model
        model.addAttribute("userInfoList", users);

        return "index";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error fetching users: " + e.getMessage());
        return "error";
    }
}

}
