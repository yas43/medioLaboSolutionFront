package com.ykestdar.mediLaboSolutionFront.service;

import com.ykestdar.mediLaboSolutionFront.DTOmodel.PatientInfo;
import com.ykestdar.mediLaboSolutionFront.DTOmodel.Prescription;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PatientInfoService {

    private final RestTemplate restTemplate;

    public PatientInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PatientInfo> displayAllPatientInformation() {


         List<PatientInfo> list = restTemplate.getForObject("http://localhost:8080/patient_info/all",List.class);

         return list;


//        List<PatientInfo> list = new LinkedList<>();
//        PatientInfo patientInfo = new PatientInfo();
//        patientInfo.setId(1);
//        patientInfo.setFirstname("nameTest");
//        patientInfo.setLastname("lastnameTest");
//        patientInfo.setAddress("addressTest");
//        patientInfo.setBirthdate(LocalDate.now());
//        patientInfo.setGender("male");
//        patientInfo.setPhoneNumber("888888888");
//        list.add(patientInfo);
//        return list;
    }

    //id data type need to convert to Integer for test purpose it consider String
    public PatientInfo updatePatient(Integer id,String firstname,String lastname,String gender,
                                     LocalDate birthdate,String address,String phoneNumber) {

        PatientInfo patientInfo = new PatientInfo();

        int idNumber = id;
        String firstName = firstname;
        String lastName = lastname;
        String Gender = gender;
        LocalDate BirthDate = birthdate;
        String Address = address;
        String phone = phoneNumber;


        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/patient_info/update")
                .queryParam("id",idNumber)
                .queryParam("firstname",firstName)
                .queryParam("lastname",lastName)
                .queryParam("gender",Gender)
                .queryParam("birthdate",BirthDate)
                .queryParam("address",Address)
                .queryParam("phoneNumber",phone)
                .toUriString();

        patientInfo.setId(id);
        patientInfo.setFirstname(firstname);
        patientInfo.setLastname(lastname);
        patientInfo.setGender(gender);
        patientInfo.setAddress(address);
        patientInfo.setBirthdate(birthdate);
        patientInfo.setPhoneNumber(phoneNumber);

         PatientInfo patientInfo1 = restTemplate.postForObject(url,patientInfo,PatientInfo.class);

         return patientInfo1;


//        PatientInfo patientInfo = new PatientInfo();
//        patientInfo.setId(1);
//        patientInfo.setFirstname("testfirstname");
//        patientInfo.setLastname("testlastname");
//        patientInfo.setGender("female");
//        patientInfo.setPhoneNumber("77777777");
//        patientInfo.setBirthdate(LocalDate.now());
//        patientInfo.setAddress("address test");
//        patientInfo.setPhoneNumber("333333333");
//
//        return patientInfo;
    }

    public PatientInfo addPatient(PatientInfo patientInfo) {
        PatientInfo patientInfo1 = new PatientInfo();
        patientInfo1.setId(10);
        patientInfo1.setBirthdate(patientInfo.getBirthdate());
        patientInfo1.setGender(patientInfo.getGender());
        patientInfo1.setLastname(patientInfo.getLastname());
        patientInfo1.setFirstname(patientInfo.getFirstname());
        patientInfo1.setAddress(patientInfo.getAddress());
        patientInfo1.setPhoneNumber(patientInfo.getPhoneNumber());

        restTemplate.postForObject("http://localhost:8080/patient_info/add",patientInfo1,PatientInfo.class);
        PatientInfo patientInfo2 = restTemplate.getForObject("http://localhost:8080/patient_info/add",PatientInfo.class);

        return patientInfo2;

//        PatientInfo patientInfo1 = new PatientInfo();
//        patientInfo1.setId(1);
//        patientInfo1.setFirstname("testfirstname");
//        patientInfo1.setLastname("testlastname");
//        patientInfo1.setBirthdate(LocalDate.of(1980 ,12, 20));
//        patientInfo1.setAddress("test address");
//        patientInfo1.setPhoneNumber("testphone77777");
//        patientInfo1.setGender("male");
//        return patientInfo1;
    }

    public Prescription addPrescription(Integer id,Prescription prescription) {

        System.out.println("prescription id is "+prescription.getId());
        String url = "http://localhost:8086/prescription/addPrescription";
       Prescription prescription1 =  restTemplate.getForObject(url,Prescription.class);
//        Prescription prescription1 = new Prescription();
        return prescription1;
    }

    public List<String> displayAllPrescription(Integer id) {

        String url = "http://localhost:8086/prescription/prescriptions/{id}";

        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);

         List<String> allTheNotes =  restTemplate.getForObject(url,List.class,uriVariable);

         return allTheNotes;

//       List<Prescription> list = restTemplate.postForObject("http://localhost:8086/prescription/prescriptions",id,List.class);
//       return list;


//        List<Prescription> list = new LinkedList<>();
//
//        Prescription prescription = new Prescription();
//        prescription.setId(5);
//        prescription.setNote("prescription of a patient");
//        prescription.setIssuedDate(LocalDateTime.now());
//
//        Prescription prescription1 = new Prescription();
//        prescription1.setId(5);
//        prescription1.setNote("prescription of a patient");
//        prescription1.setIssuedDate(LocalDateTime.now());
//
//        Prescription prescription2 = new Prescription();
//        prescription2.setId(5);
//        prescription2.setNote("prescription of a patient");
//        prescription2.setIssuedDate(LocalDateTime.now());
//
//
//        list.add(prescription);
//        list.add(prescription1);
//        list.add(prescription2);
//        return list;
    }

    public String riskLevelCalculator(Integer id) {
        return "borderline";
    }

    public PatientInfo getPatientById(Integer id) {

        String url = "http://localhost:8080/patient_info/findById/{id}";

        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);

        PatientInfo patientInfo = restTemplate.getForObject(url,PatientInfo.class,uriVariable);

        return patientInfo;

//       PatientInfo patientInfo = restTemplate.getForObject("http://localhost:8085/patient_info/findById",PatientInfo.class);
//       return patientInfo;
    }

    public Prescription findPrescriptionById(Integer id) {
        String url = "http://localhost:8086/prescription/prescription/{id}";
        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);
        Prescription prescription = restTemplate.getForObject(url,Prescription.class,uriVariable);
        return prescription;

    }
}
