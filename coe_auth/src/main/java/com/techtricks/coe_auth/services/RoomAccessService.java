package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.RoomAccessResponseDto;
import com.techtricks.coe_auth.exceptions.NoAccessPresentException;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.models.Room;
import com.techtricks.coe_auth.models.RoomAccess;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomAccessService {


    RoomAccessResponseDto assignRoomAccess(Long RegisterNumber , Long roomId) throws IllegalAccessException, RoomNotFoundExceptions;
    //ToDo -> Optional<RoomAccess> findByUserId(Long id);
    RoomAccessResponseDto findRoomAccess(Long roomId) throws RoomNotFoundExceptions;



//
      public void removeRoomAccess(Long RegisterNumber , Long roomId) throws NoAccessPresentException, RoomNotFoundExceptions;

    public List<RoomAccessResponseDto> findAll();
//
     public List<RoomAccessResponseDto> getAccessByRegNo(Long RoomAccessId);
//

    boolean validateAccess(Long RegisterNumber , Long roomId);
}

