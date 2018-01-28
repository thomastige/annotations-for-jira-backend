package com.thomastige.jiraawesomeificator.service;

import com.thomastige.jiraawesomeificator.model.dto.enums.DataType;
import com.thomastige.jiraawesomeificator.providers.DataProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import static com.thomastige.jiraawesomeificator.model.dto.enums.DataType.DEFAULT;
import static com.thomastige.jiraawesomeificator.model.dto.enums.DataType.NOTEPAD;

@Service
public class DataService {

    @Resource
    DataProvider dataProvider;

    public void dispatchToSave(String key, String value) {
        dataProvider.save(key, value);
    }

    public void saveSetting(String key, String value) {
        dataProvider.save(key, value);
    }

    public String load(String key) {
        DataType category = getCategory(key);
        String value = null;
        switch (category) {
            case NOTEPAD:
                value = dataProvider.load(key);
                if (!StringUtils.isEmpty(value)) {
                    int index = value.indexOf("|[");
                    if (index == -1) {
                        index = value.indexOf("|");
                        if (index > -1) {
                            value = value.substring(index + 1).trim();
                        }
                    } else {
                        index = value.indexOf("\n");
                        value = value.substring(index).trim();
                    }
                }
                break;
            case DEFAULT:
                value = dataProvider.load(key);
                break;
        }
        return value;
    }

    private DataType getCategory(String key) {
        DataType result = DEFAULT;
        if (key.endsWith("notePad")) {
            result = NOTEPAD;
        }
        return result;
    }


}
