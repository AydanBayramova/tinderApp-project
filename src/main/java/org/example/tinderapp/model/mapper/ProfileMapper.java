package org.example.tinderapp.model.mapper;

import org.example.tinderapp.domain.entity.ProfileEntity;
import org.example.tinderapp.model.dto.ProfileDto;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public ProfileDto toProfileDto(ProfileEntity profileEntity) {
        if (profileEntity == null) {
            return null;
        }
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profileEntity.getId());
        profileDto.setName(profileEntity.getName());
        profileDto.setUserId(profileEntity.getUserId());
        profileDto.setLiked(profileEntity.isLiked());
        profileDto.setPhotoUrl(profileEntity.getPhotoUrl());
        return profileDto;
    }

    public ProfileEntity toProfileEntity(ProfileDto profileDto) {
        if (profileDto == null) {
            return null;
        }
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(profileDto.getId());
        profileEntity.setName(profileDto.getName());
        profileEntity.setUserId(profileDto.getUserId());
        profileEntity.setLiked(profileDto.isLiked());
        profileEntity.setPhotoUrl(profileDto.getPhotoUrl());
        return profileEntity;
    }
}
