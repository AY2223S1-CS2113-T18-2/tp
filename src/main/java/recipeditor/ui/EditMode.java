package recipeditor.ui;

import recipeditor.edit.Add;
import recipeditor.edit.Delete;
import recipeditor.edit.EditModeCommand;
import recipeditor.edit.Invalid;
import recipeditor.edit.Swap;
import recipeditor.edit.Change;
import recipeditor.recipe.Recipe;
import recipeditor.recipe.RecipeList;

public class EditMode {
    private static final String INVALID_INPUT = "Invalid input given. ";
    private static final String INVALID_INDEX = "Invalid index given. ";
    private static final String GENERIC_ERROR = "Something happened. ";

    private static final String ENTER = "Entering edit mode. Currently editing: ";
    private static final String HELP_1 = "Available commands: /add, /del, /swap, /view, /done ";
    private static final String HELP_2 = "Available flags: -i (ingredients), -s (steps) ";
    private static final String EXIT = "Exiting edit mode. ";
    private static final String NOT_FOUND = "Recipe not found. ";
    private static final String OLD = "Before: ";
    private static final String NEW = "After: ";

    private Recipe originalRecipe;
    private Recipe editedRecipe;

    public void enterEditMode(int index) {
        if (index < 0) {
            Ui.showMessageInline(NOT_FOUND);
            return;
        }
        try {
            originalRecipe = RecipeList.getRecipe(index);
            Ui.showMessageInline(ENTER + originalRecipe.getTitle());

            editedRecipe = new Recipe(originalRecipe.getTitle(), originalRecipe.getDescription());
            editedRecipe.addIngredients(originalRecipe.getIngredients());
            editedRecipe.addSteps(originalRecipe.getSteps());

            Ui.showMessageInline(originalRecipe.getRecipeAttributesFormatted());

            String input = "";

            while (!input.equals("/done")) {
                Ui.showMessageInline(HELP_1);
                Ui.showMessageInline(HELP_2);
                input = Ui.readInput();
            }
        } catch (IndexOutOfBoundsException i) {
            Ui.showMessageInline(INVALID_INDEX);
        } catch (NumberFormatException n) {
            Ui.showMessageInline(INVALID_INPUT);
        } catch (Exception e) {
            Ui.showMessageInline(GENERIC_ERROR);
        }
    }

    public boolean exitEditMode() {
        Ui.showMessageInline(EXIT);
        if (editedRecipe == null) {
            return false;
        } else {
            Ui.showMessageInline(OLD);
            Ui.showMessageInline(originalRecipe.getRecipeAttributesFormatted());
            Ui.showMessageInline(NEW);
            Ui.showMessageInline(editedRecipe.getRecipeAttributesFormatted());
            return true;
        }
    }

    public Recipe getEditedRecipe() {
        return editedRecipe;
    }

}
