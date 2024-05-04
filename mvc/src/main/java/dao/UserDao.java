package dao;

import jdbc.ConnectionManager;
import jdbc.InsertJdbcTemplate;
import jdbc.UpdateJdbcTemplate;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        InsertJdbcTemplate insertJdbcTemplate = new InsertJdbcTemplate();
        insertJdbcTemplate.insert(user, this);
    }

    public void update(User user) throws SQLException {
        UpdateJdbcTemplate updateJdbcTemplate = new UpdateJdbcTemplate();
        updateJdbcTemplate.update(user, this);
    }

    public List<User> findAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            String sql = findAllQuery();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
                users.add(user);
            }

            return users;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public User findByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = findUserQuery();
            pstmt = con.prepareStatement(sql);
            insertValuesForFindUser(userId, pstmt);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public String userInsertQuery() {
        return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    }

    public void insertValuesForInsert(User user, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, user.getUserId());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
    }

    public String userUpdateQuery() {
        return "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
    }

    public void insertValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, user.getPassword());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getUserId());
    }

    public String findAllQuery() {
        return "SELECT userId, password, name, email FROM USERS";
    }

    public String findUserQuery() {
        return "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    }

    public void insertValuesForFindUser(String userId, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, userId);
    }
}
