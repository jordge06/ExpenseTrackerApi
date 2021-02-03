package com.example.authwithdatabase.repositories;

import com.example.authwithdatabase.domain.User;
import com.example.authwithdatabase.exceptions.EtAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID, " +
            "FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES(NEXTVAL('ET_USERS_SEQ'), ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, " +
            "PASSWORD FROM ET_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, " +
            "PASSWORD FROM ET_USERS WHERE EMAIL = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String firstname, String lastname, String email, String password)
            throws EtAuthException {
        String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        SQL_CREATE,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, firstname);
                ps.setString(2, lastname);
                ps.setString(3, email);
                ps.setString(4, hashedPw);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        } catch (Exception e) {
            throw new EtAuthException("Invalid Details, Failed to Create Account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, (rs, i) -> {
                return new User(
                        rs.getInt("USER_ID"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("EMAIL"),
                        rs.getString("PASSWORD")
                );
            }, email);
            if (!BCrypt.checkpw(password, user.getPassword()))
                throw new EtAuthException("Invalid Email or Password");
            return user;
        } catch (Exception e) {
            throw new EtAuthException("Invalid Email or Password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, (rs, i) -> {
            return new User(
                    rs.getInt("USER_ID"),
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"),
                    rs.getString("EMAIL"),
                    rs.getString("PASSWORD")
            );
        }, userId);
    }
}
