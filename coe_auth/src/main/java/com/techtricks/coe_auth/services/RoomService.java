package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.RoomResponse;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.models.Room;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {

    public RoomResponse addRoom(String roomName);

    public RoomResponse updateRoom(Long roomId , String roomName) throws RoomNotFoundExceptions;

    public void removeRoom(Long roomId) throws RoomNotFoundExceptions;

    public Room findRoom(Long roomId) throws RoomNotFoundExceptions;


}
