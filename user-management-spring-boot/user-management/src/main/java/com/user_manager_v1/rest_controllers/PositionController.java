package com.user_manager_v1.rest_controllers;

import com.user_manager_v1.models.Position;
import com.user_manager_v1.models.User;
import com.user_manager_v1.repository.PositionRepository;
import com.user_manager_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/position")
public class PositionController {
    @Autowired
    PositionRepository positionRepository;
    @Autowired UserRepository userRepository;
    @PostMapping("/save")
    public void savePosition(@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, @RequestParam("user_id") String i){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        Position position = new Position();
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setDate(Date.valueOf(localDate));
        position.setUser_id(Integer.parseInt(i));
        positionRepository.save(position);
    }


    @GetMapping("/user/{id}")
    public List<Position> findUserPosition(@PathVariable int id){
        return positionRepository.findUserPosition(id);
    }
}
