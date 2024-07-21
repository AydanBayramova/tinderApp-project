package org.example.tinderapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tinderapp.domain.entity.ProfileEntity;
import org.example.tinderapp.service.ProfileService;
import org.example.tinderapp.utils.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String getAllProfiles(Model model) throws SQLException {
        List<ProfileEntity> profiles = profileService.getAllProfiles(Utils.userId);
        model.addAttribute("profiles", profiles);
        return "profile-list";
    }

    @PostMapping("/like")
    public String likeProfile(@RequestParam Long userId, @RequestParam Long profileId, Model model) throws SQLException {
        ProfileEntity profile = profileService.getProfileById(profileId);
        profileService.saveProfile(profile);
        List<ProfileEntity> profiles = profileService.getAllProfiles(Utils.userId);
        model.addAttribute("profiles", profiles);
        return "profile-list";
    }

    @GetMapping("/liked")
    @ResponseBody
    public List<ProfileEntity> getAllLikedProfiles() {
        log.info("Get all liked profiles: \"GET -> /liked\"");
        return profileService.getAllProfiles(Utils.userId); // Change this if liked profiles are stored differently
    }

    @GetMapping("/{profileId}")
    @ResponseBody
    public ProfileEntity getProfile(@PathVariable Long profileId) {
        log.info("Get profile: \"GET -> /{profileId}\"");
        return profileService.getProfileById(profileId);
    }

    @PostMapping("/create")
    @ResponseBody
    public void createProfile(@RequestBody ProfileEntity profileDto) {
        log.info("Create profile: \"POST -> /create\", profile: {}", profileDto);
        profileService.saveProfile(profileDto);
    }

    @GetMapping("/messages/{id}")
    public String getMessages(@PathVariable Long id, Model model) {
        // Add hardcoded messages to the model
        List<String> messages = List.of(
                "Hii",
                "hiii\nHow are you?",
                "nice\nAre you fine?",
                "Yes always",
                "Byy"
        );
        model.addAttribute("messages", messages);
        return "chat";
    }
}
