package ProgramPackage.TaskPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public class PartFirst extends Task {
    private int time_delay;

    /**
     * Konstruktor klasy PartFirst
     * @param number_task - numer zadania
     * @param duration - czas trwania
     * @param machine_number - numer maszyny
     * @param task_name - nazwa zadania (part1, part2, maintanance)
     * @param time_delay - czas opóźnienia
     */
    public PartFirst(int number_task, int duration, byte machine_number, String task_name, int time_delay) {
        super(number_task, duration, machine_number, task_name);
        this.time_delay = time_delay;
    }

    public PartFirst cloneFirst(){
        PartFirst new_object = new PartFirst(this.getNumber_task(), this.getDuration()
                ,this.getMachine_number(),this.getTask_name(),this.getTime_delay());
        new_object.setTime_start(this.getTime_start());
        return new_object;
    }

    @Override
    public PartSecond cloneSecond() {
        return null;
    }

    @Override
    public Maintanance cloneMaintanance() {
        return null;
    }

    public int getTime_delay() {
        return time_delay;
    }

    public void setTime_delay(int time_delay) {
        this.time_delay = time_delay;
    }
}
