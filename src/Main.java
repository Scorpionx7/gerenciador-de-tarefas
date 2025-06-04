import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();

        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Menu Gerenciador de Tarefas =====");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Buscar Tarefa por ID");
            System.out.println("4. Marcar Tarefa como Concluída");
            System.out.println("5. Remover Tarefa");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Consome a entrada inválida
                continue; // Volta para o início do loop
            }
            scanner.nextLine(); // Consome a nova linha restante do nextInt()

            switch (choice) {
                case 1:
                    System.out.print("Título da tarefa: ");
                    String title = scanner.nextLine();
                    System.out.print("Descrição da tarefa: ");
                    String description = scanner.nextLine();
                    taskManager.addTask(title, description);
                    System.out.println("Tarefa adicionada com sucesso!");
                    break;
                case 2:
                    System.out.println("\n--- Lista de Tarefas ---");
                    List<Task> tasks = taskManager.listTasks();
                    if (tasks.isEmpty()) {
                        System.out.println("Nenhuma tarefa cadastrada.");
                    } else {
                        for (Task task : tasks) {
                            System.out.println(task); // Usa o toString() da Task
                        }
                    }
                    System.out.println("------------------------");
                    break;
                case 3:
                    System.out.print("Digite o ID da Tarefa a Buscar: ");
                    try {
                        int searchId = scanner.nextInt();
                        scanner.nextLine(); // Consome a nova linha
                        Optional<Task> foundTask = taskManager.findTaskById(searchId);
                        if (foundTask.isPresent()) {
                            System.out.println("Tarefa encontrada: " + foundTask.get());
                        } else {
                            System.out.println("Tarefa com ID " + searchId + " não encontrada.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("ID inválido. Por favor, digite um número.");
                        scanner.nextLine(); // Consome a entrada inválida
                    }
                    break;
                case 4:
                    System.out.print("Digite o ID da Tarefa a Marcar como Concluída: ");
                    try {
                        int completeId = scanner.nextInt();
                        scanner.nextLine(); // Consome a nova linha
                        if (taskManager.markTaskAsCompleted(completeId)) {
                            System.out.println("Tarefa com ID " + completeId + " marcada como concluída.");
                        } else {
                            System.out.println("Não foi possível marcar a tarefa com ID " + completeId + " como concluída (Tarefa não encontrada?).");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("ID inválido. Por favor, digite um número.");
                        scanner.nextLine(); // Consome a entrada inválida
                    }
                    break;
                case 5:
                    System.out.print("Digite o ID da Tarefa a Remover: ");
                    try {
                        int removeId = scanner.nextInt();
                        scanner.nextLine(); // Consome a nova linha
                        if (taskManager.removeTask(removeId)) {
                            System.out.println("Tarefa com ID " + removeId + " removida com sucesso.");
                        } else {
                            System.out.println("Não foi possível remover a tarefa com ID " + removeId + " (Tarefa não encontrada?).");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("ID inválido. Por favor, digite um número.");
                        scanner.nextLine(); // Consome a entrada inválida
                    }
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
        scanner.close();
        System.out.println("Sistema encerrado.");
    }
}