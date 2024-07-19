package org.example.tinderapp.service.impl;

import org.example.tinderapp.domain.ProfileDao;
import org.example.tinderapp.domain.entity.ProfileEntity;
import org.example.tinderapp.model.dto.ProfileDto;
import org.example.tinderapp.model.mapper.ProfileMapper;
import org.example.tinderapp.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileServiceImpl(ProfileDao profileDao, ProfileMapper profileMapper) {
        this.profileDao = profileDao;
        this.profileMapper = profileMapper;
    }

    @Override
    public List<ProfileDto> getAllProfiles() {
        List<ProfileEntity> profiles = profileDao.getAllProfiles();
        return profiles.stream()
                .map(profileMapper::toProfileDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfileDto> getLikedProfiles(Long userId) {
        List<ProfileEntity> profiles = profileDao.getLikedProfiles(userId);
        return profiles.stream()
                .map(profileMapper::toProfileDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveProfile(ProfileDto profileDTO) {
        ProfileEntity profileEntity = profileMapper.toProfileEntity(profileDTO);
        profileDao.saveProfile(profileEntity);
    }

    @Override
    public void updateProfile(ProfileDto profileDTO) {
        ProfileEntity profileEntity = profileMapper.toProfileEntity(profileDTO);
        profileDao.updateProfile(profileEntity);
    }

    @Override
    public void deleteProfile(Long id) {
        profileDao.deleteProfile(id);
    }

    @Override
    public void saveLike(Long userId, Long profileId) {
        profileDao.saveLike(userId, profileId);
    }

    @Override
    public void deleteLike(Long userId, Long profileId) {
        profileDao.deleteLike(userId, profileId);
    }
}
