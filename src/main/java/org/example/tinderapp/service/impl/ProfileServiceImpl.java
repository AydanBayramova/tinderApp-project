package org.example.tinderapp.service.impl;

import org.example.tinderapp.domain.ProfileDao;
import org.example.tinderapp.domain.entity.ProfileEntity;


import org.example.tinderapp.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    private ProfileDao profileDao;
    @Autowired
    public ProfileServiceImpl(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @Override
    public List<ProfileEntity> getAllProfiles(long userId) {
        return profileDao.getAllProfiles();
    }

    @Override
    public ProfileEntity getProfileById(Long userId) {
        return (ProfileEntity) profileDao.getProfileById(userId);
    }

    @Override
    public void saveProfile(ProfileEntity profileEntity) {
       profileDao.saveProfile(profileEntity);
    }
}
