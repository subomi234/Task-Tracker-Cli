import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Integer;

public class TaskStorage {

    private HashMap<Integer, Task> allTasks;
    private File myTasks;

    public TaskStorage(HashMap<Integer, Task> allTasks, File myTasks) {
        this.allTasks = allTasks;
        this.myTasks = myTasks;
    }
    
    public void readTasks() {
    }

}
