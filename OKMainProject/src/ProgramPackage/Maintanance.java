package ProgramPackage;

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

    public int getTime_delay() {
        return time_delay;
    }

    public void setTime_delay(int time_delay) {
        this.time_delay = time_delay;
    }
}
