package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.RoomResponse;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.models.Room;
import com.techtricks.coe_auth.repositorys.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomResponse addRoom(String roomName) {
        Room room = new Room();
        room.setRoomName(roomName);
        Room savedRoom =  roomRepository.save(room);
        return new  RoomResponse(savedRoom.getRoomId() , savedRoom.getRoomName());
    }

    @Override
    public RoomResponse updateRoom(Long roomId , String roomName) throws RoomNotFoundExceptions {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if(optionalRoom.isEmpty()) {
            throw new RoomNotFoundExceptions("Room not found");
        }
        Room room = optionalRoom.get();
        room.setRoomName(roomName);
        Room savedRoom =  roomRepository.save(room);
        return new  RoomResponse(savedRoom.getRoomId() , savedRoom.getRoomName());
    }

    @Override
    public void removeRoom(Long roomId) throws RoomNotFoundExceptions {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if(optionalRoom.isEmpty()) {
            throw new RoomNotFoundExceptions("Room not found");
        }
        Room room = optionalRoom.get();
        roomRepository.delete(room);
    }

    @Override
    public Room findRoom(Long roomId) throws RoomNotFoundExceptions {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if(optionalRoom.isEmpty()) {
            throw new RoomNotFoundExceptions("Room not found");
        }
        return optionalRoom.get();
    }


}
