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

        //tworzenie ścieżki do plików
        path_to_instance = createPathToInstance(size, inscance_number);

        //wczytanie tablicy zadań i przerw z pliku
        Task[] tasks = readInstanceFromFile(path_to_instance, count_object);
        Maintanance[] maintanances = readMaintananceFromFile(path_to_instance, count_object);

        displayTest(maintanances,tasks);

        generatorRandomSolution(tasks,maintanances);
    }

    /**
     * Wyświetlenie wynikowych list
     * @param machine1
     * @param machine2
     */
    private static void dislpaySolution(LinkedList<Task> machine1, LinkedList<Task> machine2){
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


    private static void generatorRandomSolution(Task[] tasks, Maintanance[] maintanances) {
        LinkedList<Task> machine1 = new LinkedList<>();
        LinkedList<Task> machine2 = new LinkedList<>();
        Random random = new Random(System.currentTimeMillis());
        /**
         * Uwzględnienie wszystkich przerw w rozwiązaniu
         */
        for (int i = 0; i < maintanances.length; i++) {
            int number_on_list = 0;
            maintanances[i].setTime_start(maintanances[i].getTime_delay());
            if (maintanances[i].getMachine_number() == 1) {
                /**
                 * Sprawdzenie numeru miejsca w liście machine1
                 */
                for (Task x : machine1) {
                    if (x.getTime_start() < maintanances[i].getTime_start()) {
                        number_on_list++;
                    }
                }
                machine1.add(number_on_list, maintanances[i]);
            } else {
                /**
                 * Sprawdzenie numeru miejsca w liście machine2
                 */
                for (Task x : machine2) {
                    if (x.getTime_start() < maintanances[i].getTime_start()) {
                        number_on_list++;
                    }
                }
                machine2.add(number_on_list, maintanances[i]);
            }
        }
        dislpaySolution(machine1, machine2);

        boolean[] tasks_uses_test = new boolean[tasks.length];
        for (boolean x : tasks_uses_test){
            x = false;
        }

        while (checkAllTasksWasUses(tasks_uses_test)) {
            int number_on_list = 0;
            int choose_task_from_array = random.nextInt(tasks.length);
            /**
             * Na maszynie numer 1
             */
            if (!tasks_uses_test[choose_task_from_array]) {
                if (tasks[choose_task_from_array].getMachine_number() == 1) {
                    /**
                     * Część pierwsza zadania
                     */
                    if (tasks[choose_task_from_array].getTask_name().equals("part1")) {
                        /**
                         * Sprawdzanie miejsca wstawienia w listę
                         */
                        for (int k = 0; k < machine1.size(); k++) {
                            if (k == 0 && machine1.get(0).getTime_start() >= tasks[choose_task_from_array].getDuration()
                                    + tasks[choose_task_from_array].getTime_delay()) {
                                tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                                machine1.add(0, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            if ((machine1.get(k).getTime_start() + machine1.get(k).getDuration())
                                    < tasks[choose_task_from_array].getTime_delay()) {
                                number_on_list++;
                            } else if (k == machine1.size() - 1) {
                                tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start()
                                        + machine1.get(k).getDuration());
                                machine1.addLast(tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            } else if (((machine1.get(k + 1).getTime_start()) - (machine1.get(k).getTime_start()
                                    + machine1.get(k).getDuration()))
                                    >= (tasks[choose_task_from_array].getDuration())) {
                                tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_delay()
                                        + machine1.get(k).getDuration());
                                machine1.add(number_on_list+1, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            } else {
                                number_on_list++;
                            }
                        }
                    } else if (tasks[choose_task_from_array].getTask_name().equals("part2")) {
                        boolean check = false;
                        Task tmp = null;
                        for (int k = 0; k < machine1.size(); k++) {
                            if (machine1.get(k).getNumber_task() == tasks[k].getNumber_task() && tasks[k].getTask_name().equals("part1")) {
                                tmp = machine1.get(k);
                                break;
                            }
                            check = true;
                        }
                        while (check) {
                            for (int k = 0; k < machine2.size(); k++) {
                                if (machine2.get(k).getNumber_task() == tasks[k].getNumber_task() && tasks[k].getTask_name().equals("part1")) {
                                    tmp = machine2.get(k);
                                    break;
                                }
                            }
                            check = false;
                        }
                        if (tmp != null) {
                            for (int k = 0; k < machine1.size(); k++) {
                                if (machine1.get(k).getTime_start() + machine1.get(k).getDuration() < tmp.getTime_start()
                                        + tmp.getDuration()) {
                                    number_on_list++;
                                } else if (k == machine1.size() - 1) {
                                    tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start()
                                            + machine1.get(k).getDuration());
                                    machine1.addLast(tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                } else if ((machine1.get(k + 1).getTime_start()) - (machine1.get(k).getTime_start() + machine1.get(k).getDuration())
                                        >= (tasks[choose_task_from_array].getDuration())) {
                                    tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start() + machine1.get(k).getDuration());
                                    machine1.add(number_on_list+1, tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                } else {
                                    number_on_list++;
                                }
                            }
                        }
                    }
                }
                /**
                 * Na maszynie numer 2
                 */
                if (tasks[choose_task_from_array].getMachine_number() == 2) {
                    /**
                     * Część pierwsza zadania
                     */
                    if (tasks[choose_task_from_array].getTask_name().equals("part1")) {
                        /**
                         * Sprawdzanie miejsca wstawienia w listę
                         */
                        for (int k = 0; k < machine2.size(); k++) {
                            if (k == 0 && machine2.get(0).getTime_start() >= tasks[choose_task_from_array].getDuration()
                                    + tasks[choose_task_from_array].getTime_delay()) {
                                tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                                machine2.add(0, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            if ((machine2.get(k).getTime_start() + machine2.get(k).getDuration())
                                    < tasks[choose_task_from_array].getTime_delay()) {
                                number_on_list++;
                            } else if (k == machine2.size() - 1) {
                                tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                        + machine2.get(k).getDuration());
                                machine2.addLast(tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            } else if (((machine2.get(k + 1).getTime_start()) - (machine2.get(k).getTime_start()
                                    + machine2.get(k).getDuration()))
                                    >= (tasks[choose_task_from_array].getDuration())) {
                                tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                        + machine2.get(k).getDuration());
                                machine2.add(number_on_list+1, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            } else {
                                number_on_list++;
                            }
                        }
                    } else if (tasks[choose_task_from_array].getTask_name().equals("part2")) {
                        boolean check = false;
                        Task tmp = null;
                        for (int k = 0; k < machine1.size(); k++) {
                            if (machine1.get(k).getNumber_task() == tasks[k].getNumber_task() && tasks[k].getTask_name().equals("part1")) {
                                tmp = machine1.get(k);
                                break;
                            }
                            check = true;
                        }
                        while (check) {
                            for (int k = 0; k < machine2.size(); k++) {
                                if (machine2.get(k).getNumber_task() == tasks[k].getNumber_task() && tasks[k].getTask_name().equals("part1")) {
                                    tmp = machine2.get(k);
                                    break;
                                }
                            }
                            check = false;
                        }
                        if (tmp != null) {
                            for (int k = 0; k < machine2.size(); k++) {
                                if (machine2.get(k).getTime_start() + machine2.get(k).getDuration()
                                        < tmp.getTime_start() + tmp.getDuration()) {
                                    number_on_list++;
                                } else if (k == machine2.size() - 1) {
                                    tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                            + machine2.get(k).getDuration());
                                    machine2.addLast(tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                } else if ((machine2.get(k + 1).getTime_start())
                                        - (machine2.get(k).getTime_start() + machine2.get(k).getDuration())
                                        >= (tasks[choose_task_from_array].getDuration())) {
                                    tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                            + machine2.get(k).getDuration());
                                    machine2.add(number_on_list+1, tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                } else {
                                    number_on_list++;
                                }
                            }
                        }
                    }
                }
            }
        }
        dislpaySolution(machine1,machine2);
    }

    /**
     * Wyświetla zadania i przerwy wraz ze wszystkimi danymi ich dotyczącymi.
     * @param maintanances - tablica przerw
     * @param tasks - tablica zadań
     */
    private static void displayTest(Maintanance[] maintanances, Task[] tasks){
        for (int i = 0; i<tasks.length; i++){
            System.out.println("Numer zadania " + tasks[i].getNumber_task() + "; nazwa operacji : " + tasks[i].getTask_name()
                    + "; czas operacji : " + tasks[i].getDuration() + "; numer maszyny : "+ tasks[i].getMachine_number()
                    + "; czas opóźnienia : "+ tasks[i].getTime_delay());
        }
        for (int i = 0; i<maintanances.length; i++){
            System.out.println("Numer przerwy " + maintanances[i].getNumber_task() + "; nazwa operacji : " + maintanances[i].getTask_name()
                    + "; czas przerwy : " + maintanances[i].getDuration() + "; numer maszyny : "+ maintanances[i].getMachine_number()
                    + "; czas opóźnienia : "+ maintanances[i].getTime_delay());
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
    private static Maintanance[] readMaintananceFromFile(String path, int task_count){
        Scanner scanner = null;
        try{
            scanner = new Scanner(new File(path));
        }catch (FileNotFoundException e){
            System.out.println(e);
        }
        Maintanance[] maintanances = new Maintanance[1+task_count/8];
        for (int i = 0; i<(task_count+2); i++){
            scanner.nextLine();
        }
        for (int i = 0; i<(1+task_count/8); i++){
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
