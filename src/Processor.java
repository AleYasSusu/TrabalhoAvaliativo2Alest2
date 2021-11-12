
public class Processor {
    private boolean isOccuped;
    private Task task;

    public Processor(){
        task = null;
        isOccuped = false;
    }

    public void setTask(Task task){
        this.task = task;
    }

    public Task getTask(){
        return task;
    }

    public boolean isOccuped(){
        return isOccuped;
    }

    public void setOccuped(boolean isOccuped){
        this.isOccuped = isOccuped;
    }

    @Override
    public String toString(){
        return String.format("isOccuped: %b\nTask: %s\n", isOccuped,task.toString());
    }

}