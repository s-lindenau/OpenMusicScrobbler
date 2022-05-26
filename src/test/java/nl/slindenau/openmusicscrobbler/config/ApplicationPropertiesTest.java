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
        Assertions.assertEquals(Duration.ofMinutes(1), applicationProperties.getDiscogsReadTimeout(), "Discogs read timeout default value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(10), applicationProperties.getDiscogsConnectionTimeout(), "Discogs connect timeout default value mismatch");
        Assertions.assertEquals(Duration.ofMinutes(4), applicationProperties.getDiscogsDefaultTrackLength(), "Discogs track length default value mismatch");
    }

    @Test
    void testGetOverrideDefaultValues() {
        setupProperty("oms.debug", "true");
        setupProperty("discogs.read.timeout", "PT1S");
        setupProperty("discogs.connection.timeout", "PT2S");
        setupProperty("discogs.track.length.default", "PT3S");

        Assertions.assertTrue(applicationProperties.isDebugEnabled(), "Debug value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(1), applicationProperties.getDiscogsReadTimeout(), "Discogs read timeout value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(2), applicationProperties.getDiscogsConnectionTimeout(), "Discogs connect timeout value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(3), applicationProperties.getDiscogsDefaultTrackLength(), "Discogs track length value mismatch");
    }

    @Test
    void testGetInvalidValues() {
        setupProperty("discogs.read.timeout", "PT-1S");
        setupProperty("discogs.connection.timeout", "not a duration");

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

    private void setupProperty(String key, String value) {
        this.properties.put(key, value);
    }

    private void assertThrows(Executable executable, String message) {
        Assertions.assertThrows(OpenMusicScrobblerException.class, executable, message);
    }
}