package jdbc;

import dao.UserDao;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateJdbcTemplate {
    public void update(User user, UserDao userDao) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = userDao.userUpdateQuery();
            pstmt = conn.prepareStatement(sql);
            userDao.insertValuesForUpdate(user, pstmt);

            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
