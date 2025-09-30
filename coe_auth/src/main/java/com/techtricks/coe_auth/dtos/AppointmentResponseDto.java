package com.techtricks.coe_auth.dtos;

import com.techtricks.coe_auth.models.AppointmentsStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentResponseDto {

    private Long id;
    private LocalDateTime appointmentDateAndTime;
    private String purpose;
    private Long registrationNumber;
    private AppointmentsStatus status;
}
