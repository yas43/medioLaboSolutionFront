package com.ykestdar.mediLaboSolutionFront.service;

import com.ykestdar.mediLaboSolutionFront.model.PatientInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientInfoService {
    public List<PatientInfo> displayAllPatientInformation() {
        List<PatientInfo> list = new LinkedList<>();
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(1);
        patientInfo.setFirstname("nameTest");
        patientInfo.setLastname("lastnameTest");
        patientInfo.setAddress("addressTest");
        patientInfo.setBirthdate(LocalDate.now());
        patientInfo.setGender("male");
        patientInfo.setPhoneNumber("888888888");
        list.add(patientInfo);
        return list;
    }


    public PatientInfo updatePatient(Integer id) {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(1);
        patientInfo.setFirstname("testfirstname");
        patientInfo.setLastname("testlastname");
        patientInfo.setGender("female");
        patientInfo.setPhoneNumber("77777777");
        patientInfo.setBirthdate(LocalDate.now());
        patientInfo.setAddress("address test");
        patientInfo.setPhoneNumber("333333333");

        return patientInfo;
    }
}
