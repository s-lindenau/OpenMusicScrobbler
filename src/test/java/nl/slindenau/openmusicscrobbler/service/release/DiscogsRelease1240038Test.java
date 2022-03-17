package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease1240038Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Various";
    private static final String ALBUM = "The Best Of Goa Trance 2";

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("1-1", "Lasertrancer", "Mental Flypan", "5:43", 343),
            new Track("1-2", "Color Dox", "Goa Booster", "4:33", 273),
            new Track("1-3", "DJ De Moor", "Wonderful Silence", "7:30", 450),
            new Track("1-4", "Gigamorphosys", "Way To Harmonization", "4:03", 243),
            new Track("1-5", "Psygone", "Mm-Wave", "7:42", 462),
            new Track("1-6", "Glasswhisper", "Psychy Goa Gate", "6:21", 381),
            new Track("1-7", "Pascal Hagen", "L.F.O. Morphosys", "6:35", 395),
            new Track("1-8", "Ingmar Veeck", "Tube Spirit", "4:50", 290),
            new Track("1-9", "DJ Alex", "Hypnotic Balance", "10:52", 652),
            new Track("1-10", "Synchron", "Future Fantasy", "4:25", 265),
            new Track("1-11", "Alysium", "Genius Divinity", "5:02", 302),
            new Track("2-1", "Cyber Spy", "Magic Trip", "6:49", 409),
            new Track("2-2", "Silvio Morley", "Dual Dynamic Insertion (Psy Remix)", "9:00", 540),
            new Track("2-3", "Ingmar Jonesy", "Fast Psy Stuff", "7:21", 441),
            new Track("2-4", "Scobee", "Astral Flow", "8:05", 485),
            new Track("2-5", "Hi-Tune", "Aural Structure", "8:23", 503),
            new Track("2-6", "Sphyrinx", "Dicision Of Life", "4:30", 270),
            new Track("2-7", "I-The Magican", "Slow Ambience", "7:15", 435),
            new Track("2-8", "Psychedelic Tranceforce", "Tryptophan", "6:39", 399),
            new Track("2-9", "L.A.S.E.R.-Ray", "Hard Warm Distortion", "6:52", 412),
            new Track("2-10", "Jigsaw", "Constant Punch", "6:11", 371),
            new Track("2-11", "Enfusia", "Floating Space", "7:01", 421),
            new Track("3-1", "Aminate Fx", "Wisdom & Stamina", "6:56", 416),
            new Track("3-2", "Robert McDowell", "Cyclic Progression", "8:06", 486),
            new Track("3-3", "Psychotic", "Futuristic Timewave", "6:25", 385),
            new Track("3-4", "L'Ange Gabriella", "Sunny Slide Side", "10:11", 611),
            new Track("3-5", "Pascal Hagen", "Subdued Power", "7:00", 420),
            new Track("3-6", "No-Xs", "Mystic New System", "7:20", 440),
            new Track("3-7", "Mark Nickelson", "Tribal Earth", "7:35", 455),
            new Track("3-8", "Psycos", "Cristzlization", "5:00", 300),
            new Track("3-9", "Xenos", "China Girl", "6:27", 387),
            new Track("3-10", "Tim Mark", "5th Re-Incarnation", "6:33", 393),
            new Track("3-11", "SFX", "Deep Space Consciousness", "8:01", 481)
    );

    @Override
    protected String getReleaseFileName() {
        return "release-1240038.json";
    }

    @Override
    protected List<Track> getExpectedTracksInRelease() {
        return EXPECTED_TRACKS_IN_RELEASE;
    }

    @Override
    protected String getReleaseArtist() {
        return ARTIST;
    }

    @Override
    protected String getReleaseTitle() {
        return ALBUM;
    }
}