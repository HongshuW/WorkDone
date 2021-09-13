package duke;

import java.nio.file.Path;
import java.nio.file.Paths;

import duke.command.Command;
import duke.data.Storage;
import duke.data.TaskList;
import duke.exception.DukeException;
import duke.ui.Parser;
import duke.ui.Ui;

/**
 * Represents the Duke program. Manages tasks based on commands received.
 */
public class Duke {
    /** Storage that can write to or retrieve data from a file on hard disk */
    private final Storage storage;
    /** List of tasks added */
    private final TaskList tasks;
    /** UI of the program */
    private final Ui ui;

    /**
     * Constructor of the class 'Duke'.
     *
     * @param filePath Path of the file to retrieve data.
     */
    public Duke(Path filePath) {
        this.ui = new Ui();
        this.tasks = new TaskList();
        this.storage = new Storage(filePath, this.tasks);
    }

    /**
     * Runs the duke program.
     */
    public void run() {
        // Print welcome message, start running
        this.ui.showWelcome();
        boolean isRunning = true;

        // Read in commands while the program is running
        while (isRunning) {
            String commandString = this.ui.getCommand();
            Command command;
            try {
                command = Parser.parse(commandString.strip());
                command.execute(this.tasks, this.storage);
                this.ui.showCommandOutput(command);
                isRunning = command.isRunning();
            } catch (DukeException dukeException) {
                this.ui.showError(dukeException);
            }
        }
    }

    /**
     * Runs the Duke program, prints out messages based on commands received.
     *
     * @param args The command line parameters.
     */
    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir");
        new Duke(Paths.get(filePath, "data", "duke1.txt")).run();
    }

    public String getResponse(String input) {
        Command command;
        try {
            command = Parser.parse(input.strip());
            command.execute(this.tasks, this.storage);
            return command.toString();
        } catch (DukeException dukeException) {
            return dukeException.toString();
        }
    }

    public String getGreetingMessage() {
        return this.ui.getGreetingMessage();
    }
}
