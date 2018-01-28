package com.thomastige.jiraawesomeificator.controllers.web;

import com.thomastige.jiraawesomeificator.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("persist")
public class PersistController {

    private static final String CONFIG_SAVE_DATA = "configurationData";

    Logger log = LoggerFactory.getLogger(PersistController.class);

    @Autowired
    private DataService dataService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(String key, String value) {
        log.info("Saving value " + value + " for key " + key);
        dataService.dispatchToSave(key, value);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ResponseEntity saveSetting(String key, String value) {
        log.info("Saving value " + value + " for key " + key);
        dataService.saveSetting(key, value);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity load(String key) {
        log.info("Loading value for " + key);
        String result = dataService.load(key);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(
            value="/settings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity load() {
        return load(CONFIG_SAVE_DATA);
    }

}
