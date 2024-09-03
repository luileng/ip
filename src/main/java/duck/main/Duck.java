package duck.main;

import duck.command.Command;
import duck.command.InvalidCommandException;
import duck.parser.Parser;
import duck.storage.Storage;
import duck.task.TaskList;
import duck.ui.Ui;

/**
 * Represents chatbot for managing tasks.
 */
public class Duck {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Duck instance.
     * @param filePath the path to the file for loading and saving tasks
     */
    public Duck(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the main application, which handles and executes user commands,
     * until the user inputs the command "bye".
     */
    public void run() {
        ui.showWelcomeMessage();

        while (true) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                break;
            }
            try {
                Command cmd = Parser.parse(command);
                cmd.execute(tasks, ui, storage);
            } catch (InvalidCommandException e) {
                ui.showInvalidCommand();
            }
        }
        ui.showGoodbyeMessage();
        ui.closeScanner();
    }

    /**
     * The entry point of the application. Initializes a Duck instance
     * and starts the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String filePath = "data/duck.txt";
        new Duck(filePath).run();
    }
}
