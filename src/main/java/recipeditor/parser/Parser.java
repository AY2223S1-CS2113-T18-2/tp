package recipeditor.parser;

import recipeditor.command.*;

import recipeditor.exception.ParseFileException;
import recipeditor.recipe.Recipe;
import recipeditor.ui.Editor;
import recipeditor.recipe.RecipeList;
import recipeditor.storage.Storage;
import recipeditor.ui.EditMode;
import recipeditor.ui.Ui;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class Parser {
    private static Logger logger = Logger.getLogger("LOGS");

    public static Command parseCommand(String input) {
        String[] parsed = input.split(" ");
        String commandWord = parsed[0].toLowerCase();

        switch (commandWord) {
        case AddCommand.COMMAND_TYPE:
            return parseAddCommand();
        case ListCommand.COMMAND_TYPE:
            return new ListCommand();
        case ExitCommand.COMMAND_TYPE:
            return new ExitCommand();
        case DeleteCommand.COMMAND_TYPE:
            return null;
        case EditCommand.COMMAND_TYPE:
            return parseEditCommand(parsed);
        case ViewCommand.COMMAND_TYPE:
            return parseListAlterCommand(parsed, commandWord);
        case FindCommand.COMMAND_TYPE:
            return parseFindCommand(parsed);
        case HelpCommand.COMMAND_TYPE:
            return new HelpCommand();
        default:
            return new InvalidCommand();
        }
    }


    private static Command parseAddCommand() {
        boolean saveToTemp = new Editor().enterEditor(Storage.TEMPLATE_FILE_PATH);
        boolean exitLoop = (saveToTemp) ? false : true;
        boolean valid = false;
        Recipe addRecipe = new Recipe();
        while (!exitLoop) {
            try {
                String content = Storage.loadFileContent(Storage.TEMPORARY_FILE_PATH);
                addRecipe = new TextFileParser().parseTextToRecipe(content);
                valid = true;
                exitLoop = true;
            } catch (ParseFileException | FileNotFoundException e) {
                Ui.showMessage(e.getMessage());
                Ui.showMessage("Do you want to ABORT? (Y/N)");
                String text = Ui.readInput();
                if (text.equalsIgnoreCase("n")) {
                    new Editor().enterEditor(Storage.TEMPORARY_FILE_PATH);
                } else {
                    exitLoop = true;
                }
            }
        }
        return new AddCommand(valid, addRecipe);
    }

    private static Command parseListAlterCommand(String[] parsed, String commandWord) {
        if (parsed.length == 2) {
            try {
                int index = Integer.parseInt(parsed[1]) - 1; // to account for 0-based indexing in recipelist
                if (commandWord.equals(ViewCommand.COMMAND_TYPE)) {
                    return new ViewCommand(index);
                }
                return new DeleteCommand(index);
            } catch (Exception e) {
                System.out.format("Exception: Wrong command Format%n"
                        + "Try the command in correct format: view/delete <index of task>%n");
                return new InvalidCommand();
            }
        }
        return new InvalidCommand();
    }

    private static Command parseEditCommand(String[] parsed) {
        int index = -1;
        if (parsed.length > 1) {
            try {
                index = Integer.parseInt(parsed[1]) - 1;
            } catch (NumberFormatException n) {
                index = RecipeList.getRecipeIndexFromTitle(parsed[1]);
            }
            EditMode edit = new EditMode();
            edit.enterEditMode(index);
            return new EditCommand(edit.exitEditMode(), index, edit.getEditedRecipe());
        }
        return new InvalidCommand();
    }

    private static Command parseFindCommand(String[] parsed) {
        if (parsed.length < 2) {
            return new InvalidCommand();
        }
        String flagAndInputString = convertStringArrayToString(parsed);
        String[] flagAndInput = flagAndInputString.split(" ", 2);
        char flag = flagAndInput[0].charAt(0);
        String input = flagAndInput[1];
        return new FindCommand(flag, input);
    }

    private static String convertStringArrayToString(String[] stringArray) {
        StringBuilder output = new StringBuilder();
        // Finding the flag in the string array input
        if (stringArray[1].contains("-")) {
            String[] flagAndInput = stringArray[1].split("-");
            String flag = flagAndInput[1];
            output.append(flag + " ");
        }
        for (int i = 2; i < stringArray.length; i++) {
            if (i == stringArray.length - 1) {
                output.append(stringArray[i]);
            } else {
                output.append(stringArray[i] + " ");
            }
        }
        return output.toString();
    }

}
