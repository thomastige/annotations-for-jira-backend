package com.thomastige.jiraawesomeificator.providers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportDataProvider {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String findAllBugNoteEntries = "SELECT STORED_VALUE FROM SAVED_DATA WHERE ID LIKE '%notePad'";

    public List<String> getBugNoteEntries(){
        List<String> result = jdbcTemplate.queryForList(findAllBugNoteEntries, String.class);
        return result;
    }
}
