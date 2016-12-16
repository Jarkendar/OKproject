package ProgramPackage.TaskPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public class Maintanance extends Task{
    private int time_delay;

    /**
     * Konstruktor klasy Maintance
     * @param number_task - numer zadania
     * @param duration - czas trwania
     * @param machine_number - numer maszyny
     * @param task_name - nazwa zadania (part1, part2, maintanance)
     * @param time_delay - czas rozpoczęcia przerwy
     */
    public Maintanance(int number_task, int duration, byte machine_number, String task_name, int time_delay) {
        super(number_task, duration, machine_number, task_name);
        this.time_delay = time_delay;
    }

    @Override
    public PartFirst cloneFirst() {
        return null;
    }

    @Override
    public PartSecond cloneSecond() {
        return null;
    }

    @Override
    public Maintanance cloneMaintanance() {
        Maintanance new_object = new Maintanance(this.getNumber_task(),this.getDuration()
                ,this.getMachine_number(),this.getTask_name(),this.getTime_delay());
        return new_object;
    }

    public int getTime_delay() {
        return time_delay;
    }

    public void setTime_delay(int time_delay) {
        this.time_delay = time_delay;
    }
}
