package org.matrixstudio.ocean.oauth2.config.security.oauth2;

import org.matrixstudio.ocean.core.repository.ClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public class JpaClientDetailsService implements ClientDetailsService {

    private ClientRepository clientRepository;

    public JpaClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findById(clientId).orElse(null);
    }
}
