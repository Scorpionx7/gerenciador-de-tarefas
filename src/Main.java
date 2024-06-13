import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit){
            System.out.println("Menu:");
            System.out.println("1. Adicionar Tarefa:");
            System.out.println("2. Listar Tarefas:");
            System.out.println("3. Buscar Tarefas");
            System.out.println("4. Marcar Tarefa como Concluida");
            System.out.println("5. Remover Tarefa");
            System.out.println("6. Sair");
            System.out.println("Escolher uma opção:");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Nova Linha

            switch (choice){
                case 1:
                    System.out.println("Título de tarefa: ");
                    String title = scanner.nextLine();
                    System.out.println("Descrição de tarefa: ");
                    String description = scanner.nextLine();
                    taskManager.addTask(title, description);
                    break;
                case 2:
                    System.out.println("Lista de Tarefas: ");
                    for(Task task : taskManager.listTasks()){
                        System.out.println(task.getTitle() + ": " + task.getDescription()
                                + (task.isCompleted() ? "[Concluida]" : ""));
                    }
                    break;
                case 3:
                    System.out.println("Título da Tarefa a Buscar: ");
                    title = scanner.nextLine();
                    description = taskManager.searchTasks(title);
                    if(description != null){
                        System.out.println("Descrição: " + description);
                    } else {
                        System.out.println("Tarefa não encontrada");
                    }
                    break;
                case 4:
                    System.out.println("Título da Tarefa a Marcar como Concluida: ");
                    title = scanner.nextLine();
                    taskManager.markTaskAsCompleted(title);
                    break;
                case 5:
                    System.out.println("Titulo da Tarefa a Remover: ");
                    title = scanner.nextLine();
                    taskManager.removeTask(title);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Opção invalida.");
                    break;
            }
        }
        scanner.close();
        System.out.println("Sistema encerrado");

    }
}