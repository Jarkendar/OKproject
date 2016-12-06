package ProgramPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public class PartFirst extends Task {
    private int time_delay;

    /**
     * Konstruktor klasy PartFirst z czasem opóźnienia pierwszego zadania.
     * @param number_task
     * @param duration
     * @param machine_number
     * @param part_number
     * @param time_delay - czas opóźnienia
     */
    public PartFirst(int number_task, int duration, byte machine_number, byte part_number, int time_delay) {
        super(number_task, duration, part_number, machine_number);
        this.time_delay = time_delay;
    }

    public int getTime_delay() {
        return time_delay;
    }

    public void setTime_delay(int time_delay) {
        this.time_delay = time_delay;
    }
}
