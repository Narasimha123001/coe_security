package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.RoomAccessResponseDto;
import com.techtricks.coe_auth.exceptions.NoAccessPresentException;
import com.techtricks.coe_auth.exceptions.RoomAccessAlreadyPresentException;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.services.RoomAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/room")
public class RoomAccessController {

    private final RoomAccessService roomAccessService;


    public RoomAccessController(RoomAccessService roomAccessService) {
        this.roomAccessService = roomAccessService;
    }

    @PostMapping("/assign/{RegisterNumber}/{roomId}")
    public ResponseEntity<?> assignRoomAccess(@PathVariable Long RegisterNumber ,@PathVariable Long roomId) {
        try{
            RoomAccessResponseDto roomAccess = roomAccessService.assignRoomAccess(RegisterNumber , roomId);
            return ResponseEntity.status(HttpStatus.CREATED).body(roomAccess);
        }catch (IllegalAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RoomNotFoundExceptions | RoomAccessAlreadyPresentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> findRoomAccess(@PathVariable Long roomId)  {
        System.out.println(roomId);
        try {
            RoomAccessResponseDto findRoomAccess = roomAccessService.findRoomAccess(roomId);
            return ResponseEntity.status(HttpStatus.FOUND).body(findRoomAccess);
            } catch (RoomNotFoundExceptions e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
    }

    @DeleteMapping("/{registerNumber}/{roomId}")
    public ResponseEntity<?> deleteRoomAccess( @PathVariable Long registerNumber , @PathVariable Long roomId ) {
        try{
            roomAccessService.removeRoomAccess(registerNumber, roomId);
            return ResponseEntity.ok("Access removed successfully");
        } catch (RoomNotFoundExceptions | NoAccessPresentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/access/{registerNumber}/{roomId}")
    public ResponseEntity<?> validateAccess(@PathVariable Long registerNumber, @PathVariable Long roomId) {
        try{
            boolean validate = roomAccessService.validateAccess(registerNumber, roomId);
            System.out.println(registerNumber +" "+ roomId + " "+ validate);
            if(validate){
                return ResponseEntity.ok("Access validated successfully");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Access validation failed");
            }
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
