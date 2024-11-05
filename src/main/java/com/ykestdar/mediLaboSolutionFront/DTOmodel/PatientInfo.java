package com.ykestdar.mediLaboSolutionFront.DTOmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientInfo {
    Integer id;
    String firstname;
    String lastname;
    LocalDate birthdate;
    String gender;
    String address;
    String phoneNumber;
}
