package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.RoomAccessResponseDto;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.models.Role;
import com.techtricks.coe_auth.models.Room;
import com.techtricks.coe_auth.models.RoomAccess;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.RoomAccessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomAccessServiceImpl implements RoomAccessService {

    private final RoomAccessRepository roomAccessRepository;
    private final UserService userService;
    private final RoomService roomService;

    public RoomAccessServiceImpl(RoomAccessRepository roomAccessRepository,
                                 UserService userService,
                                 RoomService roomService) {
        this.roomAccessRepository = roomAccessRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

  //TODO -> these private are used for verification and validation purpose

    private RoomAccessResponseDto buildSuccessResponse(RoomAccess savedAccess) {

        return new  RoomAccessResponseDto(
                savedAccess.getRoom().getRoomName(),
                savedAccess.getUser().getUsername(),
                savedAccess.getUser().getRegisterNumber()
        );
    }

    private RoomAccess createRoomAccess(User user, Room room) {
        RoomAccess roomAccess = new RoomAccess();
        roomAccess.setRoom(room);
        roomAccess.setUser(user);
        return roomAccess;
    }

    private boolean hasExistingAccess(Long id, Long roomId) {
        return roomAccessRepository.existsByUserIdAndRoomRoomId(id , roomId);
    }

    private Room validateAndGetRoom(Long roomId) throws RoomNotFoundExceptions {
        Room room  = roomService.findRoom(roomId);
        if(room == null) {
            throw new RoomNotFoundExceptions("Room not found for room id: " + roomId);
        }
        return room;
    }

    private User validateAndGetUser(Long registerNumber) throws IllegalAccessException {
        User user = userService.getUserById(registerNumber);
        if(user == null) {
            throw new UserNotFoundException(
                    String.format("User with id %d not found", registerNumber)
            );
        }
        if(!isStaffMember(user)){
            throw new IllegalAccessException("Only staff members be assigned room access");
        }
        return user;
    }

    private boolean isStaffMember(User user) {
        return user.getRole() !=null  && user.getRole() == Role.STAFF;
    }

    @Override
    public RoomAccessResponseDto findRoomAccess(Long roomId) throws RoomNotFoundExceptions {
        Optional<RoomAccess> roomAccess = roomAccessRepository.findRoomAccessByRoom_RoomId(roomId);
        if (roomAccess.isEmpty()) {
            throw new RoomNotFoundExceptions("Room access not found for room id: " + roomId);
        }
        User user = roomAccess.get().getUser();
        return new RoomAccessResponseDto(
                roomAccess.get().getRoom().getRoomName(),
                user.getUsername(),
                user.getRegisterNumber()
        );
    }

    @Override
    public RoomAccessResponseDto assignRoomAccess(Long RegisterNumber,
                                                  Long roomId) throws IllegalAccessException, RoomNotFoundExceptions, UserNotFoundException {
        User user = validateAndGetUser(RegisterNumber);
        Room room = validateAndGetRoom(roomId);
        if(hasExistingAccess(user.getId() , roomId)){
            return  new RoomAccessResponseDto();
        }
        RoomAccess roomAccess = createRoomAccess(user , room);
        RoomAccess savedAccess = roomAccessRepository.save(roomAccess);
        return buildSuccessResponse(savedAccess);
    }



    @Override
    @Transactional
    public void removeRoomAccess(Long registerNumber, Long roomId)
            throws UserNotFoundException, RoomNotFoundExceptions, IllegalAccessException {
        User user = validateAndGetUser(registerNumber);
        Room room = validateAndGetRoom(roomId);
        if (hasExistingAccess(user.getId(), room.getRoomId())) {
            roomAccessRepository.deleteRoomAccess(user.getId(), roomId);
        }
    }

    @Override
    public List<RoomAccessResponseDto> findAll() {
        return mapToDtoList(roomAccessRepository.findAll());
    }

    @Override
    public List<RoomAccessResponseDto> getAccessByRegNo(Long registerNumber) throws UserNotFoundException, IllegalAccessException {
        User user = validateAndGetUser(registerNumber);
        return mapToDtoList(roomAccessRepository.findByUser_id(user.getRegisterNumber()));
    }

    @Override
    public boolean validateAccess(Long RegisterNumber ,Long roomId) throws UserNotFoundException, IllegalAccessException {
        User user = validateAndGetUser(RegisterNumber);
        return roomAccessRepository.existsByUserIdAndRoomRoomId(user.getId(), roomId);
    }

    private List<RoomAccessResponseDto> mapToDtoList(List<RoomAccess> roomAccessList) {
        return roomAccessList.stream()
                .map(room -> new RoomAccessResponseDto(
                        room.getRoom().getRoomName(),
                        room.getUser().getUsername(),
                        room.getUser().getRegisterNumber()
                ))
                .collect(Collectors.toList());
    }
}
