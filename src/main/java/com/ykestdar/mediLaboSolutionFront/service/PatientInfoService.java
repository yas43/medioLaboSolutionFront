package com.ykestdar.mediLaboSolutionFront.service;

import com.ykestdar.mediLaboSolutionFront.DTOmodel.PatientInfo;
import com.ykestdar.mediLaboSolutionFront.DTOmodel.Prescription;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PatientInfoService {
    @Value("${service.url.patientInfoBase}")
    private String patientInfoUrlBase;
    @Value("${service.url.patientAnalyseBase}")
    private String patientAnalyseUrlBase;
    @Value("${service.url.patientPrescriptionBase}")
    private String patientPrescriptionUrlBase;

    private final RestTemplate restTemplate;

    public PatientInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PatientInfo> displayAllPatientInformation() {

        String url = String.format("%s/all",patientInfoUrlBase);
         List<PatientInfo> list = restTemplate.getForObject(url,List.class);

         return list;


    }


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

        String updateUrl = String.format("%s/update",patientInfoUrlBase);
        String url = UriComponentsBuilder.fromHttpUrl(updateUrl)
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


    }

    public PatientInfo addPatient(PatientInfo patientInfo) {
        String addUrl = String.format("%s/add",patientInfoUrlBase);
        PatientInfo patientInfo1 = new PatientInfo();
        patientInfo1.setBirthdate(patientInfo.getBirthdate());
        patientInfo1.setGender(patientInfo.getGender());
        patientInfo1.setLastname(patientInfo.getLastname());
        patientInfo1.setFirstname(patientInfo.getFirstname());
        patientInfo1.setAddress(patientInfo.getAddress());
        patientInfo1.setPhoneNumber(patientInfo.getPhoneNumber());

      return   restTemplate.postForObject(addUrl,patientInfo,PatientInfo.class);

    }

    public void addPrescription(Integer id,String note) {
        String addPrescriptionUrl = String.format("%s/addPrescription/%d",patientPrescriptionUrlBase,id);

//        System.out.println("prescription id is "+prescription.getId());
        String url = addPrescriptionUrl;

        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);


                Prescription prescription12 = new Prescription();
                prescription12.setId(id);
                if (prescription12.getNote()==null){
                    prescription12.setNote(new ArrayList<>());
                }
                prescription12.getNote().add(note);

         restTemplate.postForObject(url,prescription12,Prescription.class,uriVariable);



    }

    public List<String> displayAllPrescription(Integer id) {
        System.out.println("before creating url");
        String displayUrl = String.format("%s/prescriptions/%d",patientPrescriptionUrlBase,id);
        System.out.println("display url is "+displayUrl);

        String url = "http://localhost:8080/prescription/prescriptions/{id}";

        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);

         List<String> allTheNotes =  restTemplate.getForObject(displayUrl,List.class,uriVariable);

         return allTheNotes;


    }

    public String riskLevelCalculator(Integer id) {

        String riskLevelUrl = String.format("%s/score/%d",patientAnalyseUrlBase,id);
        System.out.println("link for prescription is "+riskLevelUrl);
        String url = "http://localhost:8080/analyse/score/{id}";
        Map<String,Object>uriVariable = new HashMap<>();
        uriVariable.put("id",id);

        Integer score = restTemplate.getForObject(riskLevelUrl,Integer.class,uriVariable);

        return String.valueOf(score);


    }

    public PatientInfo getPatientById(Integer id) {

        String findPatientUrl = String.format("%s/findById/%d",patientInfoUrlBase,id);

        String url = findPatientUrl;

        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);

        PatientInfo patientInfo = restTemplate.getForObject(url,PatientInfo.class,uriVariable);

        return patientInfo;

//       PatientInfo patientInfo = restTemplate.getForObject("http://localhost:8085/patient_info/findById",PatientInfo.class);

    }

    public Prescription findPrescriptionById(Integer id) {

        String findPrescriptionUrl = String.format("%s/prescription/%d",patientPrescriptionUrlBase,id);
        String url = findPrescriptionUrl;
        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);
        Prescription prescription = restTemplate.getForObject(url,Prescription.class,uriVariable);
        return prescription;

    }

    private String resolveSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
