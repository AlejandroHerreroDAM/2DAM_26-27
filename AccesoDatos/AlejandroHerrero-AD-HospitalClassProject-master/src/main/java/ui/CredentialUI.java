package ui;

import domain.model.CredentialDTO;
import domain.service.CredentialService;
import common.Constants;
import jakarta.inject.Inject;
import java.util.Scanner;

public class CredentialUI {
    private final CredentialService credentialService;

    @Inject
    public CredentialUI(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    public boolean login(CredentialDTO credential) {
        return credentialService.login(credential);
    }

    public CredentialDTO readCredential() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(Constants.USERNAME);
        String username = scanner.nextLine();
        System.out.print(Constants.PASSWORD);
        String password = scanner.nextLine();
        return new CredentialDTO(username, password);
    }
}
