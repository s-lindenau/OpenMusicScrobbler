package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.util.FileFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationPropertiesTest {

    private ConfigurationProperties configurationProperties;
    private ByteArrayOutputStream outputStream;

    @Mock
    private Path configurationFilePath;

    @Mock
    private Path configurationDirectoryPath;

    @Mock
    private FileFacade fileFacade;

    @Mock
    private SystemProperties systemProperties;

    @BeforeEach
    void setUp() throws IOException {
        this.configurationProperties = new ConfigurationProperties(fileFacade, systemProperties);
        this.outputStream = new ByteArrayOutputStream();
        when(configurationDirectoryPath.resolve(anyString())).thenReturn(configurationFilePath);
        when(fileFacade.exists(any())).thenReturn(true);
        when(fileFacade.isDirectory(any())).thenReturn(true);
        when(fileFacade.newBufferedWriter(any())).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));
    }

    @Test
    void writeConfigurationProperties() throws IOException {
        Path createdFile = configurationProperties.writeConfigurationProperties(configurationDirectoryPath);
        Assertions.assertEquals(configurationFilePath, createdFile, "File not created in expected Path");
        Properties createdFileProperties = new Properties();
        createdFileProperties.load(new ByteArrayInputStream(outputStream.toByteArray()));
        Stream.of(ApplicationProperty.values()).forEach(applicationProperty -> assertPropertyExists(applicationProperty, createdFileProperties));
    }

    private void assertPropertyExists(ApplicationProperty applicationProperty, Properties asProperties) {
        String applicationPropertyKey = applicationProperty.getKey();
        String testFailedMessage = "ApplicationProperty not found in properties from file: " + applicationPropertyKey;
        Assertions.assertTrue(asProperties.containsKey(applicationPropertyKey), testFailedMessage);
    }
}