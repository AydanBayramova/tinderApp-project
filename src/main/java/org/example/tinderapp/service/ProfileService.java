package org.example.tinderapp.service;

import org.example.tinderapp.domain.entity.ProfileEntity;

import java.util.List;

public interface ProfileService {
    List<ProfileEntity> getAllProfiles(long userId);
    ProfileEntity getProfileById(Long userId);
    void saveProfile(ProfileEntity profileEntity);

}
