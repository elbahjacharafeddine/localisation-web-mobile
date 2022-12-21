package com.user_manager_v1.rest_controllers;

import com.user_manager_v1.models.User;
import com.user_manager_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/all")
    public List<User> getAlluser(){
        return userRepository.findAllUser();
    }

    @GetMapping("/edit/{id}")
    public Optional<User> findUserId(@PathVariable int id){
       return userRepository.findById(id);
    }

    @GetMapping("/{user_id}/add/{ami_id}")
    public String addFriends(@PathVariable int user_id,@PathVariable int ami_id){
        userRepository.addAmi(user_id,ami_id);
        return "good" + user_id+ " add "+ ami_id+"  comme ami";
    }

    @GetMapping("/all/{id}")
    public List<User> getUsers(@PathVariable int id){
        return userRepository.getUsers(id);
    }

    @GetMapping("/{user_id}/accept/{ami_id}")
    void acceptInvitaion(@PathVariable int user_id,@PathVariable int ami_id){
        userRepository.acceptInvitaion(ami_id,user_id);
    }

    @GetMapping("/{id}/amis")
    List<User> getAllAmis(@PathVariable int id){
        return userRepository.getAllAmis(id);
    }

    @GetMapping("/demandeur-invit/{id}")
    public List<User> getDemandeurInvit(@PathVariable int id){
        return userRepository.getDemandeurInvit(id);
    }


    @PostMapping("/update/{id}")
    void updateImageUser(@RequestParam("image")MultipartFile file,@PathVariable int id) throws IOException {
        userRepository.updateImageUser(file.getBytes(),id);
    }


}
