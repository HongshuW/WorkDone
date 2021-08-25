/**
 * Represents a processor that can process a command and print out the results.
 */
public abstract class Command {
    /** The command to be processed */
    protected String content;
    /** Message generated */
    protected String message;
    /** Task involved in the command */
    protected Task task;

    /**
     * Constructor of the class `Processor`.
     *
     * @param content The command received.
     */
    public Command(String content) {
        this.content = content;
    }

    /**
     * Updates the message to be printed.
     *
     * @return Whether the program is still running.
     */
    public boolean execute(TaskList taskList, Storage storage) throws DukeException {
        this.message = this.content;
        return true;
    }

    /**
     * Returns the command's processed result as a string.
     *
     * @return String representation of the result of processing a command.
     */
    @Override
    public String toString() {
        return "____________________________________________________________\n" +
                this.message +
                "____________________________________________________________\n";
    }
}
