import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        // Carrega as tarefas do banco de dados ao iniciar
        this.tasks = DatabaseHelper.getAllTasks();
    }

    public void addTask(String title, String description) {
        Task newTask = new Task(title, description); // Cria a tarefa, ID ainda não definido
        int generatedId = DatabaseHelper.insertTask(newTask); // Salva no banco e obtém o ID

        if (generatedId != -1) {
            newTask.setId(generatedId); // Define o ID no objeto Task
            this.tasks.add(newTask); // Adiciona à lista em memória
            System.out.println("Tarefa adicionada ao TaskManager com ID: " + generatedId);
        } else {
            System.err.println("Falha ao adicionar tarefa ao banco, não será adicionada ao TaskManager.");
        }
    }

    public List<Task> listTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    public Optional<Task> findTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    public boolean removeTask(int id) {
        // Primeiro, tenta remover do banco de dados
        if (DatabaseHelper.deleteTask(id)) {
            // Se removido do banco com sucesso, remove também da lista em memória
            boolean removedFromMemory = this.tasks.removeIf(task -> task.getId() == id);
            if (removedFromMemory) {
                System.out.println("Tarefa com ID " + id + " também removida da lista em memória.");
            } else {
                // Isso seria um estado inconsistente, mas logamos por via das dúvidas
                System.err.println("Tarefa com ID " + id + " removida do banco, mas não encontrada na lista em memória.");
            }
            return true; // Sucesso na remoção do banco é o principal critério aqui
        }
        // Se não conseguiu remover do banco, retorna false
        return false;
    }

    public boolean markTaskAsCompleted(int id) {
        // Primeiro, tenta atualizar o status no banco de dados
        if (DatabaseHelper.updateTaskStatus(id, true)) {
            // Se atualizado no banco com sucesso, atualiza também na lista em memória
            Optional<Task> taskOptional = findTaskById(id); // Busca na lista em memória
            if (taskOptional.isPresent()) {
                taskOptional.get().setCompleted(true);
                System.out.println("Tarefa com ID " + id + " também marcada como concluída na lista em memória.");
                return true;
            } else {
                System.err.println("Tarefa com ID " + id + " atualizada no banco, mas não encontrada na lista em memória para marcar.");
                return true;
            }
        }
        // Se não conseguiu atualizar no banco, retorna false
        return false;
    }
}