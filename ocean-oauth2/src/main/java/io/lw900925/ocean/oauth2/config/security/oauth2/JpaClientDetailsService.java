package io.lw900925.ocean.oauth2.config.security.oauth2;

import io.lw900925.ocean.core.repository.jpa.ClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public class JpaClientDetailsService implements ClientDetailsService {

    private final ClientRepository clientRepository;

    public JpaClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findById(clientId).orElse(null);
    }
}
