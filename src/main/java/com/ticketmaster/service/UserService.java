package com.ticketmaster.service;

import com.ticketmaster.dao.UserDAO;
import com.ticketmaster.model.User;
import com.ticketmaster.model.UserProfile;
import com.ticketmaster.util.PasswordUtil;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public void changePassword(String username, String newPassword) {
        String passwordHash = PasswordUtil.hashPassword(newPassword);
        userDAO.updatePassword(username, passwordHash);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public long getUserCount() {
        return userDAO.getUserCount();
    }

    public boolean createUser(String username, UserProfile profile) {
        // Default password is "test123"
        String defaultPassword = "test123";
        String passwordHash = PasswordUtil.hashPassword(defaultPassword);
        return userDAO.createUser(username, passwordHash, profile);
    }

    public boolean isUsernameExists(String username) {
        return userDAO.findByUsername(username) != null;
    }
}

