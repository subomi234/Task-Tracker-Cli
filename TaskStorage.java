import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TaskStorage {

    private static final String FILE_NAME = "tasks.json";
    private static HashMap<Integer, Task> allTasks = new HashMap<>();
    private static int lastTask = 0;

    public static HashMap<Integer, Task> getTasks() { 
        return allTasks; 
    }

    public static int getLastTask() { 
        return lastTask; 
    }

    public static void setLastTask(int n) { 
        lastTask = n; 
    }

    // creates the file if it doesn't exist
    public static void initFile() {
        try {
            File myTasks = new File(FILE_NAME);
            if (myTasks.createNewFile()) {
                System.out.println("File created: " + myTasks.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating file.");
            e.printStackTrace();
        }
    }

    // loads tasks and lastTask counter from file
    public static void load() {
        File file = new File(FILE_NAME);

        // skip reading if file is empty
        if (file.length() == 0) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"last_task\"")) {
                    lastTask = Integer.valueOf(parse(line));
                }
                if (line.startsWith("\t\t\t\"id\"")) {
                    Task newTask = createTask(
                        line,
                        reader.readLine(),
                        reader.readLine(),
                        reader.readLine(),
                        reader.readLine()
                    );
                    allTasks.put(newTask.getId(), newTask);
                }
            }
        } catch (IOException | NumberFormatException | StringIndexOutOfBoundsException e) {
            System.out.println("Warning: tasks.json appears corrupted. Starting fresh.");
            File backup = new File("tasks.json.bak");
            new File(FILE_NAME).renameTo(backup);
            System.out.println("Corrupted file backed up to tasks.json.bak");
            allTasks = new HashMap<>();
            lastTask = 0;
        }
    }

    // saves all tasks and lastTask counter to file
    public static void save() {
        int checked = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("{");
            writer.write("\n\t\"last_task\": " + lastTask + ",");
            writer.write("\n\t\"tasks\":[");

            for (Integer i : allTasks.keySet()) {
                Task t = allTasks.get(i);
                writer.write("\n\t\t{\n");
                writer.write("\t\t\t\"id\": " + t.getId() + ",\n");
                writer.write("\t\t\t\"description\": \"" + escape(t.getDescription()) + "\",\n");
                writer.write("\t\t\t\"status\": \"" + t.getStatus() + "\",\n");
                writer.write("\t\t\t\"createdAt\": \"" + t.getCreatedAt() + "\",\n");
                writer.write("\t\t\t\"updatedAt\": \"" + t.getUpdatedAt() + "\"\n");

                if (checked == allTasks.size() - 1) {
                    writer.write("\t\t}");
                } else {
                    writer.write("\t\t},");
                }
                checked++;
            }

            writer.write("\n\t]");
            writer.write("\n}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // escapes special characters before writing to file
    private static String escape(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n");
    }

    // unescapes special characters after reading from file
    private static String unescape(String str) {
        return str.replace("\\n", "\n")
                  .replace("\\\"", "\"")
                  .replace("\\\\", "\\");
    }

    // parses a raw json line to extract the value
    private static String parse(String str) {
        str = str.substring(str.indexOf(":") + 2);
        if (str.contains(",")) {
            str = str.substring(0, str.length() - 1);
        }
        str = str.replace("\"", "");
        return unescape(str);
    }

    // builds a Task from raw json lines
    private static Task createTask(String id, String description, String status,
                                   String createdAt, String updatedAt) {
        return new Task(
            Integer.valueOf(parse(id)),
            parse(description),
            parse(status),
            parse(createdAt),
            parse(updatedAt)
        );
    }
}