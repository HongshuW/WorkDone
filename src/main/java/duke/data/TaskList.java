package duke.data;

import java.util.ArrayList;

import duke.task.Task;

/**
 * Represents a temporary list of tasks.
 */
public class TaskList {
    /** List of tasks */
    private final ArrayList<Task> tasks;

    /**
     * Constructor of the class `TaskList`.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds task to the task list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        this.tasks.add(task); // add to task list.
    }

    /**
     * Removes a task from the task list.
     *
     * @param task The task to be removed.
     */
    public void removeFromList(Task task) {
        this.tasks.remove(task); // remove from task list.
    }

    /**
     * Returns a task as a string to be stored in the file.
     *
     * @param index Index of the task.
     * @return String representation of the task.
     */
    public String getFileFormattedTask(int index) {
        return this.tasks.get(index).toFileFormatString();
    }

    /**
     * Returns the number of tasks.
     *
     * @return Number of tasks.
     */
    public int getNumOfTasks() {
        return this.tasks.size();
    }

    /**
     * Returns the task at the given index in the task list.
     *
     * @param index Index of a task.
     * @return Task at the given index.
     */
    public Task get(int index) {
        return this.tasks.get(index);
    }

    /**
     * Returns the index of a task in the task list.
     *
     * @param task Task in the task list.
     * @return Index of the task given.
     */
    public int indexOf(Task task) {
        return this.tasks.indexOf(task);
    }

    /**
     * Filters the task list and returns a new list with tasks containing the keyword.
     *
     * @param keyword The keyword to be checked.
     * @return A new list with tasks containing the keyword.
     */
    public ArrayList<Task> filter(String keyword) {
        int len = this.getNumOfTasks();
        ArrayList<Task> filteredTasks = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Task task = this.tasks.get(i);
            if (task.containsKeyword(keyword)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}
