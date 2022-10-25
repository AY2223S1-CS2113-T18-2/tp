package recipeditor.edit;

import recipeditor.exception.InvalidFlagException;
import recipeditor.recipe.Recipe;
import recipeditor.ui.Ui;

public class Delete extends EditModeCommand {

    public Delete(String[] parsedCommand, Recipe recipe) {
        super(parsedCommand, recipe);
    }

    @Override
    public Recipe execute() throws InvalidFlagException {
        Ui.showMessage(recipe.getRecipeAttributesFormatted());
        Recipe oldRecipe = recipe;
        int index = Integer.parseInt(parsedCommand[2]) - 1;
        switch (flag) {
        case INGREDIENT:
            recipe.deleteIngredient(index);
            message = showRecipeChanges(recipe, oldRecipe);
            Ui.showMessage(recipe.getRecipeAttributesFormatted());
            return recipe;
        case STEP:
            recipe.deleteStep(index);
            message = showRecipeChanges(recipe, oldRecipe);
            Ui.showMessage(recipe.getRecipeAttributesFormatted());
            return recipe;
        default:
            throw new InvalidFlagException();
        }
    }

}
