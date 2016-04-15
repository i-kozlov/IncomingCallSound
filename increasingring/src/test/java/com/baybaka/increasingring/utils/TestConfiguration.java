package com.baybaka.increasingring.utils;

import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;

public class TestConfiguration {

    public static Logger createAndConfigureMockLogger() {
        Logger mockLogger = mock(Logger.class);

        PowerMockito.doAnswer(invocation -> {
            System.out.println(Arrays.toString(invocation.getArguments()));
            return null;
        }).when(mockLogger).debug(anyString(), anyObject(), anyObject());
        PowerMockito.doAnswer(invocation -> {
            System.out.println(Arrays.toString(invocation.getArguments()));
            return null;
        }).when(mockLogger).info(anyString());


        PowerMockito.doAnswer(invocation -> {
            System.out.println(Arrays.toString(invocation.getArguments()));
            return null;
        }).when(mockLogger).info(anyString(), anyObject(), anyObject());

        return mockLogger;
    }
}
