package recipeditor.edit;

import recipeditor.exception.InvalidFlagException;
import recipeditor.exception.ParseException;
import recipeditor.recipe.Ingredient;
import recipeditor.recipe.Recipe;

public class Add extends EditModeCommand {

    public Add(String[] parsedCommand, Recipe recipe) {
        super(parsedCommand, recipe);
    }

    @Override
    public Recipe execute() throws ParseException, InvalidFlagException {
        StringBuilder content = new StringBuilder();
        for (int i = 2; i < parsedCommand.length; i++) {
            content.append(parsedCommand[i]).append(" ");
        }

        switch (flag) {
        case STEP:
            recipe.addStep(content.toString());
            message = String.format("Step: %s is added", content);
            return recipe;
        case INGREDIENT:
            try {
                Ingredient newIngredient = Ingredient.parsedIngredients(content.toString());
                recipe.addIngredient(newIngredient);
                message = String.format("Ingredient: %s is added", content);
                return recipe;
            } catch (ParseException e) {
                throw new ParseException();
            }
        default:
            throw new InvalidFlagException();
        }
    }

}
