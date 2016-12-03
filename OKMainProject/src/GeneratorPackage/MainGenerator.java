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
        System.out.printf("Podaj rozmiar instancji.\n");
        size = scanner.nextInt();
        System.out.printf("Podaj maksymalny czas trwiania części zadania.\n");
        max_task_time = scanner.nextInt();

        System.out.println("Tworzenie list zadań i przerw.");
        maintanance_count = (size / 8)+1;
        Task[] tasks = new Task[size];
        Maintanance[] maintanances = new Maintanance[maintanance_count];

        System.out.println("Wypełnianie list.");
        all_time = fillTasksArray(tasks, max_task_time);
        fillMaintananceArray(maintanances, max_task_time, all_time);

        System.out.println("\nLista zadań.");
        DisplayTasks(tasks);
        System.out.println("\nLista przerw.");
        DisplayMaintanance(maintanances);
    }

    /**
     * Wyświetla listę przerw, dane odpowienio wg wzorca
     * numer maszyny, czas trwania przerwy, czas rozpoczęcia przerwy
     * @param maintanances -lista zadań
     */
    private static void DisplayMaintanance(Maintanance[] maintanances){
        for (int i = 0; i<maintanances.length; i++){
            System.out.println("nr zadania " + (i+1) + " | "
                                + maintanances[i].getNr_machine() + ";"
                                + maintanances[i].getTime() + ";"
                                + maintanances[i].getTime_start() + ";");
        }
    }

    /**
     * Wyświetla listę zadań, dane odpowiednio wg wzorca
     * czas operacji nr1, czas operacji nr2, maszyna opt1, maszyna opt2, czas rozpoczęcia opt1
     * @param tasks -lista zadań
     */
    private static void DisplayTasks(Task[] tasks){
        int i = 1;
        for (Task x: tasks) {
            System.out.println("nr przerwy " + (i++) + " | "
                            + x.getTime_part1() + ";"
                            + x.getTime_part2() + ";"
                            + x.getNr_machine_part1() + ";"
                            + x.getNr_machine_part2() + ";"
                            + x.getReady_time_part1() + ";");
        }
    }

    /**
     * Wypełnianie listy przerw, losowanie odpowiednich wartości i wywoływanie konstruktora klasy przerw
     * @param maintanances - lista przerw
     * @param max_time - maksymalny czas trwania przerwy
     * @param all_time - całkowity czas trwania zadań na maszynie 1
     */
    private static void fillMaintananceArray(Maintanance[] maintanances, int max_time, int all_time){
        Random random = new Random(System.currentTimeMillis());
        for (int i =0 ; i<maintanances.length; i++){
            boolean machine = false;
            int time, start_time, nr_mach;

            do {
                machine = random.nextBoolean();
                if (machine){
                    nr_mach = 1;
                }else {
                    nr_mach = 2;
                }
                time = random.nextInt(max_time);
                start_time = random.nextInt(all_time/4);
            }while(checkMaintananceRepeat(maintanances,nr_mach,time,start_time, i));

            Maintanance maintanance = new Maintanance(nr_mach,time,start_time);
            maintanances[i] = maintanance;
        }
    }

    /**
     * Sprawdzenie czy przerwy nie nachodzą na siebie
     * @param maintanances -lista przerw
     * @param nr_mach - numer maszyny przerwy do stworzenia
     * @param time - czas trwania przerwy do stworzenie
     * @param time_start - czas rozpoczęcie przerwy do stworzenia
     * @param size - aktualnie elementów
     * @return - wartość false=nie nachodzą || true=nachodzą
     */
    private static boolean checkMaintananceRepeat(Maintanance[] maintanances, int nr_mach, int time, int time_start, int size){
        for (int i = 0; i<size; i++){
            if (maintanances[i].getNr_machine() == nr_mach){
                if (time_start > (maintanances[i].getTime_start()+maintanances[i].getTime())
                        || (time_start+time)<maintanances[i].getTime_start() ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Wypełnienie listy zadań, losowanie odpowiednich wartości i wywołanie konstruktora klasy zadań
     * @param tasks - lista zadań
     * @param max_task_time - maksymalny czas trwania operacji zadania
     * @return - całkowity czas trwania zadań na maszynie 1
     */
    private static int fillTasksArray(Task[] tasks, int max_task_time){
        int all_time = 0;
        Random random = new Random(System.currentTimeMillis());
        for(int i =0; i<tasks.length; i++){
            int time1, time2, rdy_time1;
            byte nr_mac1, nr_mac2;
            boolean machine = false;
            time1 = random.nextInt(max_task_time) + 1;
            time2 = random.nextInt(max_task_time) + 1;
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
