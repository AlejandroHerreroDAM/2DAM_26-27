package dao.interfaces;

import dao.model.UserLogin;

public interface CredentialRepository {

    UserLogin get(String username);
}