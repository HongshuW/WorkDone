/**
 * Represents a processor that can add a task to the task list. A subclass of the Processor class.
 */
public class AddATaskProcessor extends Processor {
    /**
     * Constructor of the class `AddATaskProcessor`.
     *
     * @param task Task to be added.
     */
    public AddATaskProcessor(Task task, Duke duke) {
        super("add", duke);
        this.task = task;
    }

    /**
     * Adds a task into the list and updates the message to be printed.
     *
     * @return Whether the program is still running.
     */
    @Override
    public boolean process() {
        this.duke.addToList(this.task);
        this.message = String.format(
                "Got it. I've added this task:\n  %s\nNow you have %o tasks in the list.\n",
                this.task, this.duke.getNumOfTasks());
        return true;
    }
}
