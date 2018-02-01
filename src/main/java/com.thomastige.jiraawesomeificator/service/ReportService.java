package com.thomastige.jiraawesomeificator.service;

import com.thomastige.jiraawesomeificator.legacy.adt.BugEntry;
import com.thomastige.jiraawesomeificator.legacy.parser.FolderParser;
import com.thomastige.jiraawesomeificator.legacy.tablebuilder.TableBuilder;
import com.thomastige.jiraawesomeificator.providers.ReportDataProvider;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private static final String PREFIX_SEPARATOR = "__";

    private static final boolean DEBUG_DISABLE_CLEANUP = false;

    @Value("${jai.tempfolder}")
    private String TEMP_FOLDER;

    private String TEMP_NOTES_FOLDER_SUFFIX =  File.separator + "notes";
    private String TEMP_ZIP_FOLDER_SUFFIX = File.separator + "zip";

    @Autowired
    private ReportDataProvider reportDataProvider;

    public String getCalendarReport() {
        TableBuilder tableBuilder = new TableBuilder();
        List<BugEntry> bugEntries = convertDataToBugEntries();
        String result = tableBuilder.buildCalendarLog(bugEntries);
        return result;
    }


    public String getHoursReport() {
        TableBuilder tableBuilder = new TableBuilder();
        String result = tableBuilder.buildHoursLog(convertDataToBugEntries());
        return result;
    }

    public String getMetricsReport() throws IOException {
        String result;
        TableBuilder tb = new TableBuilder(TEMP_FOLDER + TEMP_NOTES_FOLDER_SUFFIX);
        List<BugEntry> bugEntries = convertDataToBugEntries(false);
        result = tb.buildMetricsLog(bugEntries);
        cleanup();
        return result;
    }

    public File getNoteArchive(){
        convertDataToBugEntries(false);
        File zip = new File(TEMP_FOLDER + TEMP_ZIP_FOLDER_SUFFIX + File.separator + "notes" + System.currentTimeMillis() + ".zip");
        zip.getParentFile().mkdirs();
        ZipUtil.pack(new File(TEMP_FOLDER + TEMP_NOTES_FOLDER_SUFFIX), zip);
        try {
            FileUtils.cleanDirectory(new File(TEMP_FOLDER + TEMP_NOTES_FOLDER_SUFFIX));
        } catch (IOException e) {
            log.error("ERROR WHEN DELETING LOCAL TEMP DIRECTORY + ", e);
        }
        return zip;
    }

    public void cleanup() {
        if (!DEBUG_DISABLE_CLEANUP) {
            try {
                FileUtils.cleanDirectory(new File(TEMP_FOLDER));
            } catch (IOException e) {
                log.error("ERROR WHEN DELETING LOCAL TEMP DIRECTORY + ", e);
            }
        }
    }

    private List<BugEntry> convertDataToBugEntries() {
        return convertDataToBugEntries(true);
    }

    private List<BugEntry> convertDataToBugEntries(boolean cleanup) {
        List<BugEntry> result = new ArrayList<>();
        List<String> notes = reportDataProvider.getBugNoteEntries();
        FolderParser folderParser = new FolderParser(TEMP_FOLDER + TEMP_NOTES_FOLDER_SUFFIX);
        try {
            for (String note : notes) {
                String fileLoc = makeNoteFile(note);
            }
            result = folderParser.parseToList();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (cleanup) {
                cleanup();
            }
        }
        return result;
    }

    private String makeNoteFile(String note) throws IOException {
        String fileName = note.substring(0, note.indexOf("|"));
        String[] splitParts = fileName.split(PREFIX_SEPARATOR);

        String location = TEMP_FOLDER + TEMP_NOTES_FOLDER_SUFFIX + File.separator + splitParts[1] + File.separator + splitParts[2].replaceAll("[^a-zA-Z0-9\\.\\- ]", "");
        File file = new File(location);
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write(note.substring(note.indexOf("|")+1));
        fw.flush();
        fw.close();
        return file.getAbsolutePath();
    }


}
