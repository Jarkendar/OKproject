package GeneratorPackage;

/**
 * Created by Jarek on 2016-12-03.
 */
public class Maintanance {
    private static int nr_maintance = 0;
    private int nr_machine;
    private int time;
    private int time_start;

    /**
     * Konstruktor
     * @param nr_machine - numer maszyny
     * @param time - czas trwania przerwy
     * @param time_start - czas startu przerwy
     */
    public Maintanance(int nr_machine,
                       int time, int time_start) {
        this.nr_machine = nr_machine;
        this.time = time;
        this.time_start = time_start;
    }

    public static int getNr_maintance() {
        return nr_maintance;
    }

    public static void setNr_maintance(int nr_maintance) {
        Maintanance.nr_maintance = nr_maintance;
    }

    public int getNr_machine() {
        return nr_machine;
    }

    public void setNr_machine(int nr_machine) {
        this.nr_machine = nr_machine;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime_start() {
        return time_start;
    }

    public void setTime_start(int time_start) {
        this.time_start = time_start;
    }
}
