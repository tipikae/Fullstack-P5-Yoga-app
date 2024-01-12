package com.openclassrooms.starterjwt.integration.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test() throws Exception {
        // get resource without login
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().is(401));

        // register
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\", " +
                        "\"firstName\": \"user-firstname\", " +
                        "\"lastName\": \"user-lastname\", " +
                        "\"password\": \"123456\"}"))
                .andExpect(status().isOk());

        // login
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\", " +
                        "\"password\": \"123456\"}"))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");
        int id = JsonPath.parse(response).read("$.id");

        // get resource with token
        mockMvc.perform(get("/api/teacher").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // delete user
        mockMvc.perform((delete("/api/user/" + Integer.valueOf(id)).header("Authorization", "Bearer " + token)))
                .andExpect(status().isOk());

        // get resource with bad token
        mockMvc.perform(get("/api/teacher").header("Authorization", "Bearer " + token))
                .andExpect(status().is(401));
    }
}
