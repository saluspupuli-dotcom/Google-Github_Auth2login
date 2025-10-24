package edu.cit.eupena.felix.OAuth2login.config;
import edu.cit.eupena.felix.OAuth2login.service.UserService;
import edu.cit.eupena.felix.OAuth2login.model.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase(); // "google" or "github"
        Map<String, Object> attributes = oauth2User.getAttributes();
        String providerUserId = null;
        String email = null;
        String displayName = null;
        String avatarUrl = null;

        if ("GOOGLE".equals(provider)) {
            providerUserId = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            avatarUrl = (String) attributes.get("picture");
        } else if ("GITHUB".equals(provider)) {
            providerUserId = String.valueOf(attributes.get("id"));
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            avatarUrl = (String) attributes.get("avatar_url");
        } else {
            providerUserId = (String) attributes.get("id");
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            avatarUrl = (String) attributes.get("picture");
        }

        User user = userService.provisionUser(provider, providerUserId, email, displayName, avatarUrl);
        return oauth2User;
    }

}
