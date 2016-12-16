package ProgramPackage.TaskPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public abstract class Task {
    private int number_task;
    private int time_start; //zmienna potrzebna do tworzenia rozwiązań
    private int duration;
    private byte machine_number;
    private String task_name;
    /**
     * Konstruktor klasy Task
     * @param number_task - numer zadania
     * @param duration - czas trwania
     * @param machine_number - numer maszyny
     * @param task_name - nazwa zadania (part1, part2, maintanance)
     */
    public Task(int number_task, int duration, byte machine_number, String task_name) {
        this.number_task = number_task;
        this.duration = duration;
        this.machine_number = machine_number;
        this.task_name = task_name;
    }

    public abstract PartFirst cloneFirst();
    public abstract PartSecond cloneSecond();
    public abstract Maintanance cloneMaintanance();

    public abstract int getTime_delay();

    public int getNumber_task() {
        return number_task;
    }

    public void setNumber_task(int number_task) {
        this.number_task = number_task;
    }

    public int getTime_start() {
        return time_start;
    }

    public void setTime_start(int time_start) {
        this.time_start = time_start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public byte getMachine_number() {
        return machine_number;
    }

    public void setMachine_number(byte machine_number) {
        this.machine_number = machine_number;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }
}
