package ProgramPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public abstract class Task extends SolutionElement{
    private int number_task;
    private int time_start;
    private int duration;
    private byte machine_number;
    private byte part_number;

    /**
     * KOnstruktor klasy Task
     * @param number_task - numer zadania
     * @param duration - czas trwania
     * @param machine_number - numer maszyny
     * @param part_number - numer części zadania
     */
    public Task(int number_task, int duration, byte machine_number, byte part_number) {
        this.number_task = number_task;
        this.duration = duration;
        this.machine_number = machine_number;
        this.part_number = part_number;
    }

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

    public byte getPart_number() {
        return part_number;
    }

    public void setPart_number(byte part_number) {
        this.part_number = part_number;
    }
}
