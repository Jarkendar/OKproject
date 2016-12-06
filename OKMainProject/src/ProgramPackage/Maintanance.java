package ProgramPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public class Maintanance extends SolutionElement{
    private int break_number;
    private byte machine_number;
    private int time_duration;
    private int time_start;

    /**
     * Konstruktor klasy Maintanance.
     * @param break_number - numer przerwy
     * @param machine_number - numer maszyny
     * @param time_duration - czas trwania przerwy
     * @param time_start - czas startu przerwy
     */
    public Maintanance(int break_number, byte machine_number, int time_duration, int time_start) {
        this.break_number = break_number;
        this.machine_number = machine_number;
        this.time_duration = time_duration;
        this.time_start = time_start;
    }

    public int getBreak_number() {
        return break_number;
    }

    public void setBreak_number(int break_number) {
        this.break_number = break_number;
    }

    public byte getMachine_number() {
        return machine_number;
    }

    public void setMachine_number(byte machine_number) {
        this.machine_number = machine_number;
    }

    public int getTime_duration() {
        return time_duration;
    }

    public void setTime_duration(int time_duration) {
        this.time_duration = time_duration;
    }

    public int getTime_start() {
        return time_start;
    }

    public void setTime_start(int time_start) {
        this.time_start = time_start;
    }
}
