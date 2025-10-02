package com.techtricks.coe_auth.services;
import com.techtricks.coe_auth.exceptions.AppointmentException;
import com.techtricks.coe_auth.models.Appointment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService {

    Appointment bookAppointment(Long registerNumber, String purpose) throws AppointmentException;

    Boolean canBookAppointment(Long registerNumber);

    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentById(Long registerNumber) throws AppointmentException;

}
