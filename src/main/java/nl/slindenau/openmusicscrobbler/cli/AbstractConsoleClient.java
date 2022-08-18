package nl.slindenau.openmusicscrobbler.cli;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public abstract class AbstractConsoleClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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

    protected String readConsoleOptionalTextInput(String message) {
        return textIO.newStringInputReader().withMinLength(0).read(message);
    }

    protected String readConsolePasswordInput(String message) {
        return textIO.newStringInputReader().withMinLength(0).withInputMasking(true).read(message);
    }

    protected void closeConsoleClient() {
        textIO.dispose();
    }

    protected Logger getLogger() {
        return logger;
    }
}
