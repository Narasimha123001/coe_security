package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.AppointmentDto;
import com.techtricks.coe_auth.modelMappers.AppointmentMapper;
import com.techtricks.coe_auth.dtos.AppointmentResponseDto;
import com.techtricks.coe_auth.exceptions.AppointmentException;
import com.techtricks.coe_auth.models.Appointment;
import com.techtricks.coe_auth.services.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final  AppointmentService appointmentService;

    public AppointmentController( AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @PostMapping("/book/{registrationNumber}")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @PathVariable Long registrationNumber ,
            @RequestBody AppointmentDto appointmentDto) throws AppointmentException {
        Appointment appointment = appointmentService.bookAppointment(registrationNumber , appointmentDto.getPurpose());
        AppointmentResponseDto responseDto = AppointmentMapper.toDto(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
          List<Appointment> appointment = appointmentService.getAllAppointments();
          List<AppointmentResponseDto> appoimentList = AppointmentMapper.toDtoList(appointment);
          return ResponseEntity.status(HttpStatus.OK).body(appoimentList);
    }

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentById(
            @PathVariable Long registrationNumber) throws AppointmentException {
        List<Appointment> appointment = appointmentService.getAppointmentById(registrationNumber);
        List<AppointmentResponseDto> appoimentList = AppointmentMapper.toDtoList(appointment);
        return ResponseEntity.status(HttpStatus.OK).body(appoimentList);
    }


}
