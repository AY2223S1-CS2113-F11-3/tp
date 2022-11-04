package seedu.duke.utils;

import seedu.duke.exceptions.YamomException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Handles the saving and loading of states.
 */
public class Storage {
    public static final String NO_PREVIOUS_STATE_ERROR_MESSAGE = "There was no previously saved state.";

    public static final String LOADING_PREVIOUS_STATE_MESSAGE = "Loading previous state.";

    public static final String NO_PREVIOUS_SAVED_MODULE_ERROR_MESSAGE = "There are no modules saved.";

    public static final String EXPORT_MESSAGE = "These are your export links:";

    public static final String PREFERENCES_FLUSH_ERROR_MESSAGE = "Saving cannot be completed due to a failure in "
            + "the backing store, or inability to communicate with it.";

    private Logger logger = Logger.getLogger(Storage.class.getName());

    public static final String SUBSYSTEM_NAME = "storage";

    private Preferences prefs;

    public Storage() {
        this.prefs = Preferences.userRoot().node(this.getClass().getName());
    }

    public Preferences getPrefs() {
        return prefs;
    }

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
            ArrayList<String> links = readPreviousState();
            for (String link: links) {
                Link.parseLink(link, state, ui);
                if (Link.isEmptyLink(link)) {
                    ui.addMessage(NO_PREVIOUS_SAVED_MODULE_ERROR_MESSAGE);
                }
            }
            state.setSemester(1);
            ui.addMessage(LOADING_PREVIOUS_STATE_MESSAGE);
            logger.log(Level.FINE, "Opened previous saved file");
        } catch (FileNotFoundException e) {
            ui.addMessage(NO_PREVIOUS_STATE_ERROR_MESSAGE);
            logger.log(Level.FINE, "No previous saved file");
        } catch (YamomException e) {
            ui.addMessage(e.getMessage());
            logger.log(Level.FINE, e.getMessage());
        }
        ui.displayUi();
    }

    /**
     * Opens the previously saved file. The saved file should only contain one line which is the
     * link for exporting to NUSMods.
     *
     * @return the links for exporting to NUSMods
     * @throws FileNotFoundException the file in the file path cannot be found
     */
    private ArrayList<String> readPreviousState() throws FileNotFoundException {
        ArrayList<String> links = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String semester = "sem-" + i;
            String link = prefs.get(semester, "");
            if (Link.isValidLink(link)) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * Saves all current selected modules for all semesters by overriding the file.
     *
     * @param state the current state of the application
     * @param ui    to output to the user
     * @throws IOException failed or interrupted I/O operations
     */
    public void saveState(State state, Ui ui, boolean isExit) throws IOException {
        assert state != null : "State should not be null";
        logger = Logger.getLogger(SUBSYSTEM_NAME);
        logger.log(Level.FINE, "Saving current state with " + state.getSelectedModulesList().size()
            + " modules into a file. The format will be NUSMods export link.");
        ui.addMessage(EXPORT_MESSAGE);
        int currSem = state.getSemester();
        for (int i = 1; i <= 4; i++) {
            state.setSemester(i);
            String semester = "sem-" + i;
            String toSave = Link.getLink(state);
            ui.addMessage(toSave);
            prefs.put(semester, toSave);
        }
        state.setSemester(currSem);
        if (!isExit) {
            ui.clearUiBuffer();
        }
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            ui.addMessage(PREFERENCES_FLUSH_ERROR_MESSAGE);
        }
        ui.displayUi();
    }
}
