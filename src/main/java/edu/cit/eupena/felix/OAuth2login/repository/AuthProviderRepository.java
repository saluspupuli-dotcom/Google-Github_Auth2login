package edu.cit.eupena.felix.OAuth2login.repository;

import edu.cit.eupena.felix.OAuth2login.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    Optional<AuthProvider> findByProviderAndProviderUserId(String provider, String providerUserId);
}
