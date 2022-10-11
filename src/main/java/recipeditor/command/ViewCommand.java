package recipeditor.command;

import recipeditor.recipe.Recipe;
import recipeditor.recipe.RecipeList;

public class ViewCommand extends Command {
    public static final String COMMAND_TYPE = "view";
    //unnecessary comment
    private final int index;

    /**
     * Construct a view command including task to view.
     *
     * @param index the index of task view
     */
    public ViewCommand(int index) {
        this.index = index;
    }


    /**
     * View the recipe at the given index.
     *
     * @return the result message from execute
     */
    public CommandResult execute() {

        try {
            Recipe recipe = RecipeList.getRecipes().get(index);
            String result = String.format("%nName: %s%nDescription: %s%n",
                    recipe.getTitle(), recipe.getDescription());
            //TODO: have a display all details function from recipe
            return new CommandResult(result);
        } catch (IndexOutOfBoundsException e) {
            return new CommandResult(String.format(
                    "There are only %d recipes now.%n", index));
        }

    }
}
