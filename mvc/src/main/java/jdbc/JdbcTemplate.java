package jdbc;

import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, Object... parameters) throws DataAccessException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; ++i) {
                pstmt.setObject(i + 1, parameters[i]);
            }
            pstmt.executeQuery();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
        ResultSet rs;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pss.setValues(pstmt);
            rs = pstmt.executeQuery();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // 단건 조회
    public <T> T queryForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
        List<T> result = query(sql, pss, rowMapper);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
