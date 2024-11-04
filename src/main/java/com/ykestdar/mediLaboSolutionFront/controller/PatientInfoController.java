package com.ykestdar.mediLaboSolutionFront.controller;

import com.ykestdar.mediLaboSolutionFront.service.PatientInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("patient")
public class PatientInfoController {
    private final PatientInfoService patientInfoService;

    public PatientInfoController(PatientInfoService patientInfoService) {
        this.patientInfoService = patientInfoService;
    }

    @GetMapping("/display")
    public String displayAllPatient(Model model){
        model.addAttribute("patients",patientInfoService.displayAllPatientInformation());
        return "patient";
    }

    @GetMapping("/update/{id}")
    public String updatePatient(@PathVariable("id")Integer id, Model model){
        model.addAttribute("patientInfo",patientInfoService.updatePatient(id));
        return "edit_patient";
    }

}
