package com.example.notesalpha.controller.database;

import com.example.notesalpha.controller.accounts.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientsDAO {
    private final Map<String, UserProfile> sessionIdToProfile;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        sessionIdToProfile = new HashMap<>();
    }

    public void saveProfile(UserProfile userProfile) {
        try {
            jdbcTemplate.update(String.format("INSERT clients(login, pass, email) VALUES ('%s', '%s', '%s');", userProfile.getLogin(), userProfile.getPass(), userProfile.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserProfile getProfile(String login) {
        try {
            return jdbcTemplate.query("SELECT * FROM clients WHERE login = '" + login + "';", result -> {
                if (result.next()) {
                    return new UserProfile(result.getString(1),
                            result.getString(2),
                            result.getString(3));
                }
                else {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
