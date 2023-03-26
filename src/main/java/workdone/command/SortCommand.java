package workdone.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import javafx.util.Pair;
import workdone.data.Storage;
import workdone.data.TaskList;
import workdone.task.Deadline;
import workdone.task.Task;

/**
 * Represents a command that sorts all tasks with deadline from the task list. A subclass of the Command class.
 */
public class SortCommand extends Command {
    /**
     * Constructor of the class `SortCommand`.
     */
    public SortCommand() {
        super("sort");
    }

    /**
     * Executes the command. Sorts all tasks with deadline from the list, stores changes and updates the
     * message to be printed.
     *
     * @param tasks A list of tasks.
     * @param storage An instance of Storage that can read from and write to the hard disk.
     */
    @Override
    public void execute(TaskList tasks, Storage storage) {
        int len = tasks.getNumOfTasks();
        List<Task> otherTasks = new ArrayList<>();
        List<Pair<LocalDateTime, Task>> deadlineTasks = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            this.task = tasks.get(0);
            // Remove the task
            storage.removeFromFile(0);
            tasks.removeFromList(this.task);
            if (this.task instanceof Deadline) {
                deadlineTasks.add(new Pair<>(((Deadline) this.task).getTime(), this.task));
            } else {
                otherTasks.add(this.task);
            }
        }
        deadlineTasks.sort(new Comparator<Pair<LocalDateTime, Task>>() {
            @Override
            public int compare(Pair<LocalDateTime, Task> o1, Pair<LocalDateTime, Task> o2) {
                if (o1.getKey().isBefore(o2.getKey())) {
                    return -1;
                } else if (o1.getKey().isAfter(o2.getKey())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        // Add sorted tasks
        for (Pair<LocalDateTime, Task> pair : deadlineTasks) {
            tasks.addTask(pair.getValue());
            storage.addToFile(pair.getValue());
        }
        for (Task task : otherTasks) {
            tasks.addTask(task);
            storage.addToFile(task);
        }

        // Update message
        this.message = "Noted. I've sorted all the tasks with deadline.\n";
        this.message += "Here are the tasks in your list:\n";
        this.message += tasks.getFilteredListAsString(x -> true);
    }
}
