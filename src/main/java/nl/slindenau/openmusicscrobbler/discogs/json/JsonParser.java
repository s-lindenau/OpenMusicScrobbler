package nl.slindenau.openmusicscrobbler.discogs.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class JsonParser {

    public <T extends DiscogsApiResponse> T parseJsonObject(String input, Class<T> modelClass) {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        jsonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return jsonObjectMapper.readValue(input, modelClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
