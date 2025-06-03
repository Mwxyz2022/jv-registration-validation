package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_AGE = 18;

    private static final String ERROR_MESSAGE_MISMATCH =
            "Exception message text doesn't match the expected value";

    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl(new StorageDaoImpl());
    }

    @AfterEach
    void tearDown() {
        Storage.people.clear();
    }

    @Test
    void register_validUserWithLoginAtMinLength_ok() {
        User user = new User();
        user.setLogin("sixSym");
        user.setPassword("somePassword");
        user.setAge(25);

        User actual = registrationService.register(user);

        assertNotNull(actual, "Registered user object should not be null");
        assertNotNull(actual.getId(), "Registered user should have an ID assigned");

        assertEquals("sixSym", actual.getLogin(), "Login should match the input");
        assertEquals("somePassword", actual.getPassword(), "Password should match the input");
        assertEquals(25, actual.getAge(), "Age should match the input");

        assertTrue(Storage.people.contains(actual), "User should be added to the storage");
        assertEquals(1, Storage.people.size(), "Storage should contain exactly one user");
    }

    @Test
    void register_nullUser_notOk() {
        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(null),
                "Expected RegistrationException when user is null");

        assertEquals("User cannot be null!", exception.getMessage(), ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User();
        user.setLogin(null);
        user.setPassword("somePassword");
        user.setAge(25);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when login is null");

        assertEquals("Login cannot be null or empty!", exception.getMessage(),
                ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_emptyLogin_notOk() {
        User user = new User();
        user.setLogin("");
        user.setPassword("somePassword");
        user.setAge(25);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when login is empty");

        assertEquals("Login cannot be null or empty!", exception.getMessage(),
                ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_loginTooShort_notOk() {
        User user = new User();
        user.setLogin("short");
        user.setPassword("somePassword");
        user.setAge(25);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when login is too short");

        assertEquals("Login must be at least " + MIN_LOGIN_LENGTH + " characters long!",
                exception.getMessage(), ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_nullPassword_notOk() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword(null);
        user.setAge(25);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when password is null");

        assertEquals("Password cannot be null or empty!", exception.getMessage(),
                ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_emptyPassword_notOk() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword("");
        user.setAge(25);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when password is empty");

        assertEquals("Password cannot be null or empty!", exception.getMessage(),
                ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_passwordTooShort_notOk() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword("short");
        user.setAge(25);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when password is too short");

        assertEquals("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long!",
                exception.getMessage(), ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_passwordExactlyMinLength_ok() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword("123456");
        user.setAge(25);

        User actual = registrationService.register(user);

        assertNotNull(actual, "Registered user object should not be null");
        assertNotNull(actual.getId(), "Registered user should have an ID assigned");
        assertEquals("123456", actual.getPassword(), "Password should match the input");
        assertTrue(Storage.people.contains(actual), "User should be added to the storage");
        assertEquals(1, Storage.people.size(), "Storage should contain exactly one user");
    }

    @Test
    void register_invalidAge_notOk() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword("somePassword");
        user.setAge(17);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when user age is below minimum");
        assertEquals("Not valid age: " + user.getAge() + ". Min allowed age is " + MIN_AGE,
                exception.getMessage(), ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword("somePassword");
        user.setAge(null);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Expected RegistrationException when age is null");

        assertEquals("User age cannot be null!", exception.getMessage(), ERROR_MESSAGE_MISMATCH);
        assertTrue(Storage.people.isEmpty(), "Storage should be empty after failed registration");
    }

    @Test
    void register_ageExactlyMinAge_ok() {
        User user = new User();
        user.setLogin("someLogin");
        user.setPassword("somePassword");
        user.setAge(18);

        User actual = registrationService.register(user);

        assertNotNull(actual, "Registered user object should not be null");
        assertNotNull(actual.getId(), "Registered user should have an ID assigned");
        assertEquals(18, actual.getAge(), "Age should match the input");
        assertTrue(Storage.people.contains(actual), "User should be added to the storage");
        assertEquals(1, Storage.people.size(), "Storage should contain exactly one user");
    }

    @Test
    void register_duplicateLogin_notOk() {
        User firstUser = new User();
        firstUser.setLogin("someLogin");
        firstUser.setPassword("somePassword");
        firstUser.setAge(22);
        Storage.people.add(firstUser);

        assertEquals(1, Storage.people.size(),
                "Storage should contain one user after first successful registration");
        assertEquals("someLogin", Storage.people.get(0).getLogin(),
                "The login of the first user in storage should be 'someLogin'");

        User secondUser = new User();
        secondUser.setLogin("someLogin");
        secondUser.setPassword("anySomePassword");
        secondUser.setAge(24);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> registrationService.register(secondUser),
                "Expected RegistrationException when trying to register with a duplicate login");

        assertEquals("Login " + secondUser.getLogin() + " is already taken!",
                exception.getMessage(), ERROR_MESSAGE_MISMATCH);

        assertEquals(1, Storage.people.size(), "Storage should still contain only the"
                        + " first user after failed duplicate registration");
        assertTrue(Storage.people.contains(firstUser),
                "First registered user should still be present in the storage");
        assertFalse(Storage.people.contains(secondUser),
                "Duplicate user must not be added to the storage");
    }
}
