package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.RoomAccessResponseDto;
import com.techtricks.coe_auth.exceptions.NoAccessPresentException;
import com.techtricks.coe_auth.exceptions.RoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
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

    @Override
    public RoomAccessResponseDto assignRoomAccess(Long registerNumber, Long roomId)
            throws IllegalAccessException, RoomNotFoundExceptions, UserNotFoundException {

        User user = userService.getUserById(registerNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with register number: " + registerNumber);
        }

        // check role — adjust Role enum or comparison to your codebase
        if (!user.getRole().equals(com.techtricks.coe_auth.models.Role.STAFF)) {
            throw new IllegalAccessException("Only staff can have the access");
        }

        Room room = roomService.findRoom(roomId);
        if (room == null) {
            throw new RoomNotFoundExceptions("Room not found with id: " + roomId);
        }

        RoomAccess roomAccess = new RoomAccess();
        roomAccess.setRoom(room);
        roomAccess.setUser(user);

        RoomAccess saved = roomAccessRepository.save(roomAccess);

        return new RoomAccessResponseDto(
                saved.getRoom().getRoomName(),
                saved.getUser().getUsername(),
                saved.getUser().getRegisterNumber()
        );
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
    @Transactional
    public void removeRoomAccess(Long registerNumber, Long roomId)
            throws UserNotFoundException, RoomNotFoundExceptions, NoAccessPresentException {

        User user = userService.getUserById(registerNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with register number: " + registerNumber);
        }

        Room room = roomService.findRoom(roomId);
        if (room == null) {
            throw new RoomNotFoundExceptions("Room not found with id: " + roomId);
        }

        Optional<RoomAccess> optionalRoomAccess =
                roomAccessRepository.findByUserIdAndRoomRoomId(user.getId(), roomId);

        if (optionalRoomAccess.isEmpty()) {
            throw new NoAccessPresentException("No access presented by user id: " + user.getId() +
                    " to room id: " + room.getRoomId());
        }

        // delete by repository method (assumes method exists)
        roomAccessRepository.deleteRoomAccess(user.getId(), roomId);
    }

    @Override
    public List<RoomAccessResponseDto> findAll() {
        return mapToDtoList(roomAccessRepository.findAll());
    }

    @Override
    public List<RoomAccessResponseDto> getAccessByRegNo(Long registerNumber) throws UserNotFoundException {
        User user = userService.getUserById(registerNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with register number: " + registerNumber);
        }
        return mapToDtoList(roomAccessRepository.findByUser_id(registerNumber));
    }

    @Override
    public boolean validateAccess(Long RegisterNumber ,Long roomId) throws UserNotFoundException {
        User user = userService.getUserById(RegisterNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with register number: " + RegisterNumber);
        }
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
