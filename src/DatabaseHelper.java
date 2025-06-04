import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // URL de conexão com o banco de dados SQLite.
    // "tarefas.db" será o nome do arquivo do banco de dados.
    private static String databaseUrl = "jdbc:sqlite:tarefas.db";

    public static void setDatabaseUrl(String url) {
        databaseUrl = url;
    }

     //Estabelece e retorna uma conexão com o banco de dados SQLite.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    /**
     * Inicializa o banco de dados.
     * Cria a tabela 'tasks' se ela não existir.
     */
    public static void initializeDatabase() {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // ID único, gerado pelo banco
                "title TEXT NOT NULL," +                   // Título da tarefa, não pode ser nulo
                "description TEXT," +                      // Descrição da tarefa
                "is_completed INTEGER NOT NULL DEFAULT 0" + // Status (0 para false, 1 para true)
                ");";

        try (Connection conn = getConnection(); // Try-with-resources para fechar a conexão automaticamente
             Statement stmt = conn.createStatement()) { // Usado para executar comandos SQL

            // Executa o comando de criação da tabela
            stmt.execute(sqlCreateTable);
            System.out.println("Banco de dados inicializado e tabela 'tasks' verificada/criada.");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public static int insertTask(Task task) {
        String sql = "INSERT INTO tasks(title, description, is_completed) VALUES(?,?,?)";
        int generatedId = -1;

        try (Connection conn = getConnection();
             // PreparedStatement é usado para executar queries parametrizadas e seguras
             // Statement.RETURN_GENERATED_KEYS permite recuperar o ID auto-incrementado
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.isCompleted() ? 1 : 0); // Converte boolean para int (1 ou 0)

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Tenta obter as chaves geradas (nosso ID auto-incrementado)
                try (var rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1); // Pega o primeiro ID gerado
                    }
                }
            }
            System.out.println("Tarefa '" + task.getTitle() + "' inserida no banco com ID: " + generatedId);
        } catch (SQLException e) {
            System.err.println("Erro ao inserir tarefa: " + e.getMessage());
        }
        return generatedId;
    }


    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, title, description, is_completed FROM tasks";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) { // ResultSet para guardar os resultados da query

            // Itera sobre cada linha do resultado
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                boolean isCompleted = rs.getInt("is_completed") == 1; // Converte int para boolean

                // Cria um objeto Task com os dados do banco
                tasks.add(new Task(id, title, description, isCompleted));
            }
            System.out.println(tasks.size() + " tarefas carregadas do banco.");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as tarefas: " + e.getMessage());
        }
        return tasks;
    }

    public static boolean updateTaskStatus(int taskId, boolean isCompleted) {
        String sql = "UPDATE tasks SET is_completed = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, isCompleted ? 1 : 0); // Converte boolean para int
            pstmt.setInt(2, taskId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Status da tarefa com ID " + taskId + " atualizado no banco para: " + isCompleted);
                return true;
            } else {
                System.out.println("Nenhuma tarefa encontrada com ID " + taskId + " para atualizar status.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da tarefa: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Tarefa com ID " + taskId + " removida do banco.");
                return true;
            } else {
                System.out.println("Nenhuma tarefa encontrada com ID " + taskId + " para remover.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover tarefa: " + e.getMessage());
            return false;
        }
    }
}