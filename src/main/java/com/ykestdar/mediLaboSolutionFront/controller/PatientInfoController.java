package com.ykestdar.mediLaboSolutionFront.controller;

import com.ykestdar.mediLaboSolutionFront.DTOmodel.*;
import com.ykestdar.mediLaboSolutionFront.service.PatientInfoService;
import com.ykestdar.mediLaboSolutionFront.session.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.servlet.mvc.support.*;
import org.springframework.web.util.*;

import java.io.*;
import java.time.*;
import java.util.*;

@Controller
@RequestMapping("patient")
public class PatientInfoController {
    @Value("${service.url.patientAuthorizationBase}")
    private String authenticateUrlBase;
    @Value("${service.url.patientInfoBase}")
    private String patientInfoUrlBase;
    @Value("${service.url.patientPrescriptionBase}")
    private String patientPrescriptionUrlBase;
    @Value("${service.url.patientAnalyseBase}")
    private String patientAnalyseUrlBase;
    private final PatientInfoService patientInfoService;
    private final RestTemplate restTemplate;
    private final JwtSessionStore jwtSessionStore;

    public PatientInfoController(PatientInfoService patientInfoService, RestTemplate restTemplate, JwtSessionStore jwtSessionStore) {
        this.patientInfoService = patientInfoService;
        this.restTemplate = restTemplate;
        this.jwtSessionStore = jwtSessionStore;
    }


    @GetMapping("/signUp")
    public String displayLogin(Model model){

        return "login";
    }





    @PostMapping("login")
    public String login(@ModelAttribute LoginForm loginForm, HttpServletResponse response) throws IOException {


        System.out.println("we are in massages/login");

        try {
            String authenticationUrl = String.format("%s/authenticate",authenticateUrlBase);
            String url = "http://localhost:8082/login/authenticate";

            LoginForm loginForm1 = new LoginForm();
            loginForm1.setUsername(loginForm.getUsername());
            loginForm1.setPassword(loginForm.getPassword());



            ResponseEntity<String> responseToken = restTemplate.postForEntity(authenticationUrl, loginForm1, String.class);
            String token = responseToken.getBody();
            System.out.println("received token in frontend is "+token);
//    System.out.println("user name when token generated is " + jwtService.getUsername(token));

            //generate a session ID
            String sessionId = UUID.randomUUID().toString();
            System.out.println("sessionid is "+sessionId);

            // Store the session ID and JWT in the session store
            jwtSessionStore.storeToken(sessionId, token);


            // Send the session ID as a cookie to the client
            response.setHeader("Set-Cookie", "SESSIONID=" + sessionId + "; HttpOnly; Secure; Path=/");

            System.out.println("response header is " + response.getWriter());


//            return ResponseEntity.ok().build();
            return "home";
        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credential");
            return "redirect:/patient/login";
        }



    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }



    @GetMapping("/add")
    public String addPatient(Model model){
        model.addAttribute(new PatientInfo());
        return "new_patient";
    }



    @PostMapping("/add")
    public String addPatient(@ModelAttribute("patientInfo")PatientInfo patientInfo,RedirectAttributes redirectAttributes,Model model,HttpServletResponse response){
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );



            String addUrl = String.format("%s/add",patientInfoUrlBase);

            PatientInfo patientInfo1 = new PatientInfo();
            patientInfo1.setBirthdate(patientInfo.getBirthdate());
            patientInfo1.setGender(patientInfo.getGender());
            patientInfo1.setLastname(patientInfo.getLastname());
            patientInfo1.setFirstname(patientInfo.getFirstname());
            patientInfo1.setAddress(patientInfo.getAddress());
            patientInfo1.setPhoneNumber(patientInfo.getPhoneNumber());

            HttpEntity<PatientInfo> entity = new HttpEntity<>(patientInfo1,headers);

            ResponseEntity<PatientInfo> patientInfoResponseEntity= restTemplate.exchange(
                    addUrl,
                    HttpMethod.POST,
                    entity,
                    PatientInfo.class);



            String url = String.format("%s/all",patientInfoUrlBase);
            HttpEntity<List<PatientInfo>> listHttpEntity = new HttpEntity<>(headers);

            ResponseEntity<List<PatientInfo>> listResponseEntity= restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    listHttpEntity, new ParameterizedTypeReference<List<PatientInfo>>() {});

            model.addAttribute("patientInfo",patientInfoResponseEntity.getBody() );
//            model.addAttribute("patientInfo", patientInfoService.addPatient(patientInfo));
            redirectAttributes.addFlashAttribute("success","patient information added successfully");
            model.addAttribute("patients",listResponseEntity.getBody());
//            model.addAttribute("patients",patientInfoService.displayAllPatientInformation());
//            return "patient";
//            System.out.println(patientInfo);
            return "redirect:/patient/display";
        }catch (Exception e){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = String.format("%s/all",patientInfoUrlBase);

            ResponseEntity<List<PatientInfo>> list= restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, new ParameterizedTypeReference<List<PatientInfo>>() {});

            redirectAttributes.addFlashAttribute("error","patient not added try again");
            model.addAttribute("patients",list.getBody());
//            model.addAttribute("patients",patientInfoService.displayAllPatientInformation());
//            return "patient";
            return "redirect:/patient/add";
        }
    }


    @GetMapping("/display")
    public String displayAllPatient(Model model,HttpServletResponse response){
        System.out.println("inside front display method and response.header is "+response.getHeader(HttpHeaders.AUTHORIZATION));
        String url = String.format("%s/all",patientInfoUrlBase);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );
        HttpEntity<String>entity = new HttpEntity<>(headers);
        ResponseEntity<List<PatientInfo>> list= restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PatientInfo>>() {});
        System.out.println("inside front end display method list is "+list);
        model.addAttribute("patients",list.getBody());
//        model.addAttribute("patients",patientInfoService.displayAllPatientInformation());
        return "patient";
    }

//    @GetMapping("/update/{id}")
//    public String updatePatient(Model model){
//        model.addAttribute(new PatientInfo());
//        return "edit_patient";
//    }



    @GetMapping("/update/{id}")
    public String updatePatient(@PathVariable int id, Model model,HttpServletResponse response){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );

        String findPatientUrl = String.format("%s/findById/%d",patientInfoUrlBase,id);
        Map<String,Object> uriVariable = new HashMap<>();
        uriVariable.put("id",id);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PatientInfo> patientInfoResponseEntity = restTemplate.exchange(
                findPatientUrl,
                HttpMethod.GET,
                entity,
                PatientInfo.class,
                uriVariable);

        model.addAttribute("patientInfo",patientInfoResponseEntity.getBody());
//        model.addAttribute("patientInfo",patientInfoService.getPatientById(id));
        return "edit_patient";
    }





    @PostMapping("/update")
    public String updatePatient(@RequestParam("id")Integer id,
                                     @RequestParam("firstname")String firstname,
                                     @RequestParam("lastname")String lastname,
                                     @RequestParam("gender")String gender,
                                     @RequestParam("birthdate") LocalDate birthdate,
                                     @RequestParam("address")String address,
                                     @RequestParam("phoneNumber")String phoneNumber,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                HttpServletResponse response){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );

        String updateUrl = String.format("%s/update",patientInfoUrlBase);
        String url = UriComponentsBuilder.fromHttpUrl(updateUrl)
                .queryParam("id",id)
                .queryParam("firstname",firstname)
                .queryParam("lastname",lastname)
                .queryParam("gender",gender)
                .queryParam("birthdate",birthdate)
                .queryParam("address",address)
                .queryParam("phoneNumber",phoneNumber)
                .toUriString();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<PatientInfo> patientInfoResponseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    PatientInfo.class
                    );
            patientInfoResponseEntity.getBody();
//             patientInfoService.updatePatient(id, firstname, lastname, gender, birthdate, address, phoneNumber);
            redirectAttributes.addFlashAttribute("success","patient information updated successfully");
            return "redirect:/patient/display";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","user information not updated please try again");

            String findPatientUrl = String.format("%s/findById/%d",patientInfoUrlBase,id);
            Map<String,Object> uriVariable = new HashMap<>();
            uriVariable.put("id",id);

            HttpEntity<String> entity1 = new HttpEntity<>(headers);
            ResponseEntity<PatientInfo> patientInfoResponseEntity = restTemplate.exchange(
                    findPatientUrl,
                    HttpMethod.GET,
                    entity1,
                    PatientInfo.class,
                    uriVariable);

            model.addAttribute("patientInfo",patientInfoResponseEntity.getBody());
//            model.addAttribute("patientInfo",patientInfoService.getPatientById(id));
            return "edit_patient";
        }
    }




    @GetMapping("/addPrescription/{id}")
    public String addPrescription(@PathVariable("id")Integer id,Model model,HttpServletResponse response){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );



        HttpEntity<String> entity = new HttpEntity<>(headers);


//        ResponseEntity<Prescription> findPrescriptionByIdEntity = restTemplate.exchange(
//                displayUrl,
//                HttpMethod.GET,
//                entity,
//                Prescription.class
//        );



        model.addAttribute(new Prescription());
//        model.addAttribute("prescription",patientInfoService.findPrescriptionById(id));
        model.addAttribute("currentPatientId",id);


        String prescriptionUrl = String.format("%s/prescriptions/%d",patientPrescriptionUrlBase,id);
        Map<String,Object> uriVariableForPrescriptions = new HashMap<>();
        uriVariableForPrescriptions.put("id",id);


        ResponseEntity<List<String>> displayPrescriptionEntity = restTemplate.exchange(
                prescriptionUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<String>>() {},
                uriVariableForPrescriptions
        );



        model.addAttribute("notes",displayPrescriptionEntity.getBody());
//        model.addAttribute("notes",patientInfoService.displayAllPrescription(id));


        String riskLevelUrl = String.format("%s/score/%d",patientAnalyseUrlBase,id);
        Map<String,Object> uriVariableForRiskLevel = new HashMap<>();
        uriVariableForRiskLevel.put("id",id);


        ResponseEntity<String> riskLevelEntity = restTemplate.exchange(
                riskLevelUrl,
                HttpMethod.GET,
                entity,
                String.class,
                uriVariableForRiskLevel);


        model.addAttribute("riskLevel",riskLevelEntity.getBody());
//        model.addAttribute("riskLevel",patientInfoService.riskLevelCalculator(id));
        return "prescription";
    }



    @PostMapping("/addPrescription/{id}")
    public String addPrescription(@PathVariable("id")Integer id,
                                @ModelAttribute("note")String note,
                                  RedirectAttributes redirectAttributes,
                                  Model model,HttpServletResponse response){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +response.getHeader(HttpHeaders.AUTHORIZATION).substring(7) );



        String addPrescriptionUrl = String.format("%s/addPrescription/%d",patientPrescriptionUrlBase,id);
        Map<String,Object> prescriptionUriVariable = new HashMap<>();
        prescriptionUriVariable.put("id",id);

        Prescription prescription = new Prescription();
        prescription.setId(id);
        if (prescription.getNote()==null){
            prescription.setNote(new ArrayList<>());
        }
        prescription.getNote().add(note);


        HttpEntity<Prescription> entity = new HttpEntity<>(prescription, headers);

        try {


            ResponseEntity<Prescription> prescriptionResponseEntity = restTemplate.exchange(
                    addPrescriptionUrl,
                    HttpMethod.POST,
                    entity,
                    Prescription.class);

            prescriptionResponseEntity.getBody();

//            patientInfoService.addPrescription(id, note);
            redirectAttributes.addFlashAttribute("success","prescription added successfully");
//            model.addAttribute("currentPatientId",id);
//            return "prescription";
            model.addAttribute("currentPatientId",id);

            String prescriptionsUrl = String.format("%s/prescriptions/%d",patientPrescriptionUrlBase,id);
            HttpEntity<String> allPrescriptionEntity = new HttpEntity<>(headers);

            ResponseEntity<List<String>> allPrecriptionResponseEntity = restTemplate.exchange(
                    prescriptionsUrl,
                    HttpMethod.GET,
                    allPrescriptionEntity,
                    new ParameterizedTypeReference<List<String>>() {});

            model.addAttribute("notes",allPrecriptionResponseEntity.getBody());
//            model.addAttribute("notes",patientInfoService.displayAllPrescription(id));



            String riskLevelUrl = String.format("%s/score/%d",patientAnalyseUrlBase,id);

            ResponseEntity<Integer> riskLevelResponseEntity = restTemplate.exchange(
                    riskLevelUrl,
                    HttpMethod.GET,
                    allPrescriptionEntity,
                    Integer.class);

            model.addAttribute("riskLevel",riskLevelResponseEntity.getBody());
//            model.addAttribute("riskLevel",patientInfoService.riskLevelCalculator(id));
            return "prescription";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","prescription not added try again");
//            model.addAttribute("currentPatientId",id);
//            return "prescription";
            model.addAttribute("currentPatientId",id);


            String prescriptionsUrl = String.format("%s/prescriptions/%d",patientPrescriptionUrlBase,id);
            HttpEntity<String> allPrescriptionEntity = new HttpEntity<>(headers);

            ResponseEntity<List<String>> allPrecriptionResponseEntity = restTemplate.exchange(
                    prescriptionsUrl,
                    HttpMethod.GET,
                    allPrescriptionEntity,
                    new ParameterizedTypeReference<List<String>>() {});

            model.addAttribute("notes",allPrecriptionResponseEntity.getBody());

//            model.addAttribute("notes",patientInfoService.displayAllPrescription(id));


            String riskLevelUrl = String.format("%s/score/%d",patientAnalyseUrlBase,id);

            ResponseEntity<Integer> riskLevelResponseEntity = restTemplate.exchange(
                    riskLevelUrl,
                    HttpMethod.GET,
                    allPrescriptionEntity,
                    Integer.class);

            model.addAttribute("riskLevel",riskLevelResponseEntity.getBody());

//            model.addAttribute("riskLevel",patientInfoService.riskLevelCalculator(id));
            return "prescription";
        }
    }



  }
