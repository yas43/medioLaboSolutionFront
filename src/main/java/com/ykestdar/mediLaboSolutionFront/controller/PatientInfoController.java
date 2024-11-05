package com.ykestdar.mediLaboSolutionFront.controller;

import com.ykestdar.mediLaboSolutionFront.DTOmodel.PatientInfo;
import com.ykestdar.mediLaboSolutionFront.DTOmodel.Prescription;
import com.ykestdar.mediLaboSolutionFront.service.PatientInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping("/update/{id}")
    public String updatePatient(Model model){
        model.addAttribute(new PatientInfo());
        return "edit_patient";
    }

    @PostMapping("/update/{id}")
    public PatientInfo updatePatient(@PathVariable("id")String id,Model model){
        System.out.println();
        System.out.println("return patient is :" + patientInfoService.updatePatient(id)); //id data type need to convert to Integer for test purpose it consider String
        return patientInfoService.updatePatient(id);
    }



    @GetMapping("/addPrescription/{id}")
    public String addPrescription(@PathVariable("id")String id,Model model){
        model.addAttribute(new Prescription());
        model.addAttribute("prescriptions",patientInfoService.displayAllPrescription());
        model.addAttribute("riskLevel",patientInfoService.riskLevelCalculator(id));
        return "prescription";
    }



    @PostMapping("/addPrescription/{id}")
    public Prescription addPrescription(@ModelAttribute("prescription")Prescription prescription){
        prescription.setIssuedDate(LocalDateTime.now());
        System.out.println("prescripion is " + prescription);
       return patientInfoService.addPrescription();
    }

  }
