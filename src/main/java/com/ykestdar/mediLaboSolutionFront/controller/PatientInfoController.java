package com.ykestdar.mediLaboSolutionFront.controller;

import com.ykestdar.mediLaboSolutionFront.DTOmodel.PatientInfo;
import com.ykestdar.mediLaboSolutionFront.DTOmodel.Prescription;
import com.ykestdar.mediLaboSolutionFront.service.PatientInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@Controller
@RequestMapping("patient")
public class PatientInfoController {
    private final PatientInfoService patientInfoService;

    public PatientInfoController(PatientInfoService patientInfoService) {
        this.patientInfoService = patientInfoService;
    }


    @GetMapping("/add")
    public String addPatient(Model model){
        model.addAttribute(new PatientInfo());
        return "new_patient";
    }



    @PostMapping("/add")
    public void addPatient(@ModelAttribute("patientInfo")PatientInfo patientInfo,Model model){
        model.addAttribute("patientInfo",patientInfoService.addPatient(patientInfo));
        System.out.println(patientInfo);
    }


    @GetMapping("/display")
    public String displayAllPatient(Model model){
        model.addAttribute("patients",patientInfoService.displayAllPatientInformation());
        return "patient";
    }

//    @GetMapping("/update/{id}")
//    public String updatePatient(Model model){
//        model.addAttribute(new PatientInfo());
//        return "edit_patient";
//    }



    @GetMapping("/update/{id}")
    public String updatePatient(@PathVariable int id, Model model){
        model.addAttribute("patientInfo",patientInfoService.getPatientById(id));
        return "edit_patient";
    }




//    @PostMapping("/update/{id}")
//    public PatientInfo updatePatient(@PathVariable("id")String id,Model model){
//        System.out.println();
//        System.out.println("return patient is :" + patientInfoService.updatePatient(id)); //id data type need to convert to Integer for test purpose it consider String
//        return patientInfoService.updatePatient(id);
//    }

    @PostMapping("/update")
    public PatientInfo updatePatient(@RequestParam("id")Integer id,
                                     @RequestParam("firstname")String firstname,
                                     @RequestParam("lastname")String lastname,
                                     @RequestParam("gender")String gender,
                                     @RequestParam("birthdate") LocalDate birthdate,
                                     @RequestParam("address")String address,
                                     @RequestParam("phoneNumber")String phoneNumber,Model model){
        System.out.println("come from page to ui controller for post patient/update is "+"id "+id+
        "firstname "+firstname+"lastname "+lastname+"gender "+gender+"birthdate "+birthdate+
        "address "+address+"phoneNumber "+phoneNumber);
//        System.out.println();
//        System.out.println("return patient is :" + patientInfoService.updatePatient(id)); //id data type need to convert to Integer for test purpose it consider String
        return patientInfoService.updatePatient(id,firstname,lastname,gender,birthdate,address,phoneNumber);
    }




    @GetMapping("/addPrescription/{id}")
    public String addPrescription(@PathVariable("id")Integer id,Model model){
        model.addAttribute(new Prescription());
//        model.addAttribute("prescription",patientInfoService.findPrescriptionById(id));
        model.addAttribute("currentPatientId",id);
        model.addAttribute("notes",patientInfoService.displayAllPrescription(id));
        model.addAttribute("riskLevel",patientInfoService.riskLevelCalculator(id));
        return "prescription";
    }



    @PostMapping("/addPrescription/{id}")
    public void addPrescription(@PathVariable("id")Integer id,
                                @ModelAttribute("note")String note){
//        prescription.setIssuedDate(LocalDateTime.now());
//        System.out.println("prescripion is " + prescription);
//        model.addAttribute("prescription",patientInfoService.findPrescriptionById(id));
        patientInfoService.addPrescription(id,note);
    }

  }
