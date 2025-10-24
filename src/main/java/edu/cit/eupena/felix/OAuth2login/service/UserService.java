package edu.cit.eupena.felix.OAuth2login.service;

import edu.cit.eupena.felix.OAuth2login.model.User;
import edu.cit.eupena.felix.OAuth2login.model.AuthProvider;
import edu.cit.eupena.felix.OAuth2login.repository.UserRepository;
import edu.cit.eupena.felix.OAuth2login.repository.AuthProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final AuthProviderRepository authRepo;

    public UserService(UserRepository userRepo, AuthProviderRepository authRepo) {
        this.userRepo = userRepo;
        this.authRepo = authRepo;
    }

    @Transactional
    public User provisionUser(String provider, String providerUserId, String email, String displayName, String avatarUrl) {
        Optional<AuthProvider> existing = authRepo.findByProviderAndProviderUserId(provider, providerUserId);
        if (existing.isPresent()) {
            return existing.get().getUser();
        }

        User user = null;
        if (email != null) {
            user = userRepo.findByEmail(email).orElse(null);
        }
        if (user == null) {
            user = new User();
            user.setEmail(email != null ? email : provider + "_" + providerUserId + "@noemail");
            user.setDisplayName(displayName);
            user = userRepo.save(user);
        }

        AuthProvider ap = new AuthProvider();
        ap.setProvider(provider);
        ap.setProviderUserId(providerUserId);
        ap.setProviderEmail(email);
        ap.setUser(user);
        authRepo.save(ap);

        return user;
    }

    public User saveProfile(User user) {
        return userRepo.save(user);
    }
}
