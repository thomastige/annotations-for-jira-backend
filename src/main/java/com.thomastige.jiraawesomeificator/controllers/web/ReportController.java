package com.thomastige.jiraawesomeificator.controllers.web;

import com.thomastige.jiraawesomeificator.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("report")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "calendar", method = RequestMethod.GET)
    public ResponseEntity getCalendarReport(){
        String result = reportService.getCalendarReport();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "hours", method = RequestMethod.GET)
    public ResponseEntity getHoursReport(){
        String result = reportService.getHoursReport();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "metrics", method = RequestMethod.GET)
    public ResponseEntity getMetricsReport(){
        HttpStatus status = HttpStatus.OK;
        String result = null;
        try {
            result = reportService.getMetricsReport();
        } catch (IOException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Error when generating metrics report", e);
        }
        return ResponseEntity.status(status).body(result);
    }


}
