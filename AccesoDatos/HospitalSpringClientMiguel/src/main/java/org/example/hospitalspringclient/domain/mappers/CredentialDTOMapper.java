package org.example.hospitalspringclient.domain.mappers;


import org.example.hospitalspringclient.dao.model.Credential;
import org.example.hospitalspringclient.domain.model.CredentialDTO;
import org.springframework.stereotype.Component;

@Component
public class CredentialDTOMapper {
    public Credential dtoToEntity(CredentialDTO credentialDTO) {
        return new Credential(credentialDTO.getUsername(), credentialDTO.getPassword());
    }

    public CredentialDTO entityToDto(Credential credential) {
        return new CredentialDTO(credential.getUsername(), credential.getPassword());
    }

}
