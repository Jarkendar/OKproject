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

        fillTasksArray(tasks, size, max_task_time);
        fillMaintananceArray(maintanances,machine_count);

    }

    private static void fillMaintananceArray(Maintanance[] maintanances, int size){
        Random random = new Random(System.currentTimeMillis());
        for (int i =0 ; i<size; i++){

        }
    }

    private static void fillTasksArray(Task[] tasks, int size, int max_task_time){
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

    }

}
