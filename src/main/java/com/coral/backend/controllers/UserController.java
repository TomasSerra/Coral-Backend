package com.coral.backend.controllers;

import com.coral.backend.dtos.EnterpriseDTO;
import com.coral.backend.dtos.FollowInvestorDTO;
import com.coral.backend.dtos.InvestDTO;
import com.coral.backend.dtos.InvestorDTO;
import com.coral.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /*@GetMapping("/{id}")
    public ResponseEntity<Users> getUser(@PathVariable String id) {
        Optional<Users> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }*/
    @PostMapping("/create-investor-profile")
    public ResponseEntity<Object> createInvestorProfile(@RequestBody InvestorDTO requestBody){
        return userService.createInvestorProfile(requestBody);
    }

    @PostMapping("/create-enterprise-profile")
    public ResponseEntity<Object> createEnterpriseProfile(@RequestBody EnterpriseDTO requestBody){
        return userService.createEnterpriseProfile(requestBody);
    }

    @PostMapping("/enterprise")
    public ResponseEntity<Object> getEnterpriseProfile(@RequestBody EnterpriseDTO requestBody){
        return userService.getEnterpriseProfile(requestBody);
    }

    @PostMapping("/investor")
    public ResponseEntity<Object> getInvestorProfile(@RequestBody InvestorDTO requestBody){
        return userService.getInvestorProfile(requestBody);
    }

    @PostMapping("/invest")
    public ResponseEntity<Object> investInEnterprise(@RequestBody InvestDTO requestBody){
        return userService.investInEnterprise(requestBody);
    }

    @PostMapping("/follow")
    public ResponseEntity<Object> followInvestor(@RequestBody FollowInvestorDTO requestBody){
        return userService.followInvestor(requestBody);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Object> unfollowInvestor(@RequestBody FollowInvestorDTO requestBody){
        return userService.unfollowInvestor(requestBody);
    }
}