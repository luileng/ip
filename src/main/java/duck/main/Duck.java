package duck.main;

import duck.command.Command;
import duck.command.InvalidCommandException;
import duck.parser.Parser;
import duck.storage.Storage;
import duck.task.TaskList;
import duck.ui.Ui;

public class Duck {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Duck(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

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

    public static void main(String[] args) {
        String filePath = "data/duck.txt";
        new Duck(filePath).run();
    }
}