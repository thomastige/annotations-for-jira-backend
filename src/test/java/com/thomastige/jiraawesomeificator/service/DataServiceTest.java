package com.thomastige.jiraawesomeificator.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


public class DataServiceTest {

    public static final String LOAD_VALUES_PROVIDER = "loadValuesProvider";

    @InjectMocks
    private DataService dataService;

    @Mock
    private com.thomastige.jiraawesomeificator.providers.DataProvider dataProvider;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }


    @DataProvider(name = LOAD_VALUES_PROVIDER)
    public Object[][] loadValuesProvider() {
        return new Object[][]{
                {
                        "test", "test", "test"
                },{
                        "test-notePad", "test|[]\ntest2", "test2"
                },{
                        "test-notePad", "test|test2", "test2"
                },{
                        "test-notePad", "test", "test"
                }
        };
    }

    @Test(dataProvider = LOAD_VALUES_PROVIDER)
    public void loadTest(String key, String providerReturn ,String expectedValue) {
        when(dataProvider.load(anyString())).thenReturn(providerReturn);
        String value = dataService.load(key);
        Assert.assertEquals(expectedValue, value);
    }

}
