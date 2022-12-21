package com.user_manager_v1.repository;

import com.user_manager_v1.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

public interface PositionRepository extends JpaRepository<Position,Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO position(latitude, longitude) VALUES(:latitude, :longitude)", nativeQuery = true)

    int savePosition(@Param("latitude") String latitude,
                        @Param("longitude") String longitude);

   @Query(value = "select * from position where user_id = :id" ,nativeQuery = true)
    List<Position> findUserPosition(@Param("id") int id);
}
