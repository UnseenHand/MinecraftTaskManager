package net.unseenhand.taskmanagermod.data;

public class TaskListFilter {
    public static final TaskListFilter EMPTY = new TaskListFilter(false, false);
    private String searchTerm;
    private boolean byName;
    private boolean byStatus;

    public TaskListFilter(String searchTerm, boolean byName, boolean byStatus) {
        this(byName, byStatus);
        this.searchTerm = searchTerm;
    }

    public TaskListFilter(boolean byName, boolean byStatus) {
        this.byName = byName;
        this.byStatus = byStatus;
    }

    public boolean isByName() {
        return byName;
    }

    public void setByName(boolean byName) {
        this.byName = byName;
    }

    public boolean isByStatus() {
        return byStatus;
    }

    public void setByStatus(boolean byStatus) {
        this.byStatus = byStatus;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
