package org.example.tinderapp.service;

import org.example.tinderapp.model.dto.ProfileDto;
import java.util.List;

public interface ProfileService {
    List<ProfileDto> getAllProfiles();
    List<ProfileDto> getLikedProfiles(Long userId);
    void saveProfile(ProfileDto profileDTO);
    void updateProfile(ProfileDto profileDTO);
    void deleteProfile(Long id);
    void saveLike(Long userId, Long profileId);
    void deleteLike(Long userId, Long profileId);
}
