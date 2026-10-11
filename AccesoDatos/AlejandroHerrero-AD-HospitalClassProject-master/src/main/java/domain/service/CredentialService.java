package domain.service;

import dao.interfaces.CredentialRepository;
import dao.model.UserLogin;
import domain.model.CredentialDTO;
import jakarta.inject.Inject;

public class CredentialService {
    private final CredentialRepository credentialRepository;

    @Inject
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
