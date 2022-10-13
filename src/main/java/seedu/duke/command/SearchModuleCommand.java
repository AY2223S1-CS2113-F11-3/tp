package seedu.duke.command;

import seedu.duke.utils.State;
import seedu.duke.utils.Storage;
import seedu.duke.utils.Ui;
import seedu.duke.model.Module;
import seedu.duke.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchModuleCommand extends Command {
    public static final String FORMAT = "search /code [MODULE_CODE] /title [MODULE_TITLE] /level [LEVEL] /sem [SEMESTER]";
    public static final String COMMAND_WORD = "search";

    // private String toSearchModuleCode;
    private Map<String, String> params;
    private String toSearchModuleCode;
    private String toSearchModuleTitle;
    private String toSearchLevel;
    private String toSearchSemester;

    public SearchModuleCommand(String input) {
        super(input.split("\\s"));
        params = Parser.parseParams(input);
    }

    @Override
    public void execute(State state, Ui ui, Storage storage) {
        toSearchModuleCode = params.getOrDefault("code", null);
        toSearchModuleTitle = params.getOrDefault("title", null);
        toSearchLevel = params.getOrDefault("level", null);
        toSearchSemester = params.getOrDefault("sem", null);
    
        List<Module> searchResult = filterModuleSearch(toSearchModuleCode, toSearchLevel, toSearchSemester, toSearchModuleTitle);

        // size of searchResult
        int resultSize = searchResult.size();
        ui.addMessage("Search Result:");

        if (searchResult.size() == 0) {
            ui.addMessage("No module found");
        } else {
            ui.addMessage("Total " + resultSize + " module(s) found\n");
            for (int i = 0; i < searchResult.size(); i++) {
                ui.addMessage(searchResult.get(i).moduleCode + " " + searchResult.get(i).title);
            }
            ui.addMessage("\nTo get full details of the module, type 'get <module code>'");
        }

        ui.displayUi();
    }

    /**
     * Filter the module list based on input module level.
     * @param module
     * @param level
     * @return true if module level matches input level
     */
    static boolean isSameModuleLevel(Module module, String level) {
        // get the first int embedded in the module code
        String moduleCode = module.moduleCode;
        int moduleLevel = (Integer.parseInt(moduleCode.replaceAll("[^0-9]", ""))) / 1000;

        if (level.equals("1") && moduleLevel == 1) {
            return true;
        } else if (level.equals("2") && moduleLevel == 2) {
            return true;
        } else if (level.equals("3") && moduleLevel == 3) {
            return true;
        } else if (level.equals("4") && moduleLevel == 4) {
            return true;
        } else if (level.equals("5") && moduleLevel == 5) {
            return true;
        } else if (level.equals("6") && moduleLevel == 6) {
            return true;
        } else if (level.equals("7") && moduleLevel == 7) {
            return true;
        } else if (level.equals("8") && moduleLevel == 8) {
            return true;
        } else if (level.equals("9") && moduleLevel == 9) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Filters the module list based on the input semester.
     * @param module
     * @param toSearchSemester
     * @return true if module is offered in the semester
     */
    static boolean isOfferedInSemester(Module module, String toSearchSemester) {
        // convert toSearchSemester to int
        int toSearchSemesterInt = Integer.parseInt(toSearchSemester);

        // check module is offered in which semester
        if(module.getSemesterData(toSearchSemesterInt) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Filter module by user input arguments and return a list of modules that match the search query.
     * If no arguments are provided, no module will be returned.
     * At least the module code or module title must be provided.
     * If both module code and module title are provided, results will be display based on similar module code
     * and module title but will not be repeated.
     * Arguments can be in any order.
     * 
     * @param toSearchModuleCode
     * @param toSearchLevel
     * @param toSearchSemester
     * @param toSearchModuleTitle
     * @return list of modules that match the search query
     */
    public static List<Module> filterModuleSearch(String toSearchModuleCode, String toSearchLevel, String toSearchSemester, String toSearchModuleTitle) {
        List<Module> moduleList = Module.getAll();
        List<Module> searchResult = new ArrayList<>();

        // add all the mods with similar toSearchModuleCode to searchResult
        for (Module m : moduleList) {
            if (toSearchModuleCode != null && m.moduleCode.contains(toSearchModuleCode.toUpperCase())) {
                searchResult.add(m);
            } 
            
            if (toSearchModuleTitle != null && m.title.toLowerCase().contains(toSearchModuleTitle.toLowerCase())) {
                // add only if it is not already in the list
                if (!searchResult.contains(m)) {
                    searchResult.add(m);
                }
            }
        }

        // filter the searchResult if toSearchLevel is not empty and level does not match
        if (toSearchLevel != null) {
            for (int i = 0; i < searchResult.size(); i++) {
                if (!isSameModuleLevel(searchResult.get(i), toSearchLevel)) {
                    searchResult.remove(i);
                    i--;
                }
            }
        }

        // filter the searchResult if toSearchSemester is not empty and semester does not match
        if (toSearchSemester != null) {
            for (int i = 0; i < searchResult.size(); i++) {
                if (!isOfferedInSemester(searchResult.get(i), toSearchSemester)) {
                    searchResult.remove(i);
                    i--;
                }
            }
        }

        return searchResult;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public String getExecutionMessage() {
        return null;
    }
}
