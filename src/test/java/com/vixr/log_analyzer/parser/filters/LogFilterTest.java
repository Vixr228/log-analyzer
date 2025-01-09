package com.vixr.log_analyzer.parser.filters;

import com.vixr.log_analyzer.config.ParserConfig;
import com.vixr.log_analyzer.parser.model.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LogFilterTest {

    @MockBean
    private ParserConfig parserConfig;

    @Autowired
    LogFilter logFilter;

    private List<LogEntry> logEntries;

    @BeforeEach
    public void setUp() {
        logEntries = new ArrayList<>( Arrays.asList(
            new LogEntry(LocalDateTime.now(), "INFO", "[org.apache.activemq.MainClass]", "Stop running"),
            new LogEntry(LocalDateTime.now(), "INFO", "[org.apache.activemq.MainClass]", "Start running"),
            new LogEntry(LocalDateTime.now(), "INFO", "[org.apache.activemq.MainClass]", "running"),
            new LogEntry(LocalDateTime.now(), "TRACE", "[org.apache.activemq.SecondClass]", "Pause running"),
            new LogEntry(LocalDateTime.now(), "TRACE", "[org.apache.activemq.Third]", "Pause running"),
            new LogEntry(LocalDateTime.now(), "ERROR", "[org.apache.activemq.MainClass]", "Error found"),
            new LogEntry(LocalDateTime.now(), "ERROR", "[org.apache.activemq.ThirdClass]", "Error found 2"),
            new LogEntry(LocalDateTime.now(), "ERROR", "[org.spring.all.Class]", "Error found 3")
        ));
    }

    @Test
    void testFilterByLevel_OneLevel() {
        when(parserConfig.getLevels()).thenReturn(Collections.singletonList("ERROR"));
        List<LogEntry> filtered  = logFilter.filterByLevel(logEntries);

        filtered.forEach(value -> {
            assertEquals("ERROR", value.getLevel());
        });
        assertEquals(filtered.size(), 3);
    }

    @Test
    void testFilterByLevel_ManyLevel() {
        when(parserConfig.getLevels()).thenReturn(Arrays.asList("ERROR", "INFO"));
        List<LogEntry> filtered  = logFilter.filterByLevel(logEntries);

        filtered.forEach(value -> {
            assertNotEquals("TRACE", value.getLevel());
        });
        assertEquals(filtered.size(), 6);
    }

    @Test
    void testFilterByLevel_EmptyLevel() {
        when(parserConfig.getLevels()).thenReturn(Collections.emptyList());
        List<LogEntry> filtered  = logFilter.filterByLevel(logEntries);

        assertEquals(filtered, logEntries);
        assertEquals(filtered.size(), 8);
    }

    @Test
    void testFilterByPattern_OnePattern() {
        when(parserConfig.getPatterns()).thenReturn(Collections.singletonList("Stop"));

        List<LogEntry> filtered  = logFilter.filterByPatterns(logEntries);

        filtered.forEach(value -> {
            assertTrue(value.toString().contains("Stop"));
        });
        assertEquals(filtered.size(), 1);
    }

    @Test
    void testFilterByPattern_ManyPattern() {
        when(parserConfig.getPatterns()).thenReturn(Arrays.asList("Stop", "Start"));
        List<LogEntry> filtered  = logFilter.filterByPatterns(logEntries);

        assertEquals(filtered.size(), 2);
    }

    @Test
    void testFilterByPattern_EmptyPattern() {
        when(parserConfig.getPatterns()).thenReturn(Collections.emptyList());
        List<LogEntry> filtered  = logFilter.filterByPatterns(logEntries);

        assertEquals(filtered, logEntries);
        assertEquals(filtered.size(), 8);
    }

    @Test
    void testFilter() {
        when(parserConfig.getLevels()).thenReturn(Collections.singletonList("INFO"));
        when(parserConfig.getPatterns()).thenReturn(Collections.singletonList("Stop"));

        List<LogEntry> filtered  = logFilter.filter(logEntries);

        filtered.forEach(value -> {
            assertEquals("INFO", value.getLevel());
            assertTrue(value.toString().contains("Stop"));
        });
        assertEquals(filtered.size(), 1);
    }

}
