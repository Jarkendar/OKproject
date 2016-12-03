package GeneratorPackage;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by Jarek on 2016-12-03.
 */
public class MainGenerator {
    public static void main(String[] args) {
        final int machine_count = 2;
        int size = 0, max_task_time,
                maintanance_count = 0,
                all_time = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.printf("Podaj rozmiar instancji.");
        size = scanner.nextInt();
        System.out.printf("Podaj maksymalny czas trwiania części zadania.");
        max_task_time = scanner.nextInt();

        maintanance_count = (size / 8)+1;
        Task[] tasks = new Task[size];
        Maintanance[] maintanances = new Maintanance[maintanance_count];

        all_time = fillTasksArray(tasks, size, max_task_time);
        fillMaintananceArray(maintanances,machine_count, max_task_time, all_time);

        DisplayTasks(tasks);
        DisplayMaintanance(maintanances);
    }

    private static void DisplayMaintanance(Maintanance[] maintanances){
        for (int i = 0; i<maintanances.length; i++){
            System.out.println(i + ";" + maintanances[i].getNr_machine() + ";"
                                + maintanances[i].getTime() + ";"
                                + maintanances[i].getTime_start() + ";");
        }
    }

    private static void DisplayTasks(Task[] tasks){
        for (Task x: tasks) {
            System.out.println(x.getTime_part1() + ";"
                            + x.getTime_part2() + ";"
                            + x.getNr_machine_part1() + ";"
                            + x.getNr_machine_part2() + ";"
                            + x.getReady_time_part1() + ";");
        }
    }

    private static void fillMaintananceArray(Maintanance[] maintanances, int size, int max_time, int all_time){
        Random random = new Random(System.currentTimeMillis());
        for (int i =0 ; i<size; i++){
            boolean machine = false;
            int time, start_time, nr_mach;
            machine = random.nextBoolean();
            if (machine){
                nr_mach = 1;
            }else {
                nr_mach = 2;
            }
            time = random.nextInt(max_time);
            start_time = random.nextInt(all_time/4);
            Maintanance maintanance = new Maintanance(nr_mach,time,start_time);
            maintanances[i] = maintanance;
        }
    }

    private static int fillTasksArray(Task[] tasks, int size, int max_task_time){
        int all_time = 0;
        Random random = new Random(System.currentTimeMillis());
        for(int i =0; i<size; i++){
            int time1, time2, rdy_time1;
            byte nr_mac1, nr_mac2;
            boolean machine = false;
            time1 = random.nextInt(max_task_time);
            time2 = random.nextInt(max_task_time);
            all_time += time1/4;
            rdy_time1 = random.nextInt(all_time);
            machine = random.nextBoolean();
            if (machine){
                nr_mac1 = 1;
                nr_mac2 = 2;
            } else{
                nr_mac1 = 2;
                nr_mac2 = 1;
            }
            Task task = new Task(time1,time2,nr_mac1, nr_mac2, rdy_time1);
            tasks[i] = task;
        }
        return all_time;
    }

}
