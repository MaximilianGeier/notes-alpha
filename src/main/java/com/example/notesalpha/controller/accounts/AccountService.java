package com.example.notesalpha.controller.accounts;

import com.example.notesalpha.controller.database.ClientsDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Map<String, UserProfile> sessionIdToProfile;
    @Autowired
    private ClientsDAO clientsDAO;

    public AccountService() {
        sessionIdToProfile = new HashMap<>();
    }

    public void addNewUser(UserProfile userProfile) throws SQLException {
        clientsDAO.saveProfile(userProfile);
    }

    public UserProfile getUserByLogin(String login) throws SQLException {
        UserProfile profile = clientsDAO.getProfile(login);
        return profile;
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