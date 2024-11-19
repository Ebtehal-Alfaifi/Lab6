package com.example.lab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employ {

    @NotEmpty(message = "you should enter ID")
    @Size(min = 3,max = 10,message = "length should be between 3 and 10")
    private String id;

    @NotEmpty(message = "you should enter name")
    @Size(min = 5,message = "your name at least should has 5 character")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only characters")
    private String name;

    @Email(message = " you should enter Valid email")
    private String email;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and contain 10 digits")//8 that i use.it for make sure if there is 8 number after 05
    private String phoneNumber;

    @NotNull(message = " you should enter your age ")
    @Positive(message = "you should enter positive number for age")
   @Digits(integer = 3,fraction = 0,message = "Age must be a valid numeric value") // integer it is mean the age has only 3 digits and dose not has any fraction
    @Min(26)
    private int age;


    @NotEmpty(message = " you should enter Position")

    @Pattern(regexp = "^(supervisor|coordinator)$",
            message = "Position must be either 'supervisor' or 'coordinator' only")
    private String position;

    @NotNull(message = " you should enter statues")
    @AssertFalse(message = "onLeave must always be false")
    private boolean onLeave;


    @NotNull(message = " you should enter hire date")
    @PastOrPresent(message = " Hire date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;



@NotNull(message = " you sholud enter Annual Leave")
@Positive(message = " you should enter positive number for Annual Leave ")
    private int annualLeave;







}
