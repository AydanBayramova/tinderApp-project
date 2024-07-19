package org.example.tinderapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tinderapp.model.dto.ProfileDto;
import org.example.tinderapp.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profiles")
    public List<ProfileDto> getAllLikedProfiles() {
        log.info("Get all liked profiles: \"GET -> /profiles\"");
        return profileService.getAllProfiles();
    }

    @PostMapping("/like")
    public void likeProfile(@RequestParam Long userId, @RequestParam Long profileId) {
        log.info("Like profile: \"POST -> /api/v1/likes/like\", userId: {}, profileId: {}", userId, profileId);
        profileService.saveLike(userId, profileId);
    }

    @DeleteMapping("/unlike")
    public void unlikeProfile(@RequestParam Long userId, @RequestParam Long profileId) {
        log.info("Unlike profile: \"DELETE -> /api/v1/likes/unlike\", userId: {}, profileId: {}", userId, profileId);
        profileService.deleteLike(userId, profileId);
    }

    @PostMapping("/profile")
    public void createProfile(@RequestBody ProfileDto profileDto) {
        log.info("Create profile: \"POST -> /api/v1/likes/profile\", profile: {}", profileDto);
        profileService.saveProfile(profileDto);
    }

    @PutMapping("/profile")
    public void updateProfile(@RequestBody ProfileDto profileDto) {
        log.info("Update profile: \"PUT -> /api/v1/likes/profile\", profile: {}", profileDto);
        profileService.updateProfile(profileDto);
    }

    @DeleteMapping("/profile/{id}")
    public void deleteProfile(@PathVariable Long id) {
        log.info("Delete profile: \"DELETE -> /api/v1/likes/profile/{}\"", id);
        profileService.deleteProfile(id);
    }
}
