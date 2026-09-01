package com.example.demo.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public User findById(int userId) {
        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        int getUserParams = userId;

        return this.jdbcTemplate.queryForObject(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getUserParams
        );
    }

    public List<User> findAll() {
        String getUserQuery = "SELECT * FROM \"user\"";

        return this.jdbcTemplate.queryForStream(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
        ).toList();
    }

    public User save(String name, Integer age, String job, String specialty) {

        String createUserQuery = "INSERT INTO \"user\" (name, age, job, specialty, created_at) VALUES(?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{name, age, job, specialty, LocalDateTime.now()};
        this.jdbcTemplate.update(
                createUserQuery,
                createUserParams
        );
        // (B) SELECT id - MySQL:last_insert_id()->id / PostgresQL:currval()->lastval/lastval()->lastval
        String lastInsertIdQuery = "SELECT lastval()"; // 마지막 저장 데이터 ID
        Integer createdUserId = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);


        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        Integer getUserParams = createdUserId;

        return this.jdbcTemplate.queryForObject(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getUserParams
        );
    }

    public void delete(Integer userId) {
        String deleteUserQuery = "DELETE FROM \"user\" WHERE id = ?";
        Object[] deleteUserParams = new Object[]{userId};
        this.jdbcTemplate.update(
                deleteUserQuery,
                deleteUserParams
        );
    }
}
