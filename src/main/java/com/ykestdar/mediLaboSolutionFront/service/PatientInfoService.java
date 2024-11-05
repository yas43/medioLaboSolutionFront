package com.ykestdar.mediLaboSolutionFront.service;

import com.ykestdar.mediLaboSolutionFront.DTOmodel.PatientInfo;
import com.ykestdar.mediLaboSolutionFront.DTOmodel.Prescription;
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

    //id data type need to convert to Integer for test purpose it consider String
    public PatientInfo updatePatient(String id) {
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

    public PatientInfo addPatient(PatientInfo patientInfo) {
        PatientInfo patientInfo1 = new PatientInfo();
        patientInfo1.setId(1);
        patientInfo1.setFirstname("testfirstname");
        patientInfo1.setLastname("testlastname");
        patientInfo1.setBirthdate(LocalDate.of(1980 ,12, 20));
        patientInfo1.setAddress("test address");
        patientInfo1.setPhoneNumber("testphone77777");
        patientInfo1.setGender("male");
        return patientInfo1;
    }

    public Prescription addPrescription() {
        Prescription prescription = new Prescription();
        return prescription;
    }
}
