package org.example.hospitalspringclient.domain.service;
import org.example.hospitalspringclient.dao.model.Credential;
import org.example.hospitalspringclient.dao.repositories.CredentialRepository;
import org.example.hospitalspringclient.domain.model.CredentialDTO;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;


    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;

    }

    public boolean login(CredentialDTO credentialDTO) {
        Credential credential = credentialRepository.getByUsername(credentialDTO.getUsername());
        if (credential != null) {
            if (credentialDTO.getPassword().equals(credential.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
