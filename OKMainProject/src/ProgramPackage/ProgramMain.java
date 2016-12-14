package ProgramPackage;

import ProgramPackage.TaskPackage.Maintanance;
import ProgramPackage.TaskPackage.PartFirst;
import ProgramPackage.TaskPackage.PartSecond;
import ProgramPackage.TaskPackage.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Jarek on 2016-12-06.
 */
public class ProgramMain {
    public static void main(String[] args) {
        int size = 0, inscance_number = 0;
        int count_object = 0, count_maintanance = 0;
        String path_to_instance = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj rozmiar instancji do rozwiązania.");
        size = scanner.nextInt();
        System.out.println("Podaj numer instancji do rozwiązania.");
        inscance_number = scanner.nextInt();

        count_object = size;
        count_maintanance = 1 + size/8;


        for (int k = 5; k<=50 ; k+=5) {
            size = k;
            for (int j =1; j<=100; j++) {
                inscance_number = j;

                System.out.println("size " + size+ " numer instancji "+ inscance_number);


                //tworzenie ścieżki do plików
                path_to_instance = createPathToInstance(size, inscance_number);
                count_object =size;
                //wczytanie tablicy zadań i przerw z pliku
                Task[] tasks = readInstanceFromFile(path_to_instance, count_object);
                Maintanance[] maintanances = readMaintananceFromFile(path_to_instance, count_object);

        //        displayTest(maintanances, tasks);


                /**
                 * Tworzenie instancji wejściowych i wypełnieniej jej losowymi rozwiązaniami
                 */


                Solution[] solutions = new Solution[10];
                for (int i = 0; i < 10; i++) {
                    Task[] tasks_clone = tasks.clone();
                    Maintanance[] maintanances_clone = maintanances.clone();
                    System.out.println("Numer instacji " + (i + 1));
                    solutions[i] = generatorV2(tasks_clone, maintanances_clone);
                    solutions[i].displayMachine1();
                    solutions[i].displayMachine2();
                    solutions[i].setFunction_target();
                    System.out.println("Czas funkcji celu : " + solutions[i].getFunction_target());
                }
            }
        }
    }

    /**
     * Sprawdza czy wszytskie zadania zostały użyte podczas generowania instancji losowej.
     * true -  należy powtórzyć pętle
     * false - wszystkie zadania zostały wykorzystane
     * @param booleans - tablica użytych zadań
     * @return
     */
    private static boolean checkAllTasksWasUses(boolean[] booleans){
        for (int i =0; i<booleans.length; i++){
            if (!booleans[i]) return true;
        }
        return false;
    }

    private static void displayTest(LinkedList<Task> machine1, LinkedList<Task> machine2){
        System.out.println("Maszyna nr 1");
        for (Task x: machine1){
            System.out.println("nazwa operacji : " + x.getTask_name() + " " + x.getNumber_task()
                    + " ; czas pracy : " + x.getTime_start()
                    + " - " + (x.getTime_start()+x.getDuration()));
        }
        System.out.println("Maszyna nr 2");
        for (Task x: machine2){
            System.out.println("nazwa operacji : " + x.getTask_name() + " " + x.getNumber_task()
                    + " ; czas pracy : " + x.getTime_start()
                    + " - " + (x.getTime_start()+x.getDuration()));
        }
    }

    private static Solution generatorV2(Task[] tasks, Maintanance[] maintanances){
        LinkedList<Task> machine1 = new LinkedList<>();
        LinkedList<Task> machine2 = new LinkedList<>();

        Random random = new Random(System.currentTimeMillis());
        /**
         * Uwzględnienie wszystkich przerw w rozwiązaniu
         */
        boolean[] main_uses = new boolean[maintanances.length];
        for (int i = 0 ; i< main_uses.length; i++) main_uses[i] = false;

        while (checkAllTasksWasUses(main_uses)){
            int wylosowana = random.nextInt(maintanances.length);

            if (!main_uses[wylosowana]){
                /**
                 * na maszynie nr 1
                 */
                if (maintanances[wylosowana].getMachine_number() == 1){
                    /**
                     * jeśli lista jest jeszcze pusta
                     */
                    if (machine1.size() == 0){
                        maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                        machine1.addFirst(maintanances[wylosowana]);
                        main_uses[wylosowana] = true;
                    }
                    /**
                     * jeśli na liście coś jest
                     */
                    else {
                        for (int i = 0; i<machine1.size(); i++){
                            /**
                             * Zmieści się przed pierwszym zadaniem.
                             */
                            if (machine1.get(0).getTime_delay() > maintanances[wylosowana].getTime_delay()) {
                                maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                                machine1.addFirst(maintanances[wylosowana]);
                                main_uses[wylosowana] = true;
                                break;
                            }
                            /**
                             * Jeśli zmieści się pomiędzy dwoma zadaniami
                             */
                            else if (i < machine1.size()-1 && machine1.get(i).getTime_delay() > (maintanances[wylosowana].getTime_delay())) {
                                maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                                machine1.add(i, maintanances[wylosowana]);
                                main_uses[wylosowana] = true;
                                break;
                            }
                            /**
                             * Przejrzeliśmy całą maszynę
                             */
                            else if (i == machine1.size() - 1) {
                                maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                                if (machine1.get(i).getTime_delay() > maintanances[wylosowana].getTime_delay()){
                                    machine1.add(i, maintanances[wylosowana]);
                                    main_uses[wylosowana] = true;
                                    break;
                                }else {
                                    machine1.addLast(maintanances[wylosowana]);
                                    main_uses[wylosowana] = true;
                                    break;
                                }
                            }
                        }
                    }
                }else if (maintanances[wylosowana].getMachine_number() == 2){
                    if (machine2.size() == 0){
                        maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                        machine2.addFirst(maintanances[wylosowana]);
                        main_uses[wylosowana] = true;
                    }else {
                        for (int i = 0; i<machine2.size(); i++){
                            /**
                             * Zmieści się przed pierwszym zadaniem.
                             */
                            if (machine2.get(0).getTime_delay() > maintanances[wylosowana].getTime_delay()) {
                                maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                                machine2.addFirst(maintanances[wylosowana]);
                                main_uses[wylosowana] = true;
                                break;
                            }
                            /**
                             * Jeśli zmieści się pomiędzy dwoma zadaniami
                             */
                            else if (i < machine2.size()-1 && machine2.get(i).getTime_delay() > (maintanances[wylosowana].getTime_delay())) {
                                maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                                machine2.add(i, maintanances[wylosowana]);
                                main_uses[wylosowana] = true;
                                break;
                            }
                            /**
                             * Przejrzeliśmy całą maszynę
                             */
                            else if (i == machine2.size() - 1) {
                                maintanances[wylosowana].setTime_start(maintanances[wylosowana].getTime_delay());
                                if (machine2.get(i).getTime_delay() > maintanances[wylosowana].getTime_delay()){
                                    machine2.add(i,maintanances[wylosowana]);
                                    main_uses[wylosowana] = true;
                                    break;
                                }else {
                                    machine2.addLast(maintanances[wylosowana]);
                                    main_uses[wylosowana] = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        displayTest(machine1,machine2);


        int instance_size = tasks.length/2;
        boolean[] tasks_uses_test = new boolean[tasks.length];
        for (int i = 0 ; i< tasks_uses_test.length; i++) tasks_uses_test[i] = false;



        while(checkAllTasksWasUses(tasks_uses_test)){
            int wylosowana = random.nextInt(instance_size*2);
            /**
             * Wylosowana część pierwsza zadania
             */
            if (!tasks_uses_test[wylosowana]) {
                if (tasks[wylosowana].getTask_name().equals("part1")) {
                    if (tasks[wylosowana].getMachine_number() == 1) {
                        /**
                         * Przypadek kiedy maszyna jest pusta.
                         */
                        if (machine1.size() == 0) {
                            tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                            machine1.addFirst(tasks[wylosowana]);
                            tasks_uses_test[wylosowana] = true;
                        }else {
                            /**
                             * Szukanie miejsca wpisania zadania.
                             */
                            for (int i = 0; i < machine1.size(); i++) {
                                /**
                                 * Zmieści się przed pierwszym zadaniem.
                                 */
                                if (machine1.get(0).getTime_start() >= (tasks[wylosowana].getTime_delay() + tasks[wylosowana].getDuration())) {
                                    tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                    machine1.addFirst(tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zmieści się pomiędzy dwoma zadaniami
                                 */
                                else if (i < machine1.size() - 1 && (machine1.get(i + 1).getTime_start()
                                        - (machine1.get(i).getTime_start() + machine1.get(i).getDuration())
                                        >= (tasks[wylosowana].getDuration()) && (machine1.get(i).getTime_start() + machine1.get(i).getDuration())
                                        >= (tasks[wylosowana].getTime_delay()))) {
                                    tasks[wylosowana].setTime_start(machine1.get(i).getTime_start() + machine1.get(i).getDuration());
                                    machine1.add(i + 1, tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Przejrzeliśmy całą maszynę
                                 */
                                else if (i == machine1.size() - 1) {
                                    /**
                                     * Jeśli czas opóźnienia większy od czasu zakończenia ostatniego zadania.
                                     */
                                    if (tasks[wylosowana].getTime_delay() > machine1.get(i).getTime_start() + machine1.get(i).getDuration()) {
                                        tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                        machine1.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    } else {
                                        tasks[wylosowana].setTime_start(machine1.get(i).getTime_start() + machine1.get(i).getDuration());
                                        machine1.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    /**
                     * Część piersza na maszynie 2.
                     */
                    else if (tasks[wylosowana].getMachine_number() == 2) {
                        /**
                         * Przypadek kiedy maszyna jest pusta.
                         */
                        if (machine2.size() == 0) {
                            tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                            machine2.addFirst(tasks[wylosowana]);
                            tasks_uses_test[wylosowana] = true;
                        }
                        else {
                            /**
                             * Szukanie miejsca wpisania zadania.
                             */
                            for (int i = 0; i < machine2.size(); i++) {
                                /**
                                 * Zmieści się przed pierwszym zadaniem.
                                 */
                                if (machine2.get(0).getTime_start() >= (tasks[wylosowana].getTime_delay() + tasks[wylosowana].getDuration())) {
                                    tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                    machine2.addFirst(tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zmieści się pomiędzy dwoma zadaniami
                                 */
                                else if (i < machine2.size() - 1 && (((machine2.get(i + 1).getTime_start()
                                        - (machine2.get(i).getTime_start() + machine2.get(i).getDuration()))
                                        >= (tasks[wylosowana].getDuration()))
                                        && ((machine2.get(i).getTime_start() + machine2.get(i).getDuration())
                                        >= (tasks[wylosowana].getTime_delay())))) {
                                    tasks[wylosowana].setTime_start(machine2.get(i).getTime_start() + machine2.get(i).getDuration());
                                    machine2.add(i + 1, tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Przejrzeliśmy całą maszynę
                                 */
                                else if (i == machine2.size() - 1) {
                                    /**
                                     * Jeśli czas opóźnienia większy od czasu zakończenia ostatniego zadania.
                                     */
                                    if (tasks[wylosowana].getTime_delay() > (machine2.get(i).getTime_start() + machine2.get(i).getDuration())) {
                                        tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                        machine2.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    } else {
                                        tasks[wylosowana].setTime_start(machine2.get(i).getTime_start() + machine2.get(i).getDuration());
                                        machine2.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
                /**
                 * Wylosowana część druga zadania i część pierwsza została użyta.
                 */
                else if (tasks_uses_test[wylosowana - instance_size] && tasks[wylosowana].getTask_name().equals("part2")) {
                    Task tmp = checkPartFirst(machine1, machine2, tasks[wylosowana].getNumber_task());
                    int part2_delay_time = (tmp.getTime_start() + tmp.getDuration());

                    /**
                     * Część druga na maszynie nr 1
                     */
                    if (tasks[wylosowana].getMachine_number() == 1) {
                        /**
                         * Przypadek kiedy maszyna jest pusta.
                         */
                        if (machine1.size() == 0) {
                            tasks[wylosowana].setTime_start(part2_delay_time);
                            machine1.addFirst(tasks[wylosowana]);
                            tasks_uses_test[wylosowana] = true;
                        }
                        else {
                            /**
                             * Szukanie miejsca wpisania zadania.
                             */
                            for (int i = 0; i < machine1.size(); i++) {
                                /**
                                 * Zmieści się przed pierwszym zadaniem.
                                 */
                                if (machine1.get(0).getTime_start() >= (part2_delay_time + tasks[wylosowana].getDuration())) {
                                    tasks[wylosowana].setTime_start(part2_delay_time);
                                    machine1.addFirst(tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zmieści się pomiędzy dwoma zadaniami
                                 */
                                else if (i < machine1.size() - 1 && (machine1.get(i + 1).getTime_start()
                                        - (machine1.get(i).getTime_start() + machine1.get(i).getDuration())
                                        >= (tasks[wylosowana].getDuration())
                                        && (machine1.get(i).getTime_start() + machine1.get(i).getDuration())
                                        >= (part2_delay_time))) {
                                    tasks[wylosowana].setTime_start(machine1.get(i).getTime_start() + machine1.get(i).getDuration());
                                    machine1.add(i + 1, tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Przejrzeliśmy całą maszynę
                                 */
                                else if (i == machine1.size() - 1) {
                                    /**
                                     * Jeśli czas opóźnienia większy od czasu zakończenia ostatniego zadania.
                                     */
                                    if (part2_delay_time > machine1.get(i).getTime_start() + machine1.get(i).getDuration()) {
                                        tasks[wylosowana].setTime_start(part2_delay_time);
                                        machine1.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    } else {
                                        tasks[wylosowana].setTime_start(machine1.get(i).getTime_start() + machine1.get(i).getDuration());
                                        machine1.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    /**
                     * Część druga na maszynie 2.
                     */
                    else if (tasks[wylosowana].getMachine_number() == 2) {
                        /**
                         * Przypadek kiedy maszyna jest pusta.
                         */
                        if (machine2.size() == 0) {
                            tasks[wylosowana].setTime_start(part2_delay_time);
                            machine2.addFirst(tasks[wylosowana]);
                            tasks_uses_test[wylosowana] = true;
                        }
                        else {
                            /**
                             * Szukanie miejsca wpisania zadania.
                             */
                            for (int i = 0; i < machine2.size(); i++) {
                                /**
                                 * Zmieści się przed pierwszym zadaniem.
                                 */
                                if (machine2.get(0).getTime_start() >= (part2_delay_time + tasks[wylosowana].getDuration())) {
                                    tasks[wylosowana].setTime_start(part2_delay_time);
                                    machine2.addFirst(tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zmieści się pomiędzy dwoma zadaniami
                                 */
                                else if (i < machine2.size() - 1 && (machine2.get(i + 1).getTime_start()
                                        - (machine2.get(i).getTime_start() + machine2.get(i).getDuration())
                                        >= (tasks[wylosowana].getDuration())
                                        && (machine2.get(i).getTime_start() + machine2.get(i).getDuration())
                                        >= (part2_delay_time))) {
                                    tasks[wylosowana].setTime_start(machine2.get(i).getTime_start() + machine2.get(i).getDuration());
                                    machine2.add(i + 1, tasks[wylosowana]);
                                    tasks_uses_test[wylosowana] = true;
                                    break;
                                }
                                /**
                                 * Przejrzeliśmy całą maszynę
                                 */
                                else if (i == machine2.size() - 1) {
                                    /**
                                     * Jeśli czas opóźnienia większy od czasu zakończenia ostatniego zadania.
                                     */
                                    if (part2_delay_time > machine2.get(i).getTime_start() + machine2.get(i).getDuration()) {
                                        tasks[wylosowana].setTime_start(part2_delay_time);
                                        machine2.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    } else {
                                        tasks[wylosowana].setTime_start(machine2.get(i).getTime_start() + machine2.get(i).getDuration());
                                        machine2.addLast(tasks[wylosowana]);
                                        tasks_uses_test[wylosowana] = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Solution solution = new Solution(machine1,machine2);
    return solution;
    }

    /**
     * Wyciąga część pierwszą zadania.
     * @param machine1 - lista maszyny 1
     * @param machine2 - lista maszyny 2
     * @param number_task - numer zadania
     * @return - zwraca obiekt części pierwszej
     */
    private static Task checkPartFirst(LinkedList<Task> machine1, LinkedList<Task> machine2, int number_task){
        /**
         * Sprawdzenie czy część pierwsza zadania była już na maszynach
         */
        for (Task aMachine1 : machine1) {
            if (aMachine1.getNumber_task() == number_task && aMachine1.getTask_name().equals("part1")) {
                return aMachine1;
            }
        }
        for (Task aMachine2 : machine2) {
            if (aMachine2.getNumber_task() == number_task && aMachine2.getTask_name().equals("part1")) {
                return aMachine2;
            }
        }
        return null;
    }

    /**
     * Wyświetla zadania i przerwy wraz ze wszystkimi danymi ich dotyczącymi.
     * @param maintanances - tablica przerw
     * @param tasks - tablica zadań
     */
    private static void displayTest(Maintanance[] maintanances, Task[] tasks){
        for (Task task : tasks) {
            System.out.println("Numer zadania " + task.getNumber_task() + "; nazwa operacji : " + task.getTask_name()
                    + "; czas operacji : " + task.getDuration() + "; numer maszyny : " + task.getMachine_number()
                    + "; czas opóźnienia : " + task.getTime_delay());
        }
        for (Maintanance maintanance : maintanances) {
            System.out.println("Numer przerwy " + maintanance.getNumber_task() + "; nazwa operacji : " + maintanance.getTask_name()
                    + "; czas przerwy : " + maintanance.getDuration() + "; numer maszyny : " + maintanance.getMachine_number()
                    + "; czas opóźnienia : " + maintanance.getTime_delay());
        }
    }

    /**
     * Generowanie ścieżki do pliku z zadaną instancją, brak zabezpieczeń przed brakiem konkretnej instancji.
     * @param size - rozmiar = folder zawierający
     * @param number - numer = konkretny numer instancji wejściowej
     * @return - zwraca ścieżkę do konkretnej instancji
     */
    private static String createPathToInstance(int size, int number){
        return ("Instancje\\rozmiar "+size+ "\\nr "+ number +".txt");
    }

    /**
     * Wczytuje tablice zadań z pliku.
     * Oznaczenia zadań :  część pierwsza - "part1", część druga - "part2", przerwa - "maintanance".
     * @param path - ścieżka dostępu do pliku
     * @param count_objects - rozmiar instancji, ilość zadań
     * @return - zwraca tablicę zadań o rozmiarze = rozmiar instancji *2, wypełnioną obiektami części poszczególnych zadań
     */
    private static Task[] readInstanceFromFile(String path, int count_objects) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
        }catch (FileNotFoundException e){
            System.out.println(e);
        }

        System.out.println(path);
        Task[] tasks = new Task[count_objects*2];
        scanner.nextLine();
        scanner.nextLine();
        for (int i = 0; i<count_objects; i++){
            String tmp = scanner.nextLine();
            String[] strings_array = null;
            strings_array = tmp.split(";");

            System.out.println(strings_array[0] + " " +strings_array[1] + " "
                    +strings_array[2] + " " +strings_array[3] + " " + strings_array[4]);
            System.out.println(Integer.parseInt(strings_array[0]) + " " + Integer.parseInt(strings_array[1])+ " "
                    +Byte.parseByte(strings_array[2]) + " " + Byte.parseByte(strings_array[3]) + " "+ Integer.parseInt(strings_array[4]));

            PartFirst partFirst = new PartFirst((i+1), Integer.parseInt(strings_array[0]), Byte.parseByte(strings_array[2])
                    , "part1", Integer.parseInt(strings_array[4]));
            PartSecond partSecond = new PartSecond((i+1), Integer.parseInt(strings_array[1]), Byte.parseByte(strings_array[3])
                    , "part2");
            tasks[i] = partFirst;
            tasks[i+count_objects] = partSecond;
        }
        return tasks;
    }

    /**
     * Wczytanie Maintanance'ów z pliku
     * @param path - ścieżka do pliku instancji
     * @param task_count - liczba zadań, tu służy do przesunięcia głowicy
     * @return - zwraca tablicę przerw
     */
    private static Maintanance[] readMaintananceFromFile(String path, int task_count) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        Maintanance[] maintanances = new Maintanance[1 + task_count / 8];
        for (int i = 0; i < (task_count + 2); i++) {
            scanner.nextLine();
        }
        for (int i = 0; i < (1 + task_count / 8); i++) {
            String tmp = scanner.nextLine();
            tmp = tmp.substring(11);
            String[] strings_array = tmp.split(";");
            Maintanance maintanance = new Maintanance(Integer.parseInt(strings_array[0]), Integer.parseInt(strings_array[2])
                    , Byte.parseByte(strings_array[1]), "maintanance", Integer.parseInt(strings_array[3]));
            maintanances[i] = maintanance;
        }
        return maintanances;
    }
}
