package com.techtricks.coe_auth.modelMappers;

import com.techtricks.coe_auth.dtos.AppointmentResponseDto;
import com.techtricks.coe_auth.models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentMapper {

    public static AppointmentResponseDto toDto(Appointment appointment) {
        return  new AppointmentResponseDto(
                appointment.getId(),
                appointment.getAppointmentDateTime(),
                appointment.getPurpose(),
                appointment.getUser().getRegisterNumber(),
                appointment.getStatus()
        );
    }

    public static List<AppointmentResponseDto> toDtoList(List<Appointment> appointments) {
        List<AppointmentResponseDto> appointmentResponseDtoList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentResponseDtoList.add(toDto(appointment));
        }
        return appointmentResponseDtoList;
    }
}
