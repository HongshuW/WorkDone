import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the Duke program. Manages tasks based on commands received.
 */
public class Duke {
    /** Message to be printed when the program exits */
    private static final String EXITING_MESSAGE =
            "____________________________________________________________\n" +
                    "Bye. Hope to see you again soon!\n" +
                    "____________________________________________________________";
    /** Path of the current folder as a string */
    private static final String DIRECTORY_PATH = System.getProperty("user.dir");
    /** Path of file containing data saved */
    private static File data;
    /** List of tasks */
    private static ArrayList<Task> tasks = new ArrayList<>();
    /** Whether the Duke program is running */
    private boolean isRunning;
    /** UI of the program */
    private Ui ui;

    /**
     * Constructor of the class 'Duke'.
     */
    public Duke() {
        Duke.data = Paths.get(Duke.DIRECTORY_PATH, "data", "duke.txt").toFile();
        Duke.readFile();
        this.isRunning = true;
        this.ui = new Ui();
    }

    /**
     * Reads the data in the file. If the file doesn't exist, create it.
     */
    private static void readFile() {
        try {
            Scanner fileScanner = new Scanner(Duke.data);
            while (fileScanner.hasNextLine()) {
                String task = fileScanner.nextLine();
                Duke.readData(task);
            }
            fileScanner.close();
        } catch (FileNotFoundException fileNotFoundException) { // if file doesn't exist, create it.
            try {
                Duke.data.createNewFile();
            } catch (IOException ioException) { // if directory doesn't exist, create it.
                File directory = Paths.get(Duke.DIRECTORY_PATH, "data").toFile();
                directory.mkdirs();
                Duke.readFile(); // run this method again to create a file.
            }
        }
    }

    /**
     * Reads a line of data, creates a task and adds it to the task list.
     *
     * @param line A line of data.
     */
    private static void readData(String line) {
        String[] splitted = line.split(" / ");
        Task task;
        if (splitted[0].equals("T")) { // a todo task
            task = new ToDo(splitted[2]);
        } else if (splitted[0].equals("D")) { // a task with deadline
            task = new Deadline(splitted[2], splitted[3]);
        } else if (splitted[0].equals("E")) { // an event
            task = new Event(splitted[2], splitted[3]);
        } else {
            task = new Task(splitted[2]);
        }
        if (splitted[1].equals("1")) {
            task.markAsDone();
        }
        Duke.tasks.add(task); // add to task list.
    }

    /**
     * Adds a new task to the task list.
     *
     * @param task The new task.
     */
    public static void addToList(Task task){
        Duke.tasks.add(task); // add to task list.
        try {
            FileWriter fileWriter = new FileWriter(Duke.data, true);
            fileWriter.append(task.toFileFormatString()); // write to file.
            fileWriter.close();
        } catch (IOException ioException) {
            Duke.readFile();
            Ui.showFileNotFoundError();
        }
    }

    /**
     * Removes a task from the task list.
     *
     * @param task The task to be removed.
     */
    public static void removeFromList(Task task) {
        try {
            List<String> lines = Files.readAllLines(Duke.data.toPath());
            FileWriter fileWriter = new FileWriter(Duke.data);
            int index = Duke.tasks.indexOf(task);
            for (int i = 0; i < Duke.getNumOfTasks(); i++) { // remove this task from file.
                if (i != index) {
                    fileWriter.append(lines.get(i) + "\n");
                }
            }
            fileWriter.close();
        } catch (IOException ioException) {
            Duke.readFile();
            Ui.showFileNotFoundError();
        }
        Duke.tasks.remove(task); // remove from task list.
    }

    /**
     * Rewrites the data to the file.
     */
    public static void rewriteFile() {
        try {
            FileWriter fileWriter = new FileWriter(Duke.data);
            for (int i = 0; i < Duke.getNumOfTasks(); i++) {
                fileWriter.append(Duke.tasks.get(i).toFileFormatString());
            }
            fileWriter.close();
        } catch (IOException ioException) {
            Duke.readFile();
            Ui.showFileNotFoundError();
        }
    }

    /**
     * Returns a list of string, which is a copy of `tasks` list.
     *
     * @return A copy of tasks list.
     */
    public static ArrayList<Task> getTasks() {
        ArrayList<Task> copy = new ArrayList<>();
        int len = Duke.getNumOfTasks();
        for (int i = 0; i < len; i++) {
            copy.add(Duke.tasks.get(i));
        }
        return copy;
    }

    /**
     * Returns the number of tasks added.
     *
     * @return Number of tasks.
     */
    public static int getNumOfTasks() {
        return Duke.tasks.size();
    }

    /**
     * Based on the command received, either quit the program or process an event.
     */
    private void readCommand() {
        String command = this.ui.getCommand(); // read the command
        String[] splitted = command.split(" ", 2);

        if (command.equals("bye")) {
            System.out.println(Duke.EXITING_MESSAGE);
            this.isRunning = false;
        } else if (command.equals("list")) {
            this.ui.getCommandOutput(new GetListProcessor());
        } else if (splitted[0].equals("done")) {
            try {
                int index = Integer.parseInt(splitted[1]) - 1;
                this.ui.getCommandOutput(new TaskDoneProcessor(Duke.tasks.get(index)));
            } catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException e) {
                this.ui.showInvalidTaskNoError();
            }
        } else if (splitted[0].equals("delete")) {
            try {
                int index = Integer.parseInt(splitted[1]) - 1;
                this.ui.getCommandOutput(new DeleteATaskProcessor(Duke.tasks.get(index)));
            } catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException e) {
                this.ui.showInvalidTaskNoError();
            }
        } else if (splitted[0].equals("todo")) {
            if (splitted.length >= 2) {
                this.ui.getCommandOutput(new AddATaskProcessor(new ToDo(splitted[1])));
            } else {
                this.ui.showMissingDetailsError("description","todo", "");
            }
        } else if (splitted[0].equals("deadline")) {
            if (splitted.length >= 2) {
                String[] information = splitted[1].split("/by");
                if (information.length == 2) {
                    try {
                        this.ui.getCommandOutput(new AddATaskProcessor(new Deadline(information[0], information[1])));
                    } catch (DateTimeParseException dateTimeParseException) {
                        this.ui.showInvalidTimeError("yyyy-MM-dd HH:mm");
                    }
                } else if (information.length < 2) {
                    this.ui.showMissingDetailsError(
                            "time", "deadline", "/by yyyy-MM-dd HH:mm");
                } else {
                    this.ui.showMultipleTimeSlotsError("deadline");
                }
            } else {
                this.ui.showMissingDetailsError(
                        "description", "deadline", "/by yyyy-MM-dd HH:mm");
            }
        } else if (splitted[0].equals("event")) {
            if (splitted.length >= 2) {
                String[] information = splitted[1].split("/at");
                if (information.length == 2) {
                    try {
                        this.ui.getCommandOutput(new AddATaskProcessor(new Event(information[0], information[1])));
                    } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                        this.ui.showInvalidTimeError("yyyy-MM-dd HH:mm to yyyy-MM-dd HH:mm");
                    }
                } else if (information.length < 2) {
                    this.ui.showMissingDetailsError("time", "event",
                            "/at yyyy-MM-dd HH:mm to yyyy-MM-dd HH:mm");
                } else {
                    this.ui.showMultipleTimeSlotsError("event");
                }
            } else {
                this.ui.showMissingDetailsError("description", "event",
                        "/at yyyy-MM-dd HH:mm to yyyy-MM-dd HH:mm");
            }
        } else {
            this.ui.showInvalidCommandError();
        }
    }

    /**
     * Runs the Duke program, prints out messages based on commands received.
     *
     * @param args The command line parameters.
     */
    public static void main(String[] args) {
        Duke duke = new Duke();
        while (duke.isRunning) {
            duke.readCommand();
        }
    }
}
