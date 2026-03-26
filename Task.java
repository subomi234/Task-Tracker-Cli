import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    
    private int id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;

    //formats the date and time 
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E MM/dd/yyyy hh:mm a");

    public Task(){
        id = 0;
        description = "";
        status = "";
        createdAt = "";
        updatedAt = "";
    }

    public Task(int id, String description){

        this.id = id;
        this.description = description;
        status = "to-do";

        LocalDateTime date = LocalDateTime.now();
        String formatDate = date.format(myFormatObj);
        createdAt = formatDate;
        updatedAt = formatDate;
    }

    public Task(int id, String description, String status, String createdAt, String updatedAt){

        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //create
    public int getId(){
        return id;
    }

    //create
    public String getDescription(){
        return description;
    }

    //create
    public String getStatus(){

        return status;
    }

    //create
    public String getCreatedAt(){

        return createdAt;
    }

    //create
    public String getUpdatedAt(){

        return updatedAt;
    }

    public void updateStatus(String status){

        LocalDateTime date = LocalDateTime.now();
        String formatDate = date.format(myFormatObj);
        updatedAt = formatDate;

        this.status = status;
    }

    public void updateDescription(String description){
        LocalDateTime date = LocalDateTime.now();
        String formatDate = date.format(myFormatObj);
        updatedAt = formatDate;

        this.description = description;
    }


}
