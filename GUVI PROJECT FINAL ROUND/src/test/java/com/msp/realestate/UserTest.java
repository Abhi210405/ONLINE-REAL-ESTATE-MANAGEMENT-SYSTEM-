package com.msp.realestate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 */
public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1, "testuser", "test@example.com", "555-1234", "admin");
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user);
        assertEquals(1, user.id);
        assertEquals("testuser", user.username);
        assertEquals("test@example.com", user.email);
        assertEquals("555-1234", user.phone);
        assertEquals("admin", user.role);
    }

    @Test
    public void testUserFieldModification() {
        user.username = "newuser";
        user.email = "newuser@example.com";
        user.phone = "555-5678";
        user.role = "user";

        assertEquals("newuser", user.username);
        assertEquals("newuser@example.com", user.email);
        assertEquals("555-5678", user.phone);
        assertEquals("user", user.role);
    }

    @Test
    public void testUserIdField() {
        assertEquals(1, user.id);
    }

    @Test
    public void testMultipleUsers() {
        User user2 = new User(2, "anotheruser", "another@example.com", "555-9999", "user");
        assertEquals(2, user2.id);
        assertEquals("anotheruser", user2.username);
        assertNotEquals(user.id, user2.id);
    }

    @Test
    public void testUserWithEmptyEmail() {
        User emptyEmailUser = new User(3, "nomail", "", "555-0000", "guest");
        assertEquals("", emptyEmailUser.email);
        assertEquals("nomail", emptyEmailUser.username);
    }

    @Test
    public void testUserFieldIndependence() {
        User user3 = new User(3, "user3", "user3@example.com", "555-3333", "moderator");
        String originalRole = user3.role;
        user3.role = "admin";
        assertNotEquals(originalRole, user3.role);
        assertEquals("admin", user3.role);
    }
}

