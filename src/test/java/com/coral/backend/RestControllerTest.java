package com.coral.backend;

import com.coral.backend.repositories.FriendRequestRepository;
import com.coral.backend.repositories.GroupRepository;
import com.coral.backend.repositories.SessionRepository;
import com.coral.backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coral.backend.dtos.LoginDTO;
import com.coral.backend.entities.Users;
import com.scholarsync.backend.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

/**
 * To run this tests is important to put hibernate into update mode
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    FriendRequestRepository friendRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    GroupRepository groupRepository;


    public void createTemplateData() throws Exception {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setUsername("testuser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.parse("1990-01-01"));

        userRepository.save(user);

        Users user1 = new Users();
        user1.setFirstName("Robert");
        user1.setLastName("Smith");
        user1.setEmail("robertSmith@gmail.com");
        user1.setPassword("password123");
        user1.setUsername("robertsmith");

        userRepository.save(user1);
    }

    @Test
    public void registerUserTest() throws Exception {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setUsername("testuser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.parse("1990-01-01"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void loginUserTest() throws Exception {
        LoginDTO userDTO = new LoginDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void registerRobertTest() throws Exception {
        Users user = new Users();
        user.setFirstName("Robert");
        user.setLastName("Smith");
        user.setEmail("robertSmith@gmail.com");
        user.setPassword("password123");
        user.setUsername("robertsmith");
        user.setBirthDate(LocalDate.parse("1990-01-01"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    public void sendFriendRequestTest() throws Exception {
//        createTemplateData();
        String idTestUser = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/get-id-by-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        String idRobertSmith = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/get-id-by-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"robertsmith\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/send-friend-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from_id\":\"" + idTestUser + "\",\"to_id\":\"" + idRobertSmith + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllRequestsTest() throws Exception {
        String idRobertSmith = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/get-id-by-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"robertsmith\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"+idRobertSmith+"/friend-requests"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(response);
    }

    @Test
    public void registerAndSendRequestToRobert() throws Exception {

        String userId = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/get-id-by-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"lautarom\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        String roberId = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/get-id-by-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"robertsmith\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/send-friend-request")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"from_id\":\"" + userId + "\",\"to_id\":\"" + roberId + "\"}")).andExpect(MockMvcResultMatchers.status().isOk());
    }


    /**
     * this test will delete all the data from the database
     */
    @Test
    public void deleteAll(){
        friendRequestRepository.deleteAll();
        sessionRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
        Assertions.assertTrue(true);
    }

}