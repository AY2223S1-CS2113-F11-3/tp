package seedu.duke.utils;

import seedu.duke.exceptions.YamomException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the saving and loading of states.
 */
public class Storage {
    public static final String FILE_PATH = "data/duke-sem-";

    public static final String EXTENSION = ".txt";

    public static final String NO_PREVIOUS_STATE_ERROR_MESSAGE = "There was no previously saved state.";

    public static final String LOADING_PREVIOUS_STATE_MESSAGE = "Loading previous state.";

    public static final String NO_PREVIOUS_SAVED_MODULE_ERROR_MESSAGE = "There are no modules saved";

    public static final String EXPORT_MESSAGE = "This is your export link:";

    private Logger logger = Logger.getLogger(Storage.class.getName());

    public static final String SUBSYSTEM_NAME = "storage";

    /**
     * Tries to open the file containing the previously saved state from specified file path.
     * Then outputs to the user if the opening of file was successful.
     *
     * @param state current state of the application to be saved
     * @param ui    to output to the user
     */
    public void openPreviousState(State state, Ui ui) {
        assert state != null : "List of lessons should not be null";
        logger = Logger.getLogger(SUBSYSTEM_NAME);
        logger.log(Level.FINE, "Attempting to open previous saved file");
        try {
            for (int i = 1; i < 5; i++) {
                String link = readPreviousState(i);
                Link.parseLink(link, state);
            }
            state.setSemester(1);
            ui.addMessage(LOADING_PREVIOUS_STATE_MESSAGE);
            logger.log(Level.FINE, "Opened previous saved file");
        } catch (FileNotFoundException e) {
            ui.addMessage(NO_PREVIOUS_STATE_ERROR_MESSAGE);
            logger.log(Level.FINE, "No previous saved file");
        } catch (YamomException e) {
            ui.addMessage(NO_PREVIOUS_SAVED_MODULE_ERROR_MESSAGE);
            logger.log(Level.FINE, "No previous saved modules");
        }
        ui.displayUi();
    }

    /**
     * Opens the previously saved file. The saved file should only contain one line which is the
     * link for exporting to NUSMods.
     *
     * @param semester the semester file to look into
     * @return the link for exporting to NUSMods
     * @throws FileNotFoundException the file in the file path cannot be found
     */
    private String readPreviousState(int semester) throws FileNotFoundException {
        String link = "";
        File file = new File(FILE_PATH + semester + EXTENSION);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.isEmpty() && Link.isValidPreviousState(line)) {
                link = line;
                break;
            }
        }
        scanner.close();
        return link;
    }

    /**
     * Saves all current tasks by overriding the file.
     *
     * @param state the current state of the application
     * @param ui    to output to the user
     * @throws IOException failed or interrupted I/O operations
     */
    public void saveState(State state, Ui ui) throws IOException {
        assert state != null : "State should not be null";
        logger = Logger.getLogger(SUBSYSTEM_NAME);
        logger.log(Level.FINE, "Saving current state with " + state.getSelectedModulesList().size()
                + " modules into a file. The format will be NUSMods export link.");
        ui.addMessage(EXPORT_MESSAGE);
        for (int i = 1; i < 5; i++) {
            File file = new File(FILE_PATH + i + EXTENSION);
            file.getParentFile().mkdirs();
            file.createNewFile();

            state.setSemester(i);
            String toSave = Link.getLink(state);
            ui.addMessage(toSave);

            FileWriter fw = new FileWriter(file);
            fw.write(String.valueOf(toSave));
            fw.close();
        }
        ui.displayUi();
    }
}
