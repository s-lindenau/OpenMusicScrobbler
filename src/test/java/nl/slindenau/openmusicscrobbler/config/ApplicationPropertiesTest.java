package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties;
    private Properties properties;

    @BeforeEach
    void setUp() {
        this.properties = new Properties();
        this.applicationProperties = new ApplicationProperties(properties);
    }

    @Test
    void testGetDefaultValues() {
        Assertions.assertFalse(applicationProperties.isDebugEnabled(), "Default value for Debug should be false");
        Assertions.assertEquals("info", applicationProperties.getLogLevel(), "Default value for Log Level should be info");
        Assertions.assertEquals(Duration.ofMinutes(1), applicationProperties.getDiscogsReadTimeout(), "Discogs read timeout default value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(10), applicationProperties.getDiscogsConnectionTimeout(), "Discogs connect timeout default value mismatch");
        Assertions.assertEquals(Duration.ofMinutes(4), applicationProperties.getDiscogsDefaultTrackLength(), "Discogs track length default value mismatch");
    }

    @Test
    void testGetOverrideDefaultValues() {
        setupProperty(ApplicationProperty.DEBUG, "true");
        setupProperty(ApplicationProperty.LOG_LEVEL, "debug");
        setupProperty(ApplicationProperty.DISCOGS_READ_TIMEOUT, "PT1S");
        setupProperty(ApplicationProperty.DISCOGS_CONNECTION_TIMEOUT, "PT2S");
        setupProperty(ApplicationProperty.DISCOGS_TRACK_LENGTH, "PT3S");

        Assertions.assertTrue(applicationProperties.isDebugEnabled(), "Debug value mismatch");
        Assertions.assertEquals("debug", applicationProperties.getLogLevel(), "Log Level value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(1), applicationProperties.getDiscogsReadTimeout(), "Discogs read timeout value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(2), applicationProperties.getDiscogsConnectionTimeout(), "Discogs connect timeout value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(3), applicationProperties.getDiscogsDefaultTrackLength(), "Discogs track length value mismatch");
    }

    @Test
    void testGetInvalidValues() {
        setupProperty(ApplicationProperty.DISCOGS_READ_TIMEOUT, "PT-1S");
        setupProperty(ApplicationProperty.DISCOGS_CONNECTION_TIMEOUT, "not a duration");

        assertThrows(() -> applicationProperties.getDiscogsReadTimeout(), "Expected exception on negative duration length");
        assertThrows(() -> applicationProperties.getDiscogsConnectionTimeout(), "Expected exception on invalid duration format");
    }

    @Test
    void testGetDefaultEmptyValues() {
        testEmptyValue(applicationProperties::getDiscogsUsername, "Discogs username");
        testEmptyValue(applicationProperties::getLastFmUsername, "LastFm username");
        testEmptyValue(applicationProperties::getLastFmPassword, "LastFm password");
        testEmptyValue(applicationProperties::getLastFmApiKey, "LastFm api key");
        testEmptyValue(applicationProperties::getLastFmApiSecret, "LastFm api secret");
    }

    private void testEmptyValue(Supplier<Object> getter, String message) {
        Assertions.assertNull(getter.get(), message + " property should not have a default value");
    }

    private void setupProperty(ApplicationProperty property, String value) {
        this.properties.put(property.getKey(), value);
    }

    private void assertThrows(Executable executable, String message) {
        Assertions.assertThrows(OpenMusicScrobblerException.class, executable, message);
    }
}