import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private TaskManager taskManager;
    private final String productionDbUrl = "jdbc:sqlite:tarefas.db";
    private final String testDbUrl = "jdbc:sqlite::memory:";


    @BeforeEach
    void setUp() {
        DatabaseHelper.setDatabaseUrl(testDbUrl);
        DatabaseHelper.initializeDatabase();
        taskManager = new TaskManager();
    }

    @AfterEach
    void tearDown() {
        DatabaseHelper.setDatabaseUrl(productionDbUrl);
    }

    @Test
    void testAddTask_ShouldPersistAndAssignId() {
        taskManager.addTask("Test Persist", "Description Persist");

        List<Task> tasksFromManager = taskManager.listTasks();
        assertEquals(1, tasksFromManager.size(), "TaskManager deve ter 1 tarefa em memória.");
        Task addedTask = tasksFromManager.get(0);

        assertNotNull(addedTask, "A tarefa adicionada não deve ser nula.");
        assertTrue(addedTask.getId() > 0, "A tarefa deve ter um ID positivo gerado pelo banco.");
        assertEquals("Test Persist", addedTask.getTitle());
        assertEquals("Description Persist", addedTask.getDescription());
        assertFalse(addedTask.isCompleted());

        List<Task> tasksFromDb = DatabaseHelper.getAllTasks();
        assertEquals(1, tasksFromDb.size(), "Banco de dados deve conter 1 tarefa.");
        assertEquals(addedTask.getId(), tasksFromDb.get(0).getId(), "ID da tarefa no manager e no DB devem coincidir.");
    }

    @Test
    void testRemoveTask_TaskExists_ShouldRemoveFromDbAndMemory() {
        taskManager.addTask("Task to Remove", "This will be removed");
        List<Task> tasksBeforeRemove = taskManager.listTasks();
        int taskIdToRemove = tasksBeforeRemove.get(0).getId();

        assertTrue(taskManager.removeTask(taskIdToRemove), "removeTask deve retornar true para tarefa existente.");
        assertEquals(0, taskManager.listTasks().size(), "Lista em memória do TaskManager deve estar vazia.");
        assertTrue(DatabaseHelper.getAllTasks().isEmpty(), "Banco de dados deve estar vazio após remover a tarefa.");
    }

    @Test
    void testRemoveTask_TaskDoesNotExist() {
        taskManager.addTask("Existing Task", "This one stays");
        assertFalse(taskManager.removeTask(999), "removeTask deve retornar false para ID inexistente.");
        assertEquals(1, taskManager.listTasks().size(), "Lista em memória não deve mudar para ID inexistente.");
        assertEquals(1, DatabaseHelper.getAllTasks().size(), "Banco de dados não deve mudar para ID inexistente.");
    }

    @Test
    void testMarkTaskAsCompleted_TaskExists_ShouldUpdateInDbAndMemory() {
        taskManager.addTask("Task to Complete", "This will be completed");
        List<Task> tasksBeforeComplete = taskManager.listTasks();
        int taskIdToComplete = tasksBeforeComplete.get(0).getId();

        assertTrue(taskManager.markTaskAsCompleted(taskIdToComplete), "markTaskAsCompleted deve retornar true.");

        Optional<Task> taskFromManagerOpt = taskManager.findTaskById(taskIdToComplete);
        assertTrue(taskFromManagerOpt.isPresent(), "Tarefa deve ser encontrada no manager após completar.");
        assertTrue(taskFromManagerOpt.get().isCompleted(), "Tarefa no manager deve estar como concluída.");

        Optional<Task> taskFromDbOpt = DatabaseHelper.getAllTasks().stream()
                .filter(t -> t.getId() == taskIdToComplete)
                .findFirst();
        assertTrue(taskFromDbOpt.isPresent(), "Tarefa deve ser encontrada no DB.");
        assertTrue(taskFromDbOpt.get().isCompleted(), "Tarefa no DB deve estar como concluída.");
    }

    @Test
    void testMarkTaskAsCompleted_TaskDoesNotExist() {
        taskManager.addTask("Existing Task", "This one stays");
        assertFalse(taskManager.markTaskAsCompleted(999), "markTaskAsCompleted deve retornar false para ID inexistente.");

        Optional<Task> existingTaskOpt = taskManager.findTaskById(1);
        assertTrue(existingTaskOpt.isPresent());
        assertFalse(existingTaskOpt.get().isCompleted(), "Tarefa existente não deve ser afetada.");
    }

    @Test
    void testListTasks_LoadsFromDatabaseOnInitialization() {

        DatabaseHelper.initializeDatabase();
        Task directDbTask = new Task("Direct DB Title", "Direct DB Desc");
        int directDbId = DatabaseHelper.insertTask(directDbTask);
        assertTrue(directDbId > 0);


        TaskManager newTm = new TaskManager();
        List<Task> loadedTasks = newTm.listTasks();
        assertEquals(1, loadedTasks.size());
        assertEquals(directDbId, loadedTasks.get(0).getId());
        assertEquals("Direct DB Title", loadedTasks.get(0).getTitle());
    }

    @Test
    void testFindTaskById_WhenTaskExistsInMemoryReflectingDb() {
        taskManager.addTask("Find Me By ID", "ID Search Desc");
        int taskId = taskManager.listTasks().get(0).getId();

        Optional<Task> foundTaskOpt = taskManager.findTaskById(taskId);
        assertTrue(foundTaskOpt.isPresent());
        assertEquals(taskId, foundTaskOpt.get().getId());
    }

    @Test
    void testFindTaskById_WhenTaskDoesNotExist() {
        Optional<Task> foundTaskOpt = taskManager.findTaskById(888);
        assertFalse(foundTaskOpt.isPresent());
    }
}