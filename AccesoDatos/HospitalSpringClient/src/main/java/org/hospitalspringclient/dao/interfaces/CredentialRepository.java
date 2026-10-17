package org.hospitalspringclient.dao.interfaces;


import org.hospitalspringclient.dao.model.UserLogin;

public interface CredentialRepository {

    UserLogin get(String username);
}