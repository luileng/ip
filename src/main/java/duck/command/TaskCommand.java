package duck.command;

import duck.task.Deadline;
import duck.task.EmptyToDoException;
import duck.task.Event;
import duck.task.Task;
import duck.task.ToDo;
import duck.task.TaskList;
import duck.ui.Ui;
import duck.storage.Storage;
import java.time.LocalDate;

/**
 * Represents a command to manage tasks such as ToDo, Deadline, and Event.
 */
public class TaskCommand implements Command {
    private final String fullCommand;
    private final String commandType;

    /**
     * Constructs a TaskCommand instance with the specified command input and command type.
     *
     * @param fullCommand the full command string input
     * @param commandType the type of command: "todo", "deadline" or "event"
     */
    public TaskCommand(String fullCommand, String commandType) {
        this.fullCommand = fullCommand;
        this.commandType = commandType;
    }

    /**
     * Executes the command depending on the commandType
     *
     * @param list the task list to update
     * @param ui the user interface to interact with the user
     * @param storage the storage to save the updated task list
     */
    @Override
    public void execute(TaskList list, Ui ui, Storage storage) {
        Task task = switch (commandType) {
            case "todo" -> parseToDo(fullCommand);
            case "deadline" -> parseDeadline(fullCommand);
            case "event" -> parseEvent(fullCommand);
            default -> throw new IllegalArgumentException("Invalid command type for tasks: " + commandType);
        };
        list.add(task);
        ui.showAddTaskMessage(task, list);
        storage.saveTasks(list);
    }

    /**
     * Parses and creates a ToDo task.
     *
     * @param fullCommand the full command string input
     * @return a ToDo task
     */
    public static Task parseToDo(String fullCommand) {
        try {
            if (fullCommand.equals("todo")) {
                throw new EmptyToDoException("Cannot have empty todo");
            }
            return new ToDo(fullCommand.split(" ", 2)[1]);
        } catch (EmptyToDoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Parses and creates a Deadline task.
     *
     * @param fullCommand the full command string input
     * @return a Deadline task
     */
    public static Task parseDeadline(String fullCommand) {
        String[] commandParts = fullCommand.split("/by");
        LocalDate date = LocalDate.parse(commandParts[1]);
        return new Deadline(commandParts[0].split(" ", 2)[1], date);
    }

    /**
     * Parses and creates an Event task.
     *
     * @param fullCommand the full command string input
     * @return an Event task
     */
    public static Task parseEvent(String fullCommand) {
        String[] commandParts = fullCommand.split("/from|/to");
        return new Event(commandParts[0].split(" ", 2)[1], commandParts[1], commandParts[1]);
    }
}