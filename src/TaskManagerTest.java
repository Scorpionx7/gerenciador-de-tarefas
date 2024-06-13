import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    @Test
    void testAddTask(){
        TaskManager taskManager = new TaskManager();
        taskManager.addTask("Test", "This is a test task");
        assertEquals(1, taskManager.listTasks().size());
    }

    @Test
    void testRemoveTask(){
        TaskManager taskManager = new TaskManager();
        taskManager.addTask("Test", "This is a test task");
        taskManager.removeTask("Test");
        assertEquals(0, taskManager.listTasks().size());
    }

    @Test
    void testSearchTask() {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask("Test", "This is a test task");
        assertEquals("This is a test task", taskManager.searchTasks("Test"));
    }

    @Test
    void testMarkTaskAsCompleted() {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask("Test", "This is a test task");
        taskManager.markTaskAsCompleted("Test");
        assertTrue(taskManager.listTasks().get(0).isCompleted());
    }

}
