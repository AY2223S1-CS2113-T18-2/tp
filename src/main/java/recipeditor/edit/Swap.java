package recipeditor.edit;

import recipeditor.exception.InvalidFlagException;
import recipeditor.exception.ParseException;
import recipeditor.parser.FlagParser;
import recipeditor.parser.FlagType;
import recipeditor.recipe.Recipe;
import recipeditor.ui.Ui;

public class Swap extends EditModeCommand {

    public Swap(String[] parsedCommand, Recipe recipe) {
        super(parsedCommand, recipe);
    }

    @Override
    public Recipe execute() throws InvalidFlagException {
        int index1 = Integer.parseInt(parsedCommand[4]) - 1;
        int index2 = Integer.parseInt(parsedCommand[5]) - 1;
        Recipe oldRecipe = recipe;
        Ui.showMessageInline("swap " + ingredientFlag.toString() + index1 + ", " + index2 + " from: \n" + recipe.getRecipeAttributesFormatted());

        switch (ingredientFlag) {
        case INGREDIENT:
            try {
                recipe.swapIngredients(index1, index2);
                message = showRecipeChanges(recipe, oldRecipe);
                return recipe;
            } catch (Exception e) {
                throw new IndexOutOfBoundsException("Index out of bound of number of ingredients.");
            }
        case STEP:
            try {
                recipe.swapSteps(index1, index2);
                message = showRecipeChanges(recipe, oldRecipe);
                return recipe;
            } catch (Exception e) {
                throw new IndexOutOfBoundsException("Index out of bound of number of steps.");
            }
        default:
            throw new InvalidFlagException();
        }
    }

}
