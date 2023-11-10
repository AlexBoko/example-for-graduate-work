package ru.skypro.homework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

@Component
public class TestInitData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    ObjectMapper objectMapper = new ObjectMapper();

    public JSONObject createUserJson(String username, String password, Role role) {
        JSONObject register = new JSONObject();
        register.put("username", username);
        register.put("password", password);
        register.put("firstName", "firstName");
        register.put("lastName", "lastName");
        register.put("phone", "+7(921)921-21-21");
        register.put("role", role);
        return register;
    }

    public User createTestUser(JSONObject jsonObject) {
        User testUser;
        try {
            testUser = objectMapper.readValue(jsonObject.toJSONString(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Неправильно создан JSON объекта типа User");
        }
        testUser.setPassword(passwordEncoder.encode("password"));
        User savedUser = userRepository.save(testUser);
        return savedUser;
    }
}