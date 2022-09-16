package nl.slindenau.openmusicscrobbler.discogs.model;

import nl.slindenau.openmusicscrobbler.discogs.model.collection.BasicInformation;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Format;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsReleaseFormatCollector {

    private static final String UNKNOWN_FORMAT = "unknown format";
    private static final String FORMAT_SEPARATOR = ", ";
    private static final String TIMES = "x";
    private static final String N_TIMES_FORMAT_Y = "%s" + TIMES + "%s";

    public String getFormat(BasicInformation basicInformation) {
        List<Format> formats = basicInformation.getFormats();
        if (formats.isEmpty()) {
            return UNKNOWN_FORMAT;
        }
        return getFormatText(formats);
    }

    private String getFormatText(List<Format> formats) {
        return formats.stream()
                .map(this::getSingleFormatCountText)
                .collect(Collectors.joining(FORMAT_SEPARATOR));
    }

    private String getSingleFormatCountText(Format format) {
        String formatName = format.getName();
        long count = Long.parseLong(format.getQty());
        if (count > 1) {
            return String.format(N_TIMES_FORMAT_Y, count, formatName);
        } else {
            return formatName;
        }
    }
}
