package ProgramPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public class PartFirst extends Task {
    private int time_delay;

    /**
     * Konstruktor klasy PartFirst
     * @param number_task
     * @param duration
     * @param machine_number
     * @param part_number
     * @param time_delay
     */
    public PartFirst(int number_task, int duration, byte machine_number, byte part_number, int time_delay) {
        super(number_task, duration, machine_number, part_number);
        this.time_delay = time_delay;
    }

    public int getTime_delay() {
        return time_delay;
    }

    public void setTime_delay(int time_delay) {
        this.time_delay = time_delay;
    }
}
