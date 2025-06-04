package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_AGE = 18;

    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User cannot be null!");
        }

        validateLogin(user.getLogin());
        validatePassword(user.getPassword());
        validateAge(user.getAge());

        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException(
                    String.format("Login %s is already taken!", user.getLogin()));
        }

        return storageDao.add(user);
    }

    private void validateLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new RegistrationException("Login cannot be null or empty!");
        }

        if (login.length() < MIN_LOGIN_LENGTH) {
            throw new RegistrationException(
                    String.format("Login must be at least %d characters long!", MIN_LOGIN_LENGTH));
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new RegistrationException("Password cannot be null or empty!");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new RegistrationException(
                    String.format("Password must be at least %d characters long!",
                            MIN_PASSWORD_LENGTH));
        }
    }

    private void validateAge(Integer age) {
        if (age == null || age < MIN_AGE) {
            throw new RegistrationException(
                    String.format("User age must be at least %d!", MIN_AGE));
        }
    }
}
