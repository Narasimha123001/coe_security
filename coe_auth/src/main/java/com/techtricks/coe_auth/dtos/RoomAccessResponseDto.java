package com.techtricks.coe_auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomAccessResponseDto {

    private String RoomName;
    private String UserName;
    private Long RegisterNumber;

    public RoomAccessResponseDto(String roomName, String username) {
        this.RoomName = roomName;
        this.UserName = username;
    }
}
