package org.example.tinderapp.domain.repository;

import org.example.tinderapp.domain.ProfileDao;
import org.example.tinderapp.domain.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class ProfileDaoImpl implements ProfileDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ProfileEntity> profileRowMapper = (rs, rowNum) -> {
        ProfileEntity profile = new ProfileEntity();
        profile.setId(rs.getLong("id"));
        profile.setName(rs.getString("name"));
        profile.setPhotoUrl(rs.getString("photo_url"));
        profile.setLiked(rs.getBoolean("liked"));
        profile.setUserId(rs.getLong("user_id"));
        return profile;
    };

    @Override
    public List<ProfileEntity> getAllProfiles() {
        return jdbcTemplate.query("SELECT * FROM profiles WHERE liked = TRUE", profileRowMapper);
    }

    @Override
    public List<ProfileEntity> getLikedProfiles(Long userId) {
        return jdbcTemplate.query("SELECT * FROM profiles WHERE liked = TRUE AND user_id = ?", new Object[]{userId}, profileRowMapper);
    }

    @Override
    public void saveProfile(ProfileEntity profile) {
        jdbcTemplate.update(
                "INSERT INTO profiles (name, photo_url, liked, user_id) VALUES (?, ?, ?, ?)",
                profile.getName(), profile.getPhotoUrl(), profile.isLiked(), profile.getUserId()
        );
    }

    @Override
    public void updateProfile(ProfileEntity profile) {
        jdbcTemplate.update(
                "UPDATE profiles SET name = ?, photo_url = ?, liked = ?, user_id = ? WHERE id = ?",
                profile.getName(), profile.getPhotoUrl(), profile.isLiked(), profile.getUserId(), profile.getId()
        );
    }

    @Override
    public void deleteProfile(Long id) {
        jdbcTemplate.update("DELETE FROM profiles WHERE id = ?", id);
    }


    @Override
    public void saveLike(Long userId, Long profileId) {
        jdbcTemplate.update(
                "INSERT INTO user_likes (user_id, profile_id) VALUES (?, ?)",
                userId, profileId
        );
    }

    @Override
    public void deleteLike(Long userId, Long profileId) {
        jdbcTemplate.update(
                "DELETE FROM user_likes WHERE user_id = ? AND profile_id = ?",
                userId, profileId
        );
    }
}
