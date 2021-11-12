import java.util.Comparator;

public class Politica {

    private final Comparator<Task> comparablePolice;

    Politica(Comparator<Task> comparablePolice) {
        this.comparablePolice = comparablePolice;
    }

    private static Comparator<Task> alphabeticComparator() {
        return Comparator.comparing(Task::getName);
    }

    public Comparator<Task> getComparator(){
        return this.comparablePolice;
    }
}
