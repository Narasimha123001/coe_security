package com.techtricks.coe_auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomResponse {
    private Long roomId;
    private String roomName;


}
