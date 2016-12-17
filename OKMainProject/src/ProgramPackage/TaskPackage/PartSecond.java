package ProgramPackage.TaskPackage;

/**
 * Created by Jarek on 2016-12-06.
 */
public class PartSecond extends Task {

    /**
     * Konstruktor klasy PartSecond
     * @param number_task - numer zadania
     * @param duration - czas trwania
     * @param machine_number - numer maszyny
     * @param task_name - nazwa zadania (part1, part2, maintanance)
     */
    public PartSecond(int number_task, int duration, byte machine_number, String task_name) {
        super(number_task, duration, machine_number, task_name);
    }



    public PartSecond cloneSecond(){
        PartSecond new_object = new PartSecond(this.getNumber_task(), this.getDuration()
                ,this.getMachine_number(),this.getTask_name());
        new_object.setTime_start(this.getTime_start());
        return new_object;
    }

    @Override
    public PartFirst cloneFirst() {
        return null;
    }

    @Override
    public Maintanance cloneMaintanance() {
        return null;
    }

    public int getTime_delay(){ return -1;}
}
