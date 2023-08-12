package com.example.demo.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;

@Repository("PaymentDao")
@Transactional
@Log4j2
public class PaymentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean getConnectionDB() throws SQLException {
        Boolean check = true;
        Connection conn = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
        } catch (Exception ex) {
            log.error("connect failure!: DB --- " + ex.getMessage());
            check = false;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }
}
