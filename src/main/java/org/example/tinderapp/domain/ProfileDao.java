package org.example.tinderapp.domain;

import org.example.tinderapp.domain.entity.ProfileEntity;
import java.util.List;

public interface ProfileDao {
    List<ProfileEntity> getAllProfiles();
    List<ProfileEntity> getLikedProfiles(Long userId);
    void saveProfile(ProfileEntity profile);
    void updateProfile(ProfileEntity profile);
    void deleteProfile(Long id);
    void saveLike(Long userId, Long profileId);
    void deleteLike(Long userId, Long profileId);
}
