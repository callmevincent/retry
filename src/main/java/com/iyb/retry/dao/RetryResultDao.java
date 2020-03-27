package com.iyb.retry.dao;

import com.iyb.retry.bean.RetryResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author 2020/3/26 18:10 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
@Repository
public class RetryResultDao {
    @Autowired
    private JdbcTemplate jdbc;

    private static String insertSql = "INSERT INTO `vie_data`.`retry_result`" +
            "(`request_id`, `clazz`, `method`, `status`, `retry_times`, `error_msg`, `result`, `gmt_created`)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, now());";

    public int insert(RetryResult retryResult) {
        return jdbc.execute(insertSql, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(@NotNull PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, retryResult.getRequestId());
                ps.setString(2, retryResult.getClazz());
                ps.setString(3, retryResult.getMethod());
                ps.setString(4, retryResult.getStatus());
                ps.setInt(5, retryResult.getRetryTimes());
                ps.setString(6, retryResult.getErrorMsg());
                ps.setString(7, retryResult.getResult());
                return ps.executeUpdate();
            }
        });
    }
}
