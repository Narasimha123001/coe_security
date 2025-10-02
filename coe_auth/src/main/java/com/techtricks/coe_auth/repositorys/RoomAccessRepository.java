package com.techtricks.coe_auth.repositorys;

import com.techtricks.coe_auth.models.RoomAccess;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomAccessRepository extends JpaRepository<RoomAccess , Long> {

 //   Optional<RoomAccess> findRoomAccessByRoomId(Long roomId);
      Optional<RoomAccess> findRoomAccessByRoom_RoomId(Long roomId);
//
//    List<RoomAccess> findByRoomName(String roomName);
//
    List<RoomAccess> findByUser_id(Long registerNumber);
    Optional<RoomAccess> findByUserIdAndRoomRoomId(Long userId, Long roomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RoomAccess ra WHERE ra.user.id = :userId AND ra.room.roomId = :roomId")
    void deleteRoomAccess(@Param("userId")  Long userId, @Param("roomId") Long roomId);


//    @Query("SELECT EXISTS (SELECT 1 FROM RoomAccess ra WHERE ra.user.id = :userId  AND ra.room.roomId =: roomId)")
//    boolean checkRoomAccessExists(Long userId, Long roomId);

     boolean existsByUserIdAndRoomRoomId(Long userId, Long roomId);

}
