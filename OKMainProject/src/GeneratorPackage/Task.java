package GeneratorPackage;

/**
 * Created by Jarek on 2016-12-03.
 */
public class Task {
    private int time_part1;
    private int time_part2;
    private byte nr_machine_part1;
    private byte nr_machine_part2;
    private int ready_time_part1;

    /**
     * Konstruktor
     * @param time_part1 - czas trwania części 1 zadania
     * @param time_part2 - czas trwania części 2 zadania
     * @param nr_machine_part1 - numer maszyny części 1
     * @param nr_machine_part2 - numer maszyny cześci 2
     * @param ready_time_part1 - czas rozpoczęcia części 1
     */
    public Task(int time_part1, int time_part2,
                byte nr_machine_part1,
                byte nr_machine_part2,
                int ready_time_part1) {
        this.time_part1 = time_part1;
        this.time_part2 = time_part2;
        this.nr_machine_part1 = nr_machine_part1;
        this.nr_machine_part2 = nr_machine_part2;
        this.ready_time_part1 = ready_time_part1;
    }

    public int getTime_part1() {
        return time_part1;
    }

    public void setTime_part1(int time_part1) {
        this.time_part1 = time_part1;
    }

    public int getTime_part2() {
        return time_part2;
    }

    public void setTime_part2(int time_part2) {
        this.time_part2 = time_part2;
    }

    public byte getNr_machine_part1() {
        return nr_machine_part1;
    }

    public void setNr_machine_part1(byte nr_machine_part1) {
        this.nr_machine_part1 = nr_machine_part1;
    }

    public byte getNr_machine_part2() {
        return nr_machine_part2;
    }

    public void setNr_machine_part2(byte nr_machine_part2) {
        this.nr_machine_part2 = nr_machine_part2;
    }

    public int getReady_time_part1() {
        return ready_time_part1;
    }

    public void setReady_time_part1(int ready_time_part1) {
        this.ready_time_part1 = ready_time_part1;
    }
}
