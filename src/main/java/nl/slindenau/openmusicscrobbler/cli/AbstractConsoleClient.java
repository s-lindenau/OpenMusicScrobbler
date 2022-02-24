package nl.slindenau.openmusicscrobbler.cli;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public abstract class AbstractConsoleClient {

    private final TextIO textIO = TextIoFactory.getTextIO();
    private final TextTerminal<?> terminal = textIO.getTextTerminal();

    public abstract void run();

    protected void printEmptyLine() {
        terminal.println();
    }

    protected void printLine(String message) {
        terminal.println(message);
    }

    protected Integer readConsoleNumberInput(String message) {
        return textIO.newIntInputReader().read(message);
    }

    protected String readConsoleTextInput(String message) {
        return textIO.newStringInputReader().read(message);
    }

    protected String readConsoleTextInput(String message, int minimalInputLength) {
        return textIO.newStringInputReader().withMinLength(minimalInputLength).read(message);
    }

    protected String readConsolePasswordInput(String message) {
        return textIO.newStringInputReader().withInputMasking(true).read(message);
    }

    protected void closeConsoleClient() {
        textIO.dispose();
    }
}
