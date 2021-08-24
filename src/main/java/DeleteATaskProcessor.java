/**
 * Represents a processor that can delete a task from the task list. A subclass of the Processor class.
 */
public class DeleteATaskProcessor extends Processor {
    /**
     * Constructor of the class `DeleteATaskProcessor`.
     *
     * @param task Task to be deleted.
     */
    public DeleteATaskProcessor(Task task, Duke duke) {
        super("delete", duke);
        this.task = task;
    }

    /**
     * Updates the message to be printed.
     *
     * @return Whether the program is still running.
     */
    @Override
    public boolean process() {
        this.duke.removeFromList(this.task);
        this.message = String.format(
                "Noted. I've removed this task:\n  %s\nNow you have %o tasks in the list.\n",
                this.task, this.duke.getNumOfTasks());
        return true;
    }
}
