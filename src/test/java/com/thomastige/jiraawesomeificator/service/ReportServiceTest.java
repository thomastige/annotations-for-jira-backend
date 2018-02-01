package com.thomastige.jiraawesomeificator.service;

import com.thomastige.jiraawesomeificator.providers.ReportDataProvider;
import org.apache.commons.io.IOUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@TestPropertySource(locations="classpath:application.properties")
@SpringBootTest
public class ReportServiceTest {

    @Mock
    private ReportDataProvider reportDataProvider;

    @InjectMocks
    private ReportService reportService;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(reportService, "TEMP_FOLDER", "C:\\temp\\JAI_TEST");
    }

    @Test
    public void testCalendarReport(){
        when(reportDataProvider.getBugNoteEntries()).thenReturn(Arrays.asList(getText()));
        String report = reportService.getCalendarReport();
        InputStream inputStream = this.getClass().getResourceAsStream("ReportService/calendar_report.txt");
        String expectedReport = null;
        try {
            expectedReport = IOUtils.toString(inputStream, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(report, expectedReport);
    }

    @Test
    public void testHoursReport(){
        when(reportDataProvider.getBugNoteEntries()).thenReturn(Arrays.asList(getText()));
        String report = reportService.getHoursReport();
        InputStream inputStream = this.getClass().getResourceAsStream("ReportService/hours_report.txt");
        String expectedReport = null;
        try {
            expectedReport = IOUtils.toString(inputStream, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(report, expectedReport);
    }

    @Test
    public void testMetricsReport(){
        when(reportDataProvider.getBugNoteEntries()).thenReturn(Arrays.asList(getText()));
        try {
            String report = reportService.getMetricsReport();
            InputStream inputStream = this.getClass().getResourceAsStream("ReportService/metrics_report.txt");
            String expectedReport = null;
            expectedReport = IOUtils.toString(inputStream, Charset.defaultCharset());
            Assert.assertEquals(report, expectedReport);
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();
        }
    }

    private String[] getText(){
        String[] result = null;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("ReportService/notes.txt");
            result = IOUtils.toString(inputStream, Charset.defaultCharset()).split(":\\$:\\$:\\$");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
