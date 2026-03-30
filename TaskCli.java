import java.util.HashMap;

public class TaskCli {

    private static void listTasks(String filter) {
        HashMap<Integer, Task> allTasks = TaskStorage.getTasks();

        if (!filter.equals("all") && !filter.equals("done") &&
            !filter.equals("todo") && !filter.equals("in-progress")) {
            System.out.println("Invalid argument\n");
            printUsage();
            return;
        }

        boolean found = false;
        for (Integer i : allTasks.keySet()) {
            Task currTask = allTasks.get(i);
            if (filter.equals("all") || currTask.getStatus().equals(filter)) {
                System.out.println("ID: " + currTask.getId());
                System.out.println("Description: " + currTask.getDescription());
                System.out.println("Status: " + currTask.getStatus());
                System.out.println("Time Created: " + currTask.getCreatedAt());
                System.out.println("Time Updated: " + currTask.getUpdatedAt() + "\n");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found.\n");
        }
    }

    private static void printUsage() {
        String usage =
        "Task CLI - Command Usage\n\n" +
        "Commands:\n\n" +
        "Add a new task\n" +
        "add \"Task description\"\n\n" +
        "Update an existing task\n" +
        "update <task_id> \"Updated description\"\n\n" +
        "Delete a task\n" +
        "delete <task_id>\n\n" +
        "Mark a task as in progress\n" +
        "mark-in-progress <task_id>\n\n" +
        "Mark a task as done\n" +
        "mark-done <task_id>\n\n" +
        "List all tasks\n" +
        "list\n\n" +
        "List tasks by status\n" +
        "list done\n" +
        "list todo\n" +
        "list in-progress\n\n";

        System.out.println(usage);
    }

    private static int checkId(String id) {
        HashMap<Integer, Task> allTasks = TaskStorage.getTasks();
        int intId;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid id\n");
            printUsage();
            return -1;
        }

        if (allTasks.get(intId) == null) {
            System.out.println("Task with Id " + intId + " does not exist\n");
            printUsage();
            return -1;
        }

        return intId;
    }

    public static void main(String[] args) {

        TaskStorage.initFile();
        TaskStorage.load();
        HashMap<Integer, Task> allTasks = TaskStorage.getTasks();

        if (args.length == 0) {
            System.out.println("No command entered\n");
            printUsage();
            TaskStorage.save();
            return;
        }

        if (args[0].equals("add")) {
            if (args.length != 2) {
                System.out.println("Invalid Input\n");
                printUsage();
            } else if (args[1].trim().isEmpty()) {
                System.out.println("Description cannot be empty.\n");
            } else {
                int lastTask = TaskStorage.getLastTask() + 1;
                TaskStorage.setLastTask(lastTask);
                Task newTask = new Task(lastTask, args[1]);
                allTasks.put(lastTask, newTask);
                System.out.println("Task added successfully (ID: " + lastTask + ")");
            }

        } else if (args[0].equals("update")) {
            if (args.length != 3) {
                System.out.println("Invalid Input\n");
                printUsage();
            } else {
                int id = checkId(args[1]);
                if (id != -1) {
                    allTasks.get(id).updateDescription(args[2]);
                }
            }

        } else if (args[0].equals("delete")) {
            if (args.length != 2) {
                System.out.println("Invalid Input\n");
                printUsage();
            } else {
                int id = checkId(args[1]);
                if (id != -1) {
                    allTasks.remove(id);
                }
            }

        } else if (args[0].equals("mark-in-progress")) {
            if (args.length != 2) {
                System.out.println("Invalid Input\n");
                printUsage();
            } else {
                int id = checkId(args[1]);
                if (id != -1) {
                    allTasks.get(id).updateStatus("in-progress");
                }
            }

        } else if (args[0].equals("mark-done")) {
            if (args.length != 2) {
                System.out.println("Invalid Input\n");
                printUsage();
            } else {
                int id = checkId(args[1]);
                if (id != -1) {
                    allTasks.get(id).updateStatus("done");
                }
            }

        } else if (args[0].equals("list")) {
            if (args.length == 1) {
                listTasks("all");
            } else if (args.length == 2) {
                listTasks(args[1]);
            } else {
                System.out.println("Invalid Input\n");
                printUsage();
            }

        } else {
            System.out.println("Invalid Input\n");
            printUsage();
        }

        TaskStorage.save();
    }
}