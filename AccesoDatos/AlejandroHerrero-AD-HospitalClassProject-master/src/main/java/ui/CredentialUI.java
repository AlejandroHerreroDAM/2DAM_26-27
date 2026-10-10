package ui;

import domain.model.CredentialDTO;
import domain.service.CredentialService;
import jakarta.inject.Inject;

public class CredentialUI {
    private final CredentialService credentialService;

    @Inject
    public CredentialUI(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    public boolean login(CredentialDTO credential) {
        return credentialService.login(credential);
    }
}
