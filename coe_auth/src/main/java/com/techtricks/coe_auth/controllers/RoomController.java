package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.RoomResponse;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService  roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add/{roomName}")
    public ResponseEntity<?> addRoom(@PathVariable String roomName) {
        try {
            RoomResponse room = roomService.addRoom(roomName);
            return ResponseEntity.status(HttpStatus.CREATED).body(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{roomId}/{roomName}")
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId ,@PathVariable String roomName) throws RoomNotFoundExceptions {
        try{
            RoomResponse room = roomService.updateRoom(roomId, roomName);
            return ResponseEntity.status(HttpStatus.CREATED).body(room);
        } catch (RoomNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
