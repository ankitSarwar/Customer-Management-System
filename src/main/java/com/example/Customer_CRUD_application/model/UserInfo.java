package com.example.Customer_CRUD_application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "First name is required")
//    @Column(name = "first_name")
    private String firstName;


//    @NotBlank(message = "Last name is required")
    private String lastName;

//    @NotBlank
    private String street;

//    @NotBlank(message = "Address is required")
//    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

//    @NotBlank(message = "City is required")
//    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

//    @NotBlank(message = "State is required")
//    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email address")
//    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;


    private String password;

//    @NotBlank
    private String phone;

//    @Enumerated(EnumType.STRING)
    private String role;

}
