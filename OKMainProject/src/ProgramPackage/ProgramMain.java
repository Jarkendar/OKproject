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

        /**
         * Tworzenie instancji wejściowych i wypełnieniej jej losowymi rozwiązaniami
         */
        Solution[] solutions = new Solution[10];
        for (int i =0; i<10; i++){
            Task[] tasks_clone = tasks.clone();
            Maintanance[] maintanances_clone = maintanances.clone();
            System.out.println("Numer instacji " + (i+1));
            solutions[i] = generatorRandomSolution(tasks_clone,maintanances_clone);
            System.out.println("Czas funkcji celu : "+ solutions[i].getFunction_target());
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


    /**
     * Metoda generuje losowe rozwiązania, potrzebne algorytmowi mrówkowemu przy rozpoczęciu prac.
     * @param tasks - tablica zadań
     * @param maintanances - tablica przerw
     * @return - zwraca obiekt Solution zawierający przykładowe rozwiązanie
     */
    private static Solution generatorRandomSolution(Task[] tasks, Maintanance[] maintanances) {
        LinkedList<Task> machine1 = new LinkedList<>();
        LinkedList<Task> machine2 = new LinkedList<>();

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

        boolean[] tasks_uses_test = new boolean[tasks.length];
        for (int i = 0 ; i< tasks_uses_test.length; i++) tasks_uses_test[i] = false;

        Random random = new Random(System.currentTimeMillis());

        while (checkAllTasksWasUses(tasks_uses_test)) {

            int number_on_list = 0;
            int choose_task_from_array = random.nextInt(tasks.length);
            System.out.println("Numer wylosowany " + choose_task_from_array);

            /**
             * Na maszynie numer 1
             */
            if (!tasks_uses_test[choose_task_from_array]) {

                System.out.println("Warunek użycia zadania.");

                if (tasks[choose_task_from_array].getMachine_number() == 1) {

                    System.out.println("Warunek maszyny = maszyna 1");

                    /**
                     * Część pierwsza zadania
                     */
                    if (tasks[choose_task_from_array].getTask_name().equals("part1")) {

                        System.out.println("Warunek części pierwszej na maszynie 1 " + machine1.size());

                        /**
                         * Sprawdzanie miejsca wstawienia w listę
                         */
                        if (machine1.size() == 0){
                            tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                            tasks_uses_test[choose_task_from_array] == true;
                            machine1.add(tasks[choose_task_from_array]);
                        }

                        for (int k = 0; k < machine1.size(); k++) {
                            /**
                             * Jeśli część pierwsza zmieści się przed pierwszym zadaniem umieszczonym już w rozwiązaniu.
                             */
                            if (k == 0 && machine1.get(0).getTime_start() >= tasks[choose_task_from_array].getDuration()
                                    + tasks[choose_task_from_array].getTime_delay()) {

                                System.out.println("Warunek zmieszczenia się na pierwszym miejscu");

                                tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                                machine1.add(0, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            /**
                             * Jeśli opóźnienie części pierwszej jest większe od zakończenia się zadania to zwiększaj miejsce w liście
                             */
                            if ((machine1.get(k).getTime_start() + machine1.get(k).getDuration())
                                    < tasks[choose_task_from_array].getTime_delay()) {

                                System.out.println("Warunek czasu opóźnienia większego od zakończenia się zadania na maszynie");

                                number_on_list++;
                            }
                            /**
                             * Jeśli maszyna jest ustawiona na ostatnie zadanie
                             */
                            else if (k == machine1.size() - 1 || number_on_list >= machine1.size()) {

                                System.out.println("Warunek ustawienia na ostatni element maszyny");

                                /**
                                 * Jeśli czas opóźnienia części pierwszej jest większy od czasu zakończenia ostatniego zadania
                                 * to ustaw czas rozpoczęcia zadania na czas opóźnienia.
                                 */
                                if(tasks[choose_task_from_array].getTime_delay()
                                        > machine1.getLast().getTime_start() + machine1.getLast().getDuration()){

                                    System.out.println(1);

                                    tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                                }else {

                                    System.out.println(2);

                                    tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start()
                                            + machine1.get(k).getDuration());
                                }
                                machine1.addLast(tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            /**
                             * Jeśli część zadania zmieści się pomiędzy dwa zadania na maszynie.
                             */
                            else if (((machine1.get(k + 1).getTime_start()) - (machine1.get(k).getTime_start()
                                    + machine1.get(k).getDuration()))
                                    >= (tasks[choose_task_from_array].getDuration())) {

                                System.out.println("część zadania zmieści się pomiędzy dwa zadania na maszynie.");

                                tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start()
                                        + machine1.get(k).getDuration());
                                machine1.add(number_on_list+1, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            /**
                             * Jeśli część zadania nie zmieści się pomiędzy dwa zadania na maszynie.
                             */
                            else if (((machine1.get(k + 1).getTime_start()) - (machine1.get(k).getTime_start()
                                    + machine1.get(k).getDuration()))
                                    < (tasks[choose_task_from_array].getDuration())){

                                System.out.println("część zadania nie zmieści się pomiędzy dwa zadania na maszynie.");

                                number_on_list++;
                            } else{
                                System.out.println("error");
                            }
                            System.out.println(k);
                        }
                    } else if (tasks[choose_task_from_array].getTask_name().equals("part2")) {
                        /**
                         * Wyszukanie czy część pierwsza została zrealizowana
                         */
                        Task tmp = checkPartFirst(machine1,machine2,tasks[choose_task_from_array].getNumber_task());
                        if (tmp != null) {
                            for (int k = 0; k < machine1.size(); k++) {
                                /**
                                 * Jeśli czas zakończenia zadania na maszynie jest mniejszy od "czasu opóźnienia" części drugiej
                                 */
                                if (machine1.get(k).getTime_start() + machine1.get(k).getDuration() < tmp.getTime_start()
                                        + tmp.getDuration()) {
                                    number_on_list++;
                                }
                                /**
                                 * Jeśli jest ustawiona na ostatnie zadanie.
                                 */
                                else if (k == machine1.size() - 1 || number_on_list >= machine1.size()) {
                                    /**
                                     * Jeśli czas opóźnienia drugiego zadania jest mniejszy od zakończenia się zadania ostatniego
                                     * to ustaw czas opóźnienia jako start.
                                     */
                                    if(tmp.getTime_start()+tmp.getDuration()
                                            > machine1.getLast().getTime_start() + machine1.getLast().getDuration()){
                                        tasks[choose_task_from_array].setTime_start(tmp.getTime_start()+tmp.getDuration());
                                    }else {
                                        tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start()
                                                + machine1.get(k).getDuration());
                                    }
                                    machine1.addLast(tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                }
                                /**
                                 *Jeśli zadanie się zmieści pomiędzy dwoma na maszynie.
                                 */
                                else if (((machine1.get(k + 1).getTime_start()) - (machine1.get(k).getTime_start() + machine1.get(k).getDuration()))
                                        >= (tasks[choose_task_from_array].getDuration())) {
                                    tasks[choose_task_from_array].setTime_start(machine1.get(k).getTime_start() + machine1.get(k).getDuration());
                                    machine1.add(number_on_list+1, tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zadanie sie nie zmieści pomiędzy zadaniami na maszynie.
                                 */
                                else if (((machine1.get(k + 1).getTime_start()) - (machine1.get(k).getTime_start() + machine1.get(k).getDuration()))
                                        < (tasks[choose_task_from_array].getDuration())){
                                    number_on_list++;
                                }else {
                                    number_on_list++;
                                }
                            }
                        }
                    }
                }
                /**
                 * Na maszynie numer 2
                 */
                else if (tasks[choose_task_from_array].getMachine_number() == 2) {
                    /**
                     * Część pierwsza zadania
                     */
                    if (tasks[choose_task_from_array].getTask_name().equals("part1")) {
                        /**
                         * Sprawdzanie miejsca wstawienia w listę
                         */
                        for (int k = 0; k < machine2.size(); k++) {
                            /**
                             * Jeśli zadanie zmieści się przed pierwszym zadaniem umieszczonym na maszynie.
                             */
                            if (k == 0 && machine2.get(0).getTime_start() >= tasks[choose_task_from_array].getDuration()
                                    + tasks[choose_task_from_array].getTime_delay()) {
                                tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                                machine2.add(0, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            /**
                             * Jeśli czas zakończenia kolejnych zadań na maszynie jest większy od czasu opóźnienia.
                             */
                            if ((machine2.get(k).getTime_start() + machine2.get(k).getDuration())
                                    < tasks[choose_task_from_array].getTime_delay()) {
                                number_on_list++;
                            }
                            /**
                             * Jeśli maszyna jest ustawiona na ostatnie zadanie.
                             */
                            else if (k == machine2.size() - 1 || number_on_list >= machine2.size()) {
                                /**
                                 * Jeśli czas opóźnienia zadania jest większy od czasu zakończenia się ostatniego zadania na maszynie
                                 */
                                if ((machine2.getLast().getTime_start()+machine2.getLast().getDuration())
                                        < tasks[choose_task_from_array].getTime_delay()){
                                    tasks[choose_task_from_array].setTime_start(tasks[choose_task_from_array].getTime_delay());
                                } else {
                                    tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                            + machine2.get(k).getDuration());
                                }
                                machine2.addLast(tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            /**
                             * Jeśli zadanie zmieści się pomiedzy dwa zadania umieszczone na maszynie.
                             */
                            else if (((machine2.get(k + 1).getTime_start()) - (machine2.get(k).getTime_start()
                                    + machine2.get(k).getDuration()))
                                    >= (tasks[choose_task_from_array].getDuration())) {
                                tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                        + machine2.get(k).getDuration());
                                machine2.add(number_on_list+1, tasks[choose_task_from_array]);
                                tasks_uses_test[choose_task_from_array] = true;
                                break;
                            }
                            /**
                             * Jeśli zadanie nie zmieści się pomiędzy dwa zadania umieszczone na maszynie.
                             */
                            else if(((machine2.get(k + 1).getTime_start()) - (machine2.get(k).getTime_start()
                                    + machine2.get(k).getDuration()))
                                    < (tasks[choose_task_from_array].getDuration())){
                                number_on_list++;
                            }else {
                                number_on_list++;
                            }
                        }
                    } else if (tasks[choose_task_from_array].getTask_name().equals("part2")) {
                        /**
                         * Wyszukanie czy część pierwsza została zrealizowana
                         */
                        Task tmp = checkPartFirst(machine1,machine2,tasks[choose_task_from_array].getNumber_task());
                        if (tmp != null) {
                            for (int k = 0; k < machine2.size(); k++) {
                                /**
                                 * Jeśli czas opóźnienia części drugiej jest większy od czasu zakończenia się zadania na maszynie.
                                 */
                                if (machine2.get(k).getTime_start() + machine2.get(k).getDuration()
                                        < tmp.getTime_start() + tmp.getDuration()) {
                                    number_on_list++;
                                }
                                /**
                                 * Jeśli maszyna ustawiona jest na ostatnie zadanie.
                                 */
                                else if (k == machine2.size() - 1 || number_on_list >= machine2.size()) {
                                    /**
                                     * Jeśli czas opóźnienia drugiego zadania jest mniejszy od zakończenia się zadania ostatniego
                                     * to ustaw czas opóźnienia jako start.
                                     */
                                    if(tmp.getTime_start()+tmp.getDuration()
                                            > machine2.getLast().getTime_start() + machine2.getLast().getDuration()){
                                        tasks[choose_task_from_array].setTime_start(tmp.getTime_start()+tmp.getDuration());
                                    }else {
                                        tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                                + machine2.get(k).getDuration());
                                    }
                                    machine2.addLast(tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zadanie zmieści się pomiędzy dwoma zadaniami na maszynie.
                                 */
                                else if ((machine2.get(k + 1).getTime_start())
                                        - (machine2.get(k).getTime_start() + machine2.get(k).getDuration())
                                        >= (tasks[choose_task_from_array].getDuration())) {
                                    tasks[choose_task_from_array].setTime_start(machine2.get(k).getTime_start()
                                            + machine2.get(k).getDuration());
                                    machine2.add(number_on_list+1, tasks[choose_task_from_array]);
                                    tasks_uses_test[choose_task_from_array] = true;
                                    break;
                                }
                                /**
                                 * Jeśli zadanie nie zmieści się pomiędzy dwoma zadaniami na maszynie.
                                 */
                                else if ((machine2.get(k + 1).getTime_start())
                                        - (machine2.get(k).getTime_start() + machine2.get(k).getDuration())
                                        < (tasks[choose_task_from_array].getDuration())){
                                    number_on_list++;
                                }else {
                                    number_on_list++;
                                }
                            }
                        }
                    }
                }
            }
        }
        Solution solution = new Solution(machine1,machine2);
        solution.displayMachine1();
        solution.displayMachine2();
        solution.setFunction_target();
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
        for (int k = 0; k < machine1.size(); k++) {
            if (machine1.get(k).getNumber_task() == number_task && machine1.get(k).getTask_name().equals("part1")) {
                Task tmp = machine1.get(k);
                return tmp;
            }
        }
        for (int k = 0; k < machine2.size(); k++) {
            if (machine2.get(k).getNumber_task() == number_task && machine2.get(k).getTask_name().equals("part1")) {
                Task tmp = machine2.get(k);
                return tmp;
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
