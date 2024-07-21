package org.example.tinderapp.domain.repository;

import org.example.tinderapp.domain.ProfileDao;
import org.example.tinderapp.domain.entity.ProfileEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfilePostgresRepository implements ProfileDao {

    private final String JDBC_URL = "jdbc:postgresql://localhost:5433/postgres";
    private final String JDBC_USERNAME = "postgres";
    private final String JDBC_PASSWORD = "postgres";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    private ProfileEntity mapRow(ResultSet rs) throws SQLException {
        ProfileEntity profile = new ProfileEntity();
        profile.setId(rs.getLong("id"));
        profile.setName(rs.getString("name"));
        profile.setImageUrl(rs.getString("image_url"));
        return profile;
    }

    @Override
    public List<ProfileEntity> getAllProfiles() {
        final String sql = "SELECT * FROM profiles";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
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
    public ProfileEntity getProfileById(Long id) {
        final String sql = "SELECT * FROM profiles WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            } else {
                throw new RuntimeException("Profile not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveProfile(ProfileEntity profile) {
        final String sql = "INSERT INTO profiles (name, image_url) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, profile.getName());
            preparedStatement.setString(2, profile.getImageUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
