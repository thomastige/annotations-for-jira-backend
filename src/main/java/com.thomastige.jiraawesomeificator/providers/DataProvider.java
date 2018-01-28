package com.thomastige.jiraawesomeificator.providers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataProvider {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String preparedInsert = "" +
            "DELETE SAVED_DATA WHERE ID = '?'; " +
            "insert into SAVED_DATA(ID, STORED_VALUE) values('?','?');" +
            "";
    private static final String preparedLoad = "select STORED_VALUE from SAVED_DATA where ID = '?'";

    public void save(String key, String value){
        jdbcTemplate.execute(preparedInsert
                .replaceFirst("\\?", key.replaceAll("'", "''"))
                .replaceFirst("\\?", key.replaceAll("'", "''"))
                .replaceFirst("\\?", value.replaceAll("'", "''"))
        );
    }

    public String load(String key) {
        List<String> resultList = jdbcTemplate.query(preparedLoad.replaceFirst("\\?", key), new RowMapper(){

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        String result = null;
        if (resultList.size() == 1) {
            result = resultList.get(0);
        }
        return result;
    }

}
