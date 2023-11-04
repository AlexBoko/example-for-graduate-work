package ru.skypro.homework.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import ru.skypro.homework.TestInitData;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@TestPropertySource(properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:15.2-alpine:///db"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitData testInitData;

    @Test
    public void registerUserWhenUserNotExistsAndGetResponse_isCreated() throws Exception {
        JSONObject register = testInitData.createUserJson("user@yandex.ru", "password", Role.USER);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register.toString()))
                .andExpect(status().isCreated());
        User initUser = objectMapper.readValue(register.toJSONString(), User.class);
        User regigisteredUser = userRepository.findByUsername(initUser.getUsername()).get();
        assertEquals(regigisteredUser.getUsername(), initUser.getUsername());
        assertEquals(regigisteredUser.getFirstName(), initUser.getFirstName());
        assertEquals(regigisteredUser.getLastName(), initUser.getLastName());
        assertEquals(regigisteredUser.getPhone(), initUser.getPhone());
        assertEquals(regigisteredUser.getRole(), initUser.getRole());
    }
    @Test
    public void registerUserWhenUserExistsAndGetResponse_isBadRequest() throws Exception {
        JSONObject register = testInitData.createUserJson("user@yandex.ru", "password", Role.USER);
        User initUser = testInitData.createTestUser(register);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginUserWithCorrectCredentialsAndGetResponse_isOk() throws Exception {
        JSONObject register = testInitData.createUserJson("user@yandex.ru", "password", Role.USER);
        User initUser = testInitData.createTestUser(register);
        JSONObject login = new JSONObject();
        login.put("username", "user@yandex.ru");
        login.put("password", "password");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void loginUserWithIncorrectUsernameAndGetResponse_isUnauthorized() throws Exception {
        JSONObject register = testInitData.createUserJson("user@yandex.ru", "password", Role.USER);
        User initUser = testInitData.createTestUser(register);
        JSONObject login = new JSONObject();
        login.put("username", "incorrect@yandex.ru");
        login.put("password", "password");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginUserWithIncorrectPasswordAndGetResponse_isUnauthorized() throws Exception {
        JSONObject register = testInitData.createUserJson("user@yandex.ru", "password", Role.USER);
        User initUser = testInitData.createTestUser(register);
        JSONObject login = new JSONObject();
        login.put("username", "user@yandex.ru");
        login.put("password", "incorrectPassword");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login.toString()))
                .andExpect(status().isUnauthorized());
    }
}
