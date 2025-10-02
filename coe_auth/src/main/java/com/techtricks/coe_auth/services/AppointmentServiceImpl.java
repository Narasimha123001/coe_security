package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.exceptions.AppointmentException;
import com.techtricks.coe_auth.models.Appointment;
import com.techtricks.coe_auth.models.AppointmentsStatus;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final UserService userService;

    private final AppointmentRepository  appointmentRepository;

    public AppointmentServiceImpl(UserService userService, AppointmentRepository appointmentRepository) {
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment bookAppointment(Long registerNumber , String purpose) throws  AppointmentException {
        User existingUser = userService.getUserById(registerNumber);

        if(!canBookAppointment(registerNumber)){
            Appointment appointment =appointmentRepository
                    .findTopByUserRegisterNumberOrderByAppointmentDateTimeDesc(registerNumber)
                    .orElseThrow();
            LocalDateTime nextAvailable = appointment.getAppointmentDateTime().plusHours(12);
            throw new AppointmentException("you ca  n book appointment only after 12 hours :"+ nextAvailable);

        }
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentDateTime(LocalDateTime.now());
        newAppointment.setUser(existingUser);
        newAppointment.setPurpose(purpose);
        newAppointment.setStatus(AppointmentsStatus.PENDING);
        return appointmentRepository.save(newAppointment);
    }

    @Override
    public Boolean canBookAppointment(Long registerNumber) {
        Optional<Appointment> lastAppointment = appointmentRepository
                .findTopByUserRegisterNumberOrderByAppointmentDateTimeDesc(registerNumber);
        if(lastAppointment.isEmpty()){
            return true;
        }
        LocalDateTime lastTime = lastAppointment.get().getAppointmentDateTime();
        return lastTime.plusHours(12).isBefore(LocalDateTime.now());

    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAppointmentById(Long registerNumber) throws AppointmentException {
        User user = userService.getUserById(registerNumber);
        List<Appointment> appointments = appointmentRepository.findByUser_Id(user.getId());
        if(appointments.isEmpty()){
            throw new AppointmentException("not Appointment found");
        }
        return appointments;
    }



}
