import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager(){
        this.tasks = new ArrayList<>();
    }
    public void addTask(String title, String description){
        tasks.add(new Task(title, description));
    }
    public void removeTask(String title){
        tasks.removeIf(task -> task.getTitle().equals(title));
    }
    public List<Task> listTasks(){
        return tasks;
    }
    public String searchTasks(String title){
        for(Task task: tasks){
            if(task.getTitle().equals(title)){
                return task.getDescription();
            }
        }
        return null;
    }

    public void markTaskAsCompleted(String title){
        for (Task task: tasks){
            if (task.getTitle().equals(title)){
                task.setCompleted(true);
            }
        }
    }

}

