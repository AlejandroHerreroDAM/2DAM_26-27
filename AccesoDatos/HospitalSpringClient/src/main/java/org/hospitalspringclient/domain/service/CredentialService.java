package org.hospitalspringclient.domain.service;



import org.hospitalspringclient.dao.interfaces.CredentialRepository;
import org.hospitalspringclient.dao.model.UserLogin;
import org.hospitalspringclient.domain.model.CredentialDTO;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public boolean login(CredentialDTO credential) {
        if (credential == null
                || credential.getUsername() == null
                || credential.getUsername().isBlank()
                || credential.getPassword() == null
                || credential.getPassword().isBlank()) {
            return false;
        }

        UserLogin storedCredential = credentialRepository.get(credential.getUsername());
        return storedCredential != null
                && credential.getPassword().equals(storedCredential.getPassword());
    }
}
