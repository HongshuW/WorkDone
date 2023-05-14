package workdone.command;

import java.util.ArrayList;
import java.util.List;

import workdone.data.Storage;
import workdone.data.TaskList;
import workdone.task.Deadline;
import workdone.task.Event;
import workdone.task.ToDo;

/**
 * Represents a command that retrieves the task list. A subclass of the Command class.
 */
public class GetListCommand extends Command {
    private List<String> typesToHide;

    /**
     * Constructor of the class `GetListCommand`.
     */
    public GetListCommand() {
        super("list");
        this.message = "Here are the tasks in your list:\n";
        this.typesToHide = new ArrayList<>();
    }

    public GetListCommand(List<String> typesToHide) {
        super("list");
        this.message = "Here are the tasks in your list:\n";
        this.typesToHide = typesToHide;
    }

    /**
     * Executes the command. Updates the message to be printed.
     *
     * @param tasks A list of tasks.
     * @param storage An instance of Storage that can read from and write to the hard disk.
     */
    @Override
    public void execute(TaskList tasks, Storage storage) {
        this.message += tasks.getFilteredListAsString(x -> {
            String type = "";
            if (x instanceof ToDo) {
                type = "T";
            } else if (x instanceof Deadline) {
                type = "D";
            } else if (x instanceof Event) {
                type = "E";
            }
            return !this.typesToHide.contains(type);
        });
    }
}
