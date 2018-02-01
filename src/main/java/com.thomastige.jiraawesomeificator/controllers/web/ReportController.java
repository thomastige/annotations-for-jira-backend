package com.thomastige.jiraawesomeificator.controllers.web;

import com.thomastige.jiraawesomeificator.service.ReportService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("report")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "calendar", method = RequestMethod.GET)
    public ResponseEntity getCalendarReport() {
        String result = reportService.getCalendarReport();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "hours", method = RequestMethod.GET)
    public ResponseEntity getHoursReport() {
        String result = reportService.getHoursReport();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "metrics", method = RequestMethod.GET)
    public ResponseEntity getMetricsReport() {
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

    @RequestMapping(
            value = "notes",
            method = RequestMethod.GET
//            ,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public void getNoteArchive( HttpServletResponse response) {
        File file = reportService.getNoteArchive();
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(file);
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. Filename was '{}'", file.getName(), ex);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

}
