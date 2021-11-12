public class Task {
    private String name;
    private int time;
    private int workTime;

    public Task(String name, int time){
        this.name = name;
        this.time = time;
        workTime = time;
    }

    public boolean doing(){
        workTime--;
        if(workTime == 0){
            workTime = time;
            return true;
        }
        return false;
    }

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return String.format("%s_%d", name,time);
    }
}
