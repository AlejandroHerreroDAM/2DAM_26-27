package org.example.hospitalspringclient.dao.repositories;


import org.example.hospitalspringclient.dao.model.Credential;

public interface CredentialRepository {
    Credential getByUsername(String username);

}
