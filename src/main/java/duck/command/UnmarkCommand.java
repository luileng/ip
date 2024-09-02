package duck.command;

import duck.task.TaskList;
import duck.ui.Ui;
import duck.storage.Storage;

/**
 * Represents a command to unmark a task from the task list.
 */
public class UnmarkCommand implements Command {
    private final String fullCommand;

    /**
     * Constructs an UnmarkCommand instance with the command input.
     *
     * @param fullCommand the full command string input
     */
    public UnmarkCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public void execute(TaskList list, Ui ui, Storage storage) {
        int taskIndex = Integer.parseInt(fullCommand.split(" ")[1]) - 1;
        list.unmarkTask(taskIndex);
        ui.showUnmarkedTaskMessage(list.getTask(taskIndex));
        storage.saveTasks(list);
    }
}