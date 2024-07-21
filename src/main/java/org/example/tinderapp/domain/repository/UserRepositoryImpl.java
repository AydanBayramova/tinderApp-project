package org.example.tinderapp.domain.repository;

import org.example.tinderapp.domain.entity.User;
import org.example.tinderapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (name,surname,user_name,email,phoneNumber,password) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getUserName(), user.getEmail(), user.getPhoneNumber(), user.getPassword());
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE user_name=?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
        return Optional.ofNullable(user);
    }

    @Override
    public Long getUserId(String username, String password) throws UserNotFoundException{
        String sql = "SELECT id FROM users WHERE user_name=? AND password=?";
        Long userId=jdbcTemplate.queryForObject(sql,new Object[]{username,password},Long.class);
        return userId;
    }

    @Override
    public void deleteById(Long id) {
    String sql = "DELETE FROM users WHERE id=?";
    jdbcTemplate.update(sql,id);
    }

    @Override
    public List<User> findAll() {
        String qsql = "SELECT * FROM users";
        return jdbcTemplate.query(qsql, new UserRowMapper());
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("user_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("password")
            );
        }
    }

}

