package org.example.tinderapp.domain;

import org.example.tinderapp.domain.entity.ProfileEntity;
import java.util.List;

public interface ProfileDao {
    List<ProfileEntity> getAllProfiles();
    void saveProfile(ProfileEntity profile);
    ProfileEntity getProfileById(Long id);
}
