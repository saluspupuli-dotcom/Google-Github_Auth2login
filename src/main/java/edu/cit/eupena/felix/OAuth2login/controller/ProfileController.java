package edu.cit.eupena.felix.OAuth2login.controller;

import edu.cit.eupena.felix.OAuth2login.model.User;
import edu.cit.eupena.felix.OAuth2login.repository.UserRepository;
import edu.cit.eupena.felix.OAuth2login.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final UserService userService;

    public ProfileController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public User getProfile(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = (String) oauth2User.getAttributes().get("email");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public User updateProfile(@AuthenticationPrincipal OAuth2User oauth2User,
                              @RequestBody User incoming) {
        String email = (String) oauth2User.getAttributes().get("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (incoming.getDisplayName() != null) user.setDisplayName(incoming.getDisplayName());
        if (incoming.getBio() != null) user.setBio(incoming.getBio());
        return userService.saveProfile(user);
    }
}
