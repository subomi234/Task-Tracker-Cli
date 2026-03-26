import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Integer;


public class Main {

    //this method parses through the string 
    private static String parse(String str) {

        //gets all the string after colon 
        str = str.substring(str.indexOf(":") + 2, str.length());

        //if the string has a comma at the end exclude it
        if (str.contains(",")) {
            str = str.substring(0, str.length() - 1);
        }
        str = str.replace("\"", "");
 
        return str;
    }

    //creates task with raw lines from json
    private static Task createTask(String id, String description, String status, 
                String createdAt, String updatedAt){
            
            id = parse(id);
            description = parse(description);
            status = parse(status);
            createdAt = parse(createdAt);
            updatedAt = parse(updatedAt);            

            return new Task(Integer.valueOf(id), description, status, createdAt, updatedAt);
    }

    //this will print all the options users have
    private static void printUsage(){
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


    private static int checkId(String id, HashMap<Integer, Task> map){

        int intId;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid id\n");
            printUsage();
            intId = -1;
        } 

        if (intId != -1 && map.get(intId) == null){
            System.out.println("Task with Id " + intId + " does not exist\n");
            printUsage();
            intId = -1;
        }

        return intId;
    }

    public static void main(String[] args) {

        
        //we will make a map later, for easier indexing
        //haha I did
        HashMap<Integer, Task> allTasks = new HashMap<Integer, Task>();
        int last_task = 0;

        try {
            //create file object 
            File myTasks = new File("tasks.json");

            //try to create the file 
            if (myTasks.createNewFile()) {
                System.out.println("File created " + myTasks.getName());
            }
            else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); // Print error details
        }



        //read 
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("last_task")) {
                    last_task = Integer.valueOf(parse(line));
                    System.out.println(last_task);
                }
                if (line.contains("id")) {
                    Task newTask = createTask(line, reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine());
                    allTasks.put(newTask.getId(), newTask);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //need to check arguments after we read in file
        if (args.length > 0) {

            //adds a new task 
            if (args[0].equals("add")){
                if (args.length != 2) {
                    System.out.println("Invalid Input\n");
                    printUsage();
                    return;
                }
                else {
                    last_task++;
                    Task newTask = new Task(last_task, args[1]);
                    allTasks.put(last_task, newTask);
                }
            }
            //updates task with a new description
            else if (args[0].equals("update")){
                if (args.length != 3) {
                    System.out.println("Invalid Input\n");
                    printUsage();
                    return;
                }
                else {
                    int id = checkId(args[1], allTasks);
                    if (id != -1) {
                        allTasks.get(id).updateDescription(args[2]);
                    }
                }
            }
            else if (args[0].equals("mark-in-progress")){
                if (args.length != 2) {
                    System.out.println("Invalid Input\n");
                    printUsage();
                    return;
                }
                else {
                    int id = checkId(args[1], allTasks);
                    if (id != -1) {
                        allTasks.get(id).updateStatus("in-progress");
                    }
                }
            }
            else if (args[0].equals("mark-done")){
                if (args.length != 2) {
                    System.out.println("Invalid Input\n");
                    printUsage();
                    return;
                }
                else {
                    int id = checkId(args[1], allTasks);
                    if (id != -1) {
                        allTasks.get(id).updateStatus("done");
                    }
                }
            }
            else if (args[0].equals("list")){
                System.out.println("list");
            }
            else{
                System.out.println("Invalid Input\n");
                printUsage();
            }

        }
        else {
            System.out.println("No command entered\n");
            printUsage();
        }
         
        //writes all the tasks back to the json file 

        //keep checked so that I don't add a comma at the end of the last task, which would mess up the json format
        int checked = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.json"))) {
            writer.write("{");
            writer.write("\n\t\"last_task\": " + last_task + ",");
            writer.write("\n\t\"tasks\":[");

            for (Integer i : allTasks.keySet()) {
                writer.write("\n");
                writer.write("\t\t{\n");

                writer.write("\t\t\t\"id\": " + allTasks.get(i).getId() + ",\n");
                writer.write("\t\t\t\"description\": \"" + allTasks.get(i).getDescription() + "\",\n");
                writer.write("\t\t\t\"status\": \"" + allTasks.get(i).getStatus() + "\",\n");
                writer.write("\t\t\t\"createdAt\": \"" + allTasks.get(i).getCreatedAt() + "\",\n");
                writer.write("\t\t\t\"updatedAt\": \"" + allTasks.get(i).getUpdatedAt() + "\"\n");

                // checks for the last task
                if (checked == allTasks.size() - 1) {
                    writer.write("\t\t}");
                }
                else {
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

}
