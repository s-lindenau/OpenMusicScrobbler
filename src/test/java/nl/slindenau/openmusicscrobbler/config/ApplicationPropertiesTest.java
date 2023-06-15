package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties;
    private Properties properties;
    private Properties overrideProperties;

    @BeforeEach
    void setUp() {
        this.properties = new Properties();
        this.overrideProperties = new Properties();
        this.applicationProperties = new ApplicationProperties(properties, overrideProperties);
    }

    @Test
    void testGetDefaultValues() {
        Assertions.assertFalse(applicationProperties.isDebugEnabled(), "Default value for Debug should be false");
        Assertions.assertEquals("info", applicationProperties.getLogLevel(), "Default value for Log Level should be info");
        Assertions.assertEquals(Duration.ofMinutes(1), applicationProperties.getDiscogsReadTimeout(), "Discogs read timeout default value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(10), applicationProperties.getDiscogsConnectionTimeout(), "Discogs connect timeout default value mismatch");
        Assertions.assertEquals(Duration.ofMinutes(1), applicationProperties.getDiscogsDefaultTrackLength(), "Discogs track length default value mismatch");
    }

    @Test
    void testGetChangedDefaultValues() {
        setupProperty(ApplicationProperty.DEBUG, "true");
        setupProperty(ApplicationProperty.LOG_LEVEL, "debug");
        setupProperty(ApplicationProperty.DISCOGS_READ_TIMEOUT, "PT1S");
        setupProperty(ApplicationProperty.DISCOGS_CONNECTION_TIMEOUT, "PT2S");
        setupProperty(ApplicationProperty.DISCOGS_TRACK_LENGTH, "PT3S");
        setupProperty(ApplicationProperty.DISCOGS_TOKEN, "personal-token");

        Assertions.assertTrue(applicationProperties.isDebugEnabled(), "Debug value mismatch");
        Assertions.assertEquals("debug", applicationProperties.getLogLevel(), "Log Level value mismatch");
        Assertions.assertEquals(Optional.of("personal-token"), applicationProperties.getDiscogsPersonalAccessToken(), "Discogs personal token value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(1), applicationProperties.getDiscogsReadTimeout(), "Discogs read timeout value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(2), applicationProperties.getDiscogsConnectionTimeout(), "Discogs connect timeout value mismatch");
        Assertions.assertEquals(Duration.ofSeconds(3), applicationProperties.getDiscogsDefaultTrackLength(), "Discogs track length value mismatch");
    }

    @Test
    void testOverrideProperties () {
        setupProperty(ApplicationProperty.LOG_LEVEL, "debug");
        setupOverrideProperty(ApplicationProperty.LOG_LEVEL, "warn");
        setupOverrideProperty(ApplicationProperty.LAST_FM_API_SECRET, "secret");
        Assertions.assertEquals("warn", applicationProperties.getLogLevel(), "Log Level value mismatch");
        Assertions.assertEquals("secret", applicationProperties.getLastFmApiSecret(), "LastFm api secret value mismatch");
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

    @Test
    void testGetDefaultEmptyOptionalValues() {
        testEmptyOptionalValue(applicationProperties::getDiscogsPersonalAccessToken, "Discogs personal token");
    }

    @Test
    void testGetAsProperties() {
        Properties asProperties = applicationProperties.getAsProperties();
        Stream.of(ApplicationProperty.values()).forEach(applicationProperty -> assertPropertyExists(applicationProperty, asProperties));
    }

    private void assertPropertyExists(ApplicationProperty applicationProperty, Properties asProperties) {
        String applicationPropertyKey = applicationProperty.getKey();
        Assertions.assertTrue(asProperties.containsKey(applicationPropertyKey), "ApplicationProperty not found in properties: " + applicationPropertyKey);
    }

    private void testEmptyOptionalValue(Supplier<Optional<?>> getter, String message) {
        Assertions.assertEquals(Optional.empty(), getter.get(), message + " property should not have a default value");
    }

    private void testEmptyValue(Supplier<Object> getter, String message) {
        Assertions.assertNull(getter.get(), message + " property should not have a default value");
    }

    private void setupProperty(ApplicationProperty property, String value) {
        this.properties.put(property.getKey(), value);
    }

    private void setupOverrideProperty(ApplicationProperty property, String value) {
        this.overrideProperties.put(property.getKey(), value);
    }

    private void assertThrows(Executable executable, String message) {
        Assertions.assertThrows(OpenMusicScrobblerException.class, executable, message);
    }
}