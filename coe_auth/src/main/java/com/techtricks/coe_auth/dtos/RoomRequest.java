package com.techtricks.coe_auth.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomRequest {

    private Long roomId;
    private String roomName;
}
