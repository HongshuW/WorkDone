package workdone.command;

import workdone.data.Storage;
import workdone.data.TaskList;

/**
 * Represents a command that deletes all done tasks from the task list. A subclass of the ClearCommand class.
 */
public class ClearDoneCommand extends ClearCommand {
    /**
     * Constructor of the class `ClearDoneCommand`.
     */
    public ClearDoneCommand() {
        super();
    }

    /**
     * Executes the command. Deletes all done tasks from the list, stores changes and updates the message
     * to be printed.
     *
     * @param tasks A list of tasks.
     * @param storage An instance of Storage that can read from and write to the hard disk.
     */
    @Override
    public void execute(TaskList tasks, Storage storage) {
        int len = tasks.getNumOfTasks();
        int index = 0;
        for (int i = 0; i < len; i++) {
            this.task = tasks.get(index);
            // Remove the task
            assert this.task != null : "task shouldn't be null";
            if (this.task.isDone()) {
                storage.removeFromFile(index);
                tasks.removeFromList(this.task);
            } else {
                index++;
            }
        }

        // Update message
        this.message = "Noted. I've removed the tasks you've done.\n";
    }
}
