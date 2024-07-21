package org.example.tinderapp.domain.repository;

import org.example.tinderapp.domain.ProfileDao;
import org.example.tinderapp.domain.entity.ProfileEntity;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ProfilePostgresRepository implements ProfileDao {

    public ProfilePostgresRepository() {
        createTables();
    }

    private ProfileEntity mapRow(ResultSet rs) throws SQLException {
        ProfileEntity profile = new ProfileEntity();
        profile.setId(rs.getLong("id"));
        profile.setName(rs.getString("name"));
        profile.setPhotoUrl(rs.getString("photo_url"));
        profile.setLiked(rs.getBoolean("liked"));
        profile.setUserId(rs.getLong("user_id"));
        return profile;
    }

    @Override
    public List<ProfileEntity> getAllProfiles() {
        final String sql = "SELECT * FROM profiles WHERE liked = TRUE";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<ProfileEntity> profiles = new ArrayList<>();
            while (rs.next()) {
                profiles.add(mapRow(rs));
            }
            return profiles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProfileEntity> getLikedProfiles(Long userId) {
        final String sql = "SELECT * FROM profiles WHERE liked = TRUE AND user_id = ?";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            List<ProfileEntity> profiles = new ArrayList<>();
            while (rs.next()) {
                profiles.add(mapRow(rs));
            }
            return profiles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveProfile(ProfileEntity profile) {
        final String sql = "INSERT INTO profiles (name, photo_url, liked, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, profile.getName());
            preparedStatement.setString(2, profile.getPhotoUrl());
            preparedStatement.setBoolean(3, profile.isLiked());
            preparedStatement.setLong(4, profile.getUserId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProfile(ProfileEntity profile) {
        final String sql = "UPDATE profiles SET name = ?, photo_url = ?, liked = ?, user_id = ? WHERE id = ?";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, profile.getName());
            preparedStatement.setString(2, profile.getPhotoUrl());
            preparedStatement.setBoolean(3, profile.isLiked());
            preparedStatement.setLong(4, profile.getUserId());
            preparedStatement.setLong(5, profile.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteProfile(Long id) {
        final String sql = "DELETE FROM profiles WHERE id = ?";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveLike(Long userId, Long profileId) {
        final String sql = "INSERT INTO user_likes (user_id, profile_id) VALUES (?, ?)";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, profileId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteLike(Long userId, Long profileId) {
        final String sql = "DELETE FROM user_likes WHERE user_id = ? AND profile_id = ?";
        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, profileId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTables() {
        try (Connection connection = new PostgresDriver().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS profiles(" +
                    "id BIGINT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "photo_url VARCHAR(255)," +
                    "liked BOOLEAN," +
                    "user_id BIGINT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_likes(" +
                    "user_id BIGINT," +
                    "profile_id BIGINT," +
                    "PRIMARY KEY(user_id, profile_id))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
