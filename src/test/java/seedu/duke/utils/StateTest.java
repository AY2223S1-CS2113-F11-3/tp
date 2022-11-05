package seedu.duke.utils;

import org.junit.jupiter.api.Test;
import seedu.duke.model.Module;
import seedu.duke.model.SelectedModule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StateTest {

    @Test
    public void testGetSelectedModulesList_correctSelectedModuleList() {
        // validate correct module details
        Module module1 = Module.get("CS1010S");
        Module module2 = Module.get("CS1231");
        validateTwoTestModulesCS1010SandCS1231(module1, module2);

        // Validating list of selectedModules
        List<SelectedModule> list = new ArrayList<>();
        int semester = 1;

        SelectedModule selectedModule1 = new SelectedModule(module1, semester);
        SelectedModule selectedModule2 = new SelectedModule(module1, semester);
        assertNotNull(selectedModule1.getModule());
        assertNotNull(selectedModule2.getModule());

        list.add(selectedModule1);
        list.add(selectedModule2);
        assertNotNull(list);

        State state = new State();

        state.setSelectedModulesList(list);
        List<SelectedModule> listReturned = state.getSelectedModulesList();
        assertNotNull(listReturned);

        assertEquals(listReturned, list);
    }

    @Test
    public void testSetAndGetSemester_correctSemester() {
        State state = new State();

        int initialisedSemester = state.getSemester();
        assertEquals(initialisedSemester, 1);

        int newSemester = 2;
        state.setSemester(newSemester);

        int newReturnedSemester = state.getSemester();
        assertEquals(newReturnedSemester, newSemester);

    }


    @Test
    void testAddSelectedModule_returnedListEqualsGenericListOfSelectedModulesAdded() {

        // validate correct module details
        Module module1 = Module.get("CS1010S");
        Module module2 = Module.get("CS1231");
        validateTwoTestModulesCS1010SandCS1231(module1, module2);

        // Validating list of selectedModules
        List<SelectedModule> list = new ArrayList<>();
        int semester = 1;

        SelectedModule selectedModule1 = new SelectedModule(module1, semester);
        SelectedModule selectedModule2 = new SelectedModule(module1, semester);
        assertNotNull(selectedModule1.getModule());
        assertNotNull(selectedModule2.getModule());

        list.add(selectedModule1);
        list.add(selectedModule2);
        assertNotNull(list);

        State state = new State();

        // Add modules via state instance method
        state.addSelectedModule(selectedModule1);
        state.addSelectedModule(selectedModule2);

        List<SelectedModule> returnedListOfAddedModules = state.getSelectedModulesList();
        assertEquals(list, returnedListOfAddedModules);
    }

    @Test
    void testRemoveSelectedModule_returnedListEqualsGenericListOfSelectedModulesAdded() {

        // validate correct module details
        Module module1 = Module.get("CS1010S");
        Module module2 = Module.get("CS1231");
        validateTwoTestModulesCS1010SandCS1231(module1, module2);

        // Validating list of selectedModules
        List<SelectedModule> list = new ArrayList<>();
        int semester = 1;

        SelectedModule selectedModule1 = new SelectedModule(module1, semester);
        SelectedModule selectedModule2 = new SelectedModule(module1, semester);
        assertNotNull(selectedModule1.getModule());
        assertNotNull(selectedModule2.getModule());

        list.add(selectedModule2);
        assertNotNull(list);
        State state = new State();

        // Add modules via state instance method
        state.addSelectedModule(selectedModule1);
        state.addSelectedModule(selectedModule2);

        // Remove modules via state instance method
        state.removeSelectedModule(selectedModule1);

        List<SelectedModule> returnedListOfAddedModules = state.getSelectedModulesList();
        assertEquals(list, returnedListOfAddedModules);

        state.removeSelectedModule(selectedModule2);
        returnedListOfAddedModules = state.getSelectedModulesList();

        list.remove(selectedModule2);
        assertEquals(returnedListOfAddedModules, list);
    }

    void validateTwoTestModulesCS1010SandCS1231(Module module1, Module module2) {
        assertNotNull(module1);
        assertEquals("CS1010S", module1.moduleCode);
        assertEquals("Programming Methodology", module1.title);
        assertEquals(List.of(2, 1, 1, 3, 3), module1.workload);
        assertEquals(2, module1.semesterData.size());
        assertEquals("Computer Science", module1.department);
        assertEquals("Computing", module1.faculty);

        assertNotNull(module2);
        assertEquals("CS1231", module2.moduleCode);
        assertEquals("Discrete Structures", module2.title);
        assertEquals(List.of(3, 1, 0, 3, 3), module2.workload);
        assertEquals(2, module2.semesterData.size());
        assertEquals("Computer Science", module2.department);
        assertEquals("Computing", module2.faculty);
    }

}