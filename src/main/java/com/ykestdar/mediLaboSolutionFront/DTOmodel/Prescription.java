package com.ykestdar.mediLaboSolutionFront.DTOmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    Integer id;
   List<String> note;

}
