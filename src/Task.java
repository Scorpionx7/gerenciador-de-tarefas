public class Task {
    private int id;
    private String title;
    private String description;
    private boolean isCompleted;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }

    public Task(int id, String title, String description, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "ID: " + (id == 0 ? "N/A (Não Salvo)" : id) +
                ", Título: '" + title + '\'' +
                ", Descrição: '" + description + '\'' +
                ", Concluída: " + (isCompleted ? "Sim" : "Não");
    }
}