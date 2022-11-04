package seedu.duke.utils;

import org.junit.jupiter.api.Test;
import seedu.duke.model.LessonType;
import seedu.duke.model.Module;
import seedu.duke.model.SelectedModule;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    @Test
    public void saveState_state_outputFile() throws IOException {
        //Save state into file
        State state = new State();
        int semester = 1;
        state.setSemester(semester);
        SelectedModule selectedModule1 = new SelectedModule(Module.get("CS1010"), semester);
        selectedModule1.selectSlot(LessonType.TUTORIAL, "01");
        selectedModule1.selectSlot(LessonType.SECTIONAL_TEACHING, "1");
        state.addSelectedModule(selectedModule1);
        SelectedModule selectedModule2 = new SelectedModule(Module.get("CS2113"), semester);
        selectedModule2.selectSlot(LessonType.LECTURE, "1");
        selectedModule2.selectSlot(LessonType.TUTORIAL, "4");
        state.addSelectedModule(selectedModule2);
        Storage storage = new Storage();
        Ui ui = new Ui();
        storage.saveState(state, ui, true);

        //Read saved state from saved file
        StringBuilder actualString = new StringBuilder();
        Preferences prefs = storage.getPrefs();
        for (int i = 1; i <= 4; i++) {
            String semesterKey = "sem-" + i;
            String link = prefs.get(semesterKey, "");
            actualString.append(link);
        }

        //Read state from expected file
        File expectedFile = new File("src/test/resources/saveCs1010.txt");
        Scanner expectedScanner = new Scanner(expectedFile);
        StringBuilder expectedString = new StringBuilder();
        while (expectedScanner.hasNext()) {
            expectedString.append(expectedScanner.nextLine());
        }
        expectedScanner.close();

        assertEquals(expectedString.toString(), actualString.toString());
    }
}
