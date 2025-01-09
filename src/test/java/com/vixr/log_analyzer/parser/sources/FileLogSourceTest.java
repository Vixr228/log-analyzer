package com.vixr.log_analyzer.parser.sources;

import com.vixr.log_analyzer.config.LogConfig;
import com.vixr.log_analyzer.parser.model.LogEntry;
import com.vixr.log_analyzer.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class FileLogSourceTest {

    @MockBean
    private LogConfig logConfig;

    @Autowired
    private FileLogSource fileLogSource;

    @MockBean
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        when(logConfig.getFilePath()).thenReturn("src/test/resources/test_short.log");
    }


    @Test
    void testFetchLogs_ValidLogs() {
        List<LogEntry> logEntries = fileLogSource.fetchLogs();

        assertNotNull(logEntries, "logEntries should not be null");
        assertEquals(13, logEntries.size());

        LogEntry logEntry = logEntries.getFirst();
        assertEquals("INFO", logEntry.getLevel());
        assertEquals("[org.apache.activemq.broker.BrokerService]", logEntry.getSource());
        assertEquals("[RMITCP Connection(3)-127.0.0.1] Using Persistence Adapter: KahaDBPersistenceAdapter\n", logEntry.getMessage());


        logEntries.forEach(System.out::println);

        verify(logRepository, times(13)).save(Mockito.any(LogEntry.class));

    }

    @Test
    void testFetchLogs_NoFile() {
       when(logConfig.getFilePath()).thenReturn(null);

        List<LogEntry> logEntries = fileLogSource.fetchLogs();

        assertNotNull(logEntries, "logEntries should not be null");
        assertTrue(logEntries.isEmpty(), "logEntries should be empty");
        verifyNoInteractions(logRepository);
    }

    @Test
    void testFetchLogs_EmptyFile() {
        when(logConfig.getFilePath()).thenReturn("src/test/resources/test_empty.log");

        List<LogEntry> logEntries = fileLogSource.fetchLogs();

        assertNotNull(logEntries, "logEntries should not be null");
        assertTrue(logEntries.isEmpty(), "logEntries should be empty");
        verifyNoInteractions(logRepository);
    }

    //TODO CHECK
    @Test
    void testFetchLogs_InvaliFormat() {
        when(logConfig.getFilePath()).thenReturn("src/test/resources/test_invalid_format.log");

        List<LogEntry> logEntries = fileLogSource.fetchLogs();
        assertNotNull(logEntries, "logEntries should not be null");
        assertTrue(logEntries.isEmpty(), "logEntries should be empty");
        verifyNoInteractions(logRepository);
    }
}
