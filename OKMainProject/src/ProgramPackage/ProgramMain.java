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
        TestsClass testsClass = new TestsClass();

        int size = 0, inscance_number = 0;
        int count_object = 0, count_maintanance = 0;
        String path_to_instance = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj rozmiar instancji do rozwiązania.");
        size = scanner.nextInt();
        System.out.println("Podaj numer instancji do rozwiązania.");
        inscance_number = scanner.nextInt();

        FeromonMatrix feromonMatrix_machine1 = new FeromonMatrix(size);
        FeromonMatrix feromonMatrix_machine2 = new FeromonMatrix(size);

        count_object = size;
        count_maintanance = 1 + size/8;


//*******************WCZYTANIE INSTANCJI Z PLIKU************************
        //mierzenie czasu start
        long start = System.currentTimeMillis();
//        for (int l = 5; l<=50 ; l+=5) {
//            size = l;
//            for (int j =1; j<=100; j++) {
//                inscance_number = j;
//                System.out.println("size " + size+ " numer instancji "+ inscance_number);
                //tworzenie ścieżki do plików
                path_to_instance = createPathToInstance(size, inscance_number);
//                count_object =size;
                //wczytanie tablicy zadań i przerw z pliku
                Task[] tasks = readInstanceFromFile(path_to_instance, count_object);
                Maintanance[] maintanances = readMaintananceFromFile(path_to_instance, count_object);

//*******************TESTY RANDOM GENERATORA******************************
                /**
                 * Tworzenie instancji wejściowych i wypełnieniej jej losowymi rozwiązaniami
                 */
                Solution solution = null;

                    //kopiowanie głębokie obiektów z tablicy zadań
                    Task[] tasks_clone = cloneTaskArray(tasks);
                    //kopiowanie głębokie obiektów maintanance
                    Maintanance[] maintanances_clone = cloneMaintananceArray(maintanances);

                    solution = generatorV2(tasks_clone, maintanances_clone);

                    solution.setFunction_target();
        //wyświetlenie originału
//        solution.displayMachine1();
//        solution.displayMachine2();
//        System.out.println("Czas funkcji celu : " + solution.getFunction_target());

//***********************TESTY MUTANTÓW*****************************
        //stworzenie i wyświetlenie klona
        Solution clone_solution = solution.cloneSolution();
//        clone_solution.displayMachine1();
//        clone_solution.displayMachine2();
//        System.out.println("Czas funkcji celu : " + clone_solution.getFunction_target());

        //wywołanie funkcji mutacji
        long startm = System.currentTimeMillis();
        for (int i = 0; i<100; i++) {
            Task[] test_mutant_task = cloneTaskArray(tasks);
            Maintanance[] test_mutant_maintanance = cloneMaintananceArray(maintanances);
            Solution mutant = createMutantSolution(clone_solution, size, test_mutant_task, test_mutant_maintanance);
//            System.out.println("Mutant "  +(i+1));
//            mutant.displayMachine1();
//            mutant.displayMachine2();
//            System.out.println("Czas funkcji celu : " + mutant.getFunction_target());
            if (testsClass.sprawdzNakladanieZadan(mutant)) {
                System.out.println("Błąd");
            }
//            System.out.println("Mutant "  + (1) );
//            mutant.displayMachine1();
//            mutant.displayMachine2();
//            System.out.println("Czas funkcji celu : " + mutant.getFunction_target());
//            System.out.println("Originał nakłada się "+testsClass.sprawdzNakladanieZadan(solution));
//            System.out.println("Mutant nakłada się "+testsClass.sprawdzNakladanieZadan(mutant));

//        System.out.println("Originał nakłada się "+testsClass.sprawdzNakladanieZadan(solution));
//        System.out.println("Mutant nakłada się "+testsClass.sprawdzNakladanieZadan(mutant));
//        }
//        long stopm = System.currentTimeMillis();
//        System.out.println("Czas dla 100 mutacji "+ (stopm-startm) + " milis" );

//**********************TESTY MACIERZY FEROMONOWYCH********************************
            feromonMatrix_machine1.addFeromonWay(mutant.getMachine1(), 1);
            feromonMatrix_machine2.addFeromonWay(mutant.getMachine2(), 1);

            int[] step = feromonMatrix_machine1.useFeromonMatrix(2);
            System.out.println("Z " + step[0] + " do " + step[1]);

//            System.out.println("Tablica feromonowa 1");
//            feromonMatrix_machine1.displayFeromonMatrix();
//            System.out.println("Tablica feromonowa 2");
//            feromonMatrix_machine2.displayFeromonMatrix();

//        for (int i = 0 ;i<10; i++){
            feromonMatrix_machine1.evaporationFeromonWay();
            feromonMatrix_machine2.evaporationFeromonWay();
//        }
        }
        long stopm = System.currentTimeMillis();
        System.out.println("Czas dla 100 mutacji "+ (stopm-startm) + " milis" );
        System.out.println("Tablica feromonowa 1");
        feromonMatrix_machine1.displayFeromonMatrix();
        System.out.println("Tablica feromonowa 2");
        feromonMatrix_machine2.displayFeromonMatrix();

//*********************TESTY FUNKCJI WYRÓWNUJĄCEJ RÓŻNICE W RZĘDZIE MACIERZY*************
        double[] array_in = {0,10,15,30,50,1000};
        double[] array_out = feromonMatrix_machine1.alignValueInMatrix(array_in);

        System.out.println("Tablica wejściowa ");
        for (double x: array_in){
            System.out.format("%.2f ", x);
        }
        System.out.println("\nTablica wyjściowa ");
        for (double x: array_out){
            System.out.format("%.2f ", x);
        }
        System.out.println();

//********************TESTY GENERATORA Z MACIERZĄ FEROMONOWĄ ****************************
        Task[] test_mutant_task = cloneTaskArray(tasks);
        Maintanance[] test_mutant_maintanance = cloneMaintananceArray(maintanances);
        Solution feromonmatrixgeneratortest = generatorWithFeromonMatrix(test_mutant_task,test_mutant_maintanance,feromonMatrix_machine1,feromonMatrix_machine2,300000, 150000);
        feromonmatrixgeneratortest.displayMachine1();
        feromonmatrixgeneratortest.displayMachine2();

        if (testsClass.sprawdzNakladanieZadan(feromonmatrixgeneratortest)) {
            System.out.println("Błąd");
        }

    }



    /**
     * Metoda szuka komplementarnego zadania na przeciwnej maszynie
     * @param tablica - tablica kolejności przeciwnej maszyny
     * @param number_task - numer zadania
     * @param size - rozmiar instancji
     * @return - zwraca indeks w tablicy na którym znajduje się komplementarne zadanie
     */
    private static int searchComplementaryTaskOnAgainstMachine(int[] tablica, int number_task, int size){
        /**
         * Gdy szukamy części pierwszej zadania
         */
        if (number_task>size){
            for (int i = 0; i<tablica.length; i++){
                if (tablica[i] == (number_task-size)) return i;
            }
        }
        /**
         * Gdy szukamy części drugiej zadania.
         */
        else {
            for (int i = 0; i<tablica.length; i++){
                if (tablica[i] == (number_task+size)) return i;
            }
        }
        return -1;
    }

    private static Solution createMutantSolution(Solution presolution, int count_task, Task[] tasks, Maintanance[] maintanances){
        // oznaczenia komórek tworzonej tabeli
        // <1;count_task> - części pierwsze zadań
        // <count_task+1;2*count_task> - części drugie zadań
        // <-infinity; -1> - maintanance
        int[] array_of_sequence_machine1 = new int[presolution.getMachine1().size()];
        int[] array_of_sequence_machine2 = new int[presolution.getMachine2().size()];

        /**
         * Przygotowanie tablicy kolejności
         */
        for (int i = 0; i<presolution.getMachine1().size(); i++){
            if (presolution.getMachine1().get(i).getTask_name().equals("part1")){
                array_of_sequence_machine1[i] = presolution.getMachine1().get(i).getNumber_task();
            }else if (presolution.getMachine1().get(i).getTask_name().equals("part2")){
                array_of_sequence_machine1[i] = presolution.getMachine1().get(i).getNumber_task() + count_task;
            }else if (presolution.getMachine1().get(i).getTask_name().equals("maintanance")){
                array_of_sequence_machine1[i] = -presolution.getMachine1().get(i).getNumber_task();
            }
        }
        for (int i = 0; i<presolution.getMachine2().size(); i++){
            if (presolution.getMachine2().get(i).getTask_name().equals("part1")){
                array_of_sequence_machine2[i] = presolution.getMachine2().get(i).getNumber_task();
            }else if (presolution.getMachine2().get(i).getTask_name().equals("part2")){
                array_of_sequence_machine2[i] = presolution.getMachine2().get(i).getNumber_task() + count_task;
            }else if (presolution.getMachine2().get(i).getTask_name().equals("maintanance")){
                array_of_sequence_machine2[i] = -presolution.getMachine2().get(i).getNumber_task();
            }
        }

//        displayArray(array_of_sequence_machine1,array_of_sequence_machine2);

        //zamiana miejsc
        Random random = new Random(System.currentTimeMillis());
        boolean check = true;
        boolean choose_machine = random.nextBoolean();

        /**
         * choose_machine
         * -true dla maszyny nr1
         * -false dla maszyny nr2
         */
        if (choose_machine) {
            while (check) {
                int choose1 = random.nextInt(array_of_sequence_machine1.length);
                int choose2 = random.nextInt(array_of_sequence_machine1.length);

                if (choose1 != choose2) {
                    if (array_of_sequence_machine1[choose1] > 0
                            && array_of_sequence_machine1[choose2] > 0
                            && array_of_sequence_machine1[choose1] <= count_task
                            && array_of_sequence_machine1[choose2] <= count_task) {
//                        System.out.println(choose1 + " " + choose2);
                        int tmp = array_of_sequence_machine1[choose1];
                        array_of_sequence_machine1[choose1] = array_of_sequence_machine1[choose2];
                        array_of_sequence_machine1[choose2] = tmp;
                        check = false;

                        int ch2 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine2, array_of_sequence_machine1[choose1], count_task);
                        int ch3 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine2, array_of_sequence_machine1[choose2], count_task);

                        tmp = array_of_sequence_machine2[ch2];
                        array_of_sequence_machine2[ch2] = array_of_sequence_machine2[ch3];
                        array_of_sequence_machine2[ch3] = tmp;

//                        System.out.println("maszyna 1 zamiana części pierwszej");
//                        displayArray(array_of_sequence_machine1, array_of_sequence_machine2);
                    } else if (array_of_sequence_machine1[choose1] > count_task
                            && array_of_sequence_machine1[choose2] > count_task) {
//                        System.out.println(choose1 + " " + choose2);
                        int tmp = array_of_sequence_machine1[choose1];
                        array_of_sequence_machine1[choose1] = array_of_sequence_machine1[choose2];
                        array_of_sequence_machine1[choose2] = tmp;
                        check = false;

                        int ch2 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine2, array_of_sequence_machine1[choose1], count_task);
                        int ch3 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine2, array_of_sequence_machine1[choose2], count_task);

                        tmp = array_of_sequence_machine2[ch2];
                        array_of_sequence_machine2[ch2] = array_of_sequence_machine2[ch3];
                        array_of_sequence_machine2[ch3] = tmp;

//                        System.out.println("maszyna 1 zamiana części drugiej");
//                        displayArray(array_of_sequence_machine1, array_of_sequence_machine2);
                    }
                }
            }
        }else{
            while (check) {
                int choose1 = random.nextInt(array_of_sequence_machine2.length);
                int choose2 = random.nextInt(array_of_sequence_machine2.length);

                if (choose1 != choose2) {
                    if (array_of_sequence_machine2[choose1] > 0
                            && array_of_sequence_machine2[choose2] > 0
                            && array_of_sequence_machine2[choose1] <= count_task
                            && array_of_sequence_machine2[choose2] <= count_task) {
//                        System.out.println(choose1 + " " + choose2);
                        int tmp = array_of_sequence_machine2[choose1];
                        array_of_sequence_machine2[choose1] = array_of_sequence_machine2[choose2];
                        array_of_sequence_machine2[choose2] = tmp;
                        check = false;

                        int ch2 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine1, array_of_sequence_machine2[choose1], count_task);
                        int ch3 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine1, array_of_sequence_machine2[choose2], count_task);

                        tmp = array_of_sequence_machine1[ch2];
                        array_of_sequence_machine1[ch2] = array_of_sequence_machine1[ch3];
                        array_of_sequence_machine1[ch3] = tmp;

//                        System.out.println("maszyna 2 zamiana części pierwszej");
//                        displayArray(array_of_sequence_machine1, array_of_sequence_machine2);
                    } else if (array_of_sequence_machine2[choose1] > count_task
                            && array_of_sequence_machine2[choose2] > count_task) {
//                        System.out.println(choose1 + " " + choose2);
                        int tmp = array_of_sequence_machine2[choose1];
                        array_of_sequence_machine2[choose1] = array_of_sequence_machine2[choose2];
                        array_of_sequence_machine2[choose2] = tmp;
                        check = false;

                        int ch2 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine1, array_of_sequence_machine2[choose1], count_task);
                        int ch3 = searchComplementaryTaskOnAgainstMachine(array_of_sequence_machine1, array_of_sequence_machine2[choose2], count_task);

                        tmp = array_of_sequence_machine1[ch2];
                        array_of_sequence_machine1[ch2] = array_of_sequence_machine1[ch3];
                        array_of_sequence_machine1[ch3] = tmp;

//                        System.out.println("maszyna 2 zamiana części drugiej");
//                        displayArray(array_of_sequence_machine1, array_of_sequence_machine2);
                    }
                }
            }
        }

        //tworzenie nowych list na podstawie kolejności
        LinkedList<Task> machinenr1 = new LinkedList<>();
        LinkedList<Task> machinenr2 = new LinkedList<>();
        int position_on_machine1 = 0;
        int position_on_machine2 = 0;
        boolean number_of_machine = true;
        //true = machine1
        //false = machine2
        //dopóki nie wykorzystamy wszystkich zadań

        /**
         * Wyjaśnienie oznaczeń :
         * 1)w tablicy kolejności mam numery zadań, dziwnym zbiegiem okoliczności numery te pomniejszone o 1
         * odpowiadają zadaniu z tablicy przekazywanej Task[], dlatego gdy się odwołuję do nich mam
         * task[array...[positionx]-1]
         * 2)Teraz odwoływanie się do listy, position_on_machinex odpowiada aktualnie przeglądanej komórce w tablicy,
         * zatem na liście już w części stworzonej jest positionx-1 obiektów,
         * przez co muszę w ten sposób się do nich odwoływać
         */

        /**
         * Rób dopóki nie skończysz obu tablic kolejności
         */
        while(position_on_machine1 < array_of_sequence_machine1.length
                || position_on_machine2 < array_of_sequence_machine2.length ){


            if (number_of_machine && position_on_machine1 < array_of_sequence_machine1.length) {
                /**
                 * jeśli zadanie jest częścią pierwszą to można bez przeszkód włożyć na listę
                 */
                if (array_of_sequence_machine1[position_on_machine1] > 0
                        && array_of_sequence_machine1[position_on_machine1] <= count_task) {
                    /**
                     * Jeśli maszyna jest jeszcze pusta
                     */
                    if (position_on_machine1 == 0) {
                        tasks[array_of_sequence_machine1[position_on_machine1] - 1]
                                .setTime_start(tasks[array_of_sequence_machine1[position_on_machine1] - 1].getTime_delay());
                    }
                    /**
                     * Jeśli nie to rozpatrz czy należy zaczynać od końca poprzedniego zadania czy czasu opóźnienia
                     */
                    else {
                        if (machinenr1.get(position_on_machine1 - 1).getTime_start() + machinenr1.get(position_on_machine1 - 1).getDuration()
                                >= tasks[array_of_sequence_machine1[position_on_machine1] - 1].getTime_delay()) {
                            tasks[array_of_sequence_machine1[position_on_machine1] - 1]
                                    .setTime_start(machinenr1.get(position_on_machine1 - 1).getTime_start()
                                            + machinenr1.get(position_on_machine1 - 1).getDuration());
                        } else {
                            tasks[array_of_sequence_machine1[position_on_machine1] - 1]
                                    .setTime_start(tasks[array_of_sequence_machine1[position_on_machine1] - 1].getTime_delay());
                        }
                    }
                    machinenr1.addLast(tasks[array_of_sequence_machine1[position_on_machine1] - 1]);
                    position_on_machine1++;
                }
                /**
                 * Jeśli zadanie jest maintanacem, można bez przeszkód włożyć na listę.?
                 */
                else if (array_of_sequence_machine1[position_on_machine1] < 0) {
                    maintanances[-array_of_sequence_machine1[position_on_machine1] - 1]
                            .setTime_start(maintanances[-array_of_sequence_machine1[position_on_machine1] - 1].getTime_delay());
                    /**
                     * Sprawdzenie czy maintanace nie nachodzi na zadanie poprzedzające
                     * jeśli nachodzi to wsadz go przed zadanie poprzedzające,
                     * a czas startu zadania poprzedzającego ustaw na czas zakończenia maintanancu
                     */
                    if (position_on_machine1 == 0){
                        machinenr1.addLast(maintanances[-array_of_sequence_machine1[position_on_machine1] - 1]);
                    }
                    else if( (machinenr1.get(position_on_machine1-1).getTime_start()+machinenr1.get(position_on_machine1-1).getDuration())
                            > maintanances[-array_of_sequence_machine1[position_on_machine1] - 1].getTime_delay()){
                        int m = 1;
                        while (m<=position_on_machine1 && (machinenr1.get(position_on_machine1 - m).getTime_start() + machinenr1.get(position_on_machine1 - m).getDuration())
                                > maintanances[-array_of_sequence_machine1[position_on_machine1] - 1].getTime_delay()) {
                            m++;
                        }
                        m--;
                        machinenr1.add(position_on_machine1-m, maintanances[-array_of_sequence_machine1[position_on_machine1] - 1]);
                        m--;
                        while(m>=0) {
                            machinenr1.get(position_on_machine1 - m).setTime_start(machinenr1.get(position_on_machine1 -m -1).getTime_start()+
                                    machinenr1.get(position_on_machine1 - m -1).getDuration());
                            m--;
                        }
                    }else {
                        machinenr1.addLast(maintanances[-array_of_sequence_machine1[position_on_machine1] - 1]);
                    }
                    position_on_machine1++;
                }
                /**
                 * Jeśli zadanie jest częścią drugą, należy znaleźć na przeciwnej maszynie część pierwszą
                 * jeśli się znajdzie część drugą można wstawić, jeśli nie to należy zamienić maszyny
                 */
                else if (array_of_sequence_machine1[position_on_machine1] > count_task) {
                    Task tmp = checkPartFirst(machinenr1, machinenr2, tasks[array_of_sequence_machine1[position_on_machine1] - 1].getNumber_task());
                    if (tmp != null) {
                        int time_delay_part2 = tmp.getTime_start() + tmp.getDuration();
                        /**
                         * Jeśli zadanie jest pierwsze na liście
                         */
                        if (position_on_machine1 == 0) {
                            tasks[array_of_sequence_machine1[position_on_machine1]-1].setTime_start(time_delay_part2);
                        }
                        /**
                         * Sprawdzenie na kiedy ustawić time_start, czas zakończenia poprzedniego zadania czy czas opóźnienia
                         */
                        else if (machinenr1.get(position_on_machine1 - 1).getTime_start() + machinenr1.get(position_on_machine1 - 1).getDuration()
                                >= time_delay_part2) {
                            tasks[array_of_sequence_machine1[position_on_machine1] - 1]
                                    .setTime_start(machinenr1.get(position_on_machine1 - 1)
                                            .getTime_start() + machinenr1.get(position_on_machine1 - 1).getDuration());
                        } else {
                            tasks[array_of_sequence_machine1[position_on_machine1] - 1].setTime_start(time_delay_part2);
                        }
                        machinenr1.addLast(tasks[array_of_sequence_machine1[position_on_machine1] - 1]);
                        position_on_machine1++;
                    }
                    /**
                     * Zmień maszynę
                     */
                    else {
                        number_of_machine = !number_of_machine;
                    }
                }
            }
            /**
             * maszyna 2
             */
            else if (!number_of_machine && position_on_machine2 < array_of_sequence_machine2.length){
                //-*********************************************
                /**
                 * jeśli zadanie jest częścią pierwszą to można bez przeszkód włożyć na listę
                 */
                if (array_of_sequence_machine2[position_on_machine2] > 0
                        && array_of_sequence_machine2[position_on_machine2] <= count_task) {
                    /**
                     * Jeśli zadanie jest pierwsze na liście
                     */
                    if (position_on_machine2 == 0){
                        tasks[array_of_sequence_machine2[position_on_machine2]-1]
                                .setTime_start(tasks[array_of_sequence_machine2[position_on_machine2]-1].getTime_delay());
                    }else {
                        /**
                         * Ustalanie czasu rozpoczęcia, end ostatniego czy time_delay
                         */
                        if (machinenr2.get(position_on_machine2-1).getTime_start() + machinenr2.get(position_on_machine2-1).getDuration()
                                >= tasks[array_of_sequence_machine2[position_on_machine2]-1].getTime_delay()) {
                            tasks[array_of_sequence_machine2[position_on_machine2]-1]
                                    .setTime_start(machinenr2.get(position_on_machine2 - 1).getTime_start()
                                            + machinenr2.get(position_on_machine2-1).getDuration());
                        }else {
                            tasks[array_of_sequence_machine2[position_on_machine2]-1]
                                    .setTime_start(tasks[array_of_sequence_machine2[position_on_machine2]-1].getTime_delay());
                        }
                    }
                    machinenr2.addLast(tasks[array_of_sequence_machine2[position_on_machine2]-1]);
                    position_on_machine2++;
                }
                /**
                 * Jeśli zadanie jest maintanacem, można bez przeszkód włożyć na listę.
                 */
                else if (array_of_sequence_machine2[position_on_machine2] < 0){
                    maintanances[-array_of_sequence_machine2[position_on_machine2]-1]
                            .setTime_start(maintanances[-array_of_sequence_machine2[position_on_machine2]-1].getTime_delay());
                    /**
                     * Sprawdzenie czy maintanace nie nachodzi na zadanie poprzedzające
                     * jeśli nachodzi to wsadz go przed zadanie poprzedzające,
                     * a czas startu zadania poprzedzającego ustaw na czas zakończenia maintanancu
                     */
                    if (position_on_machine2 == 0){
                        machinenr2.addLast(maintanances[-array_of_sequence_machine2[position_on_machine2] - 1]);
                    }
                    else if( (machinenr2.get(position_on_machine2-1).getTime_start()+machinenr2.get(position_on_machine2-1).getDuration())
                            > maintanances[-array_of_sequence_machine2[position_on_machine2] - 1].getTime_delay()){
                        int m = 1;

                        while (m<=position_on_machine2 && (machinenr2.get(position_on_machine2 - m).getTime_start() + machinenr2.get(position_on_machine2 - m).getDuration())
                                > maintanances[-array_of_sequence_machine2[position_on_machine2] - 1].getTime_delay()) {
                            m++;
                        }

                        m--;
                        machinenr2.add(position_on_machine2-m, maintanances[-array_of_sequence_machine2[position_on_machine2] - 1]);
                        m--;
                        while(m>=0) {
                            machinenr2.get(position_on_machine2 - m).setTime_start(machinenr2.get(position_on_machine2 -m -1).getTime_start()+
                                    machinenr2.get(position_on_machine2 - m -1).getDuration());
                            m--;
                        }
                    }else {
                        machinenr2.addLast(maintanances[-array_of_sequence_machine2[position_on_machine2] - 1]);
                    }
                    position_on_machine2++;
                }
                /**
                 * Jeśli zadanie jest częścią drugą, trzeba poszukać komplementarnego
                 */
                else if (array_of_sequence_machine2[position_on_machine2] > count_task){
                    Task tmp = checkPartFirst(machinenr1, machinenr2, tasks[array_of_sequence_machine2[position_on_machine2]-1].getNumber_task());
                    if (tmp != null){
                        int time_delay_part2 = tmp.getTime_start()+tmp.getDuration();
                        /**
                         * Jeśli maszyna jest jeszcze pusta
                         */
                        if (position_on_machine2 == 0) {
                            tasks[array_of_sequence_machine2[position_on_machine2]-1].setTime_start(time_delay_part2);
                        }
                        /**
                         * Ustawienie czasu rozpoczęcia, end ostatniego czy time_delay
                         */
                        else if (machinenr2.get(position_on_machine2-1).getTime_start()+machinenr2.get(position_on_machine2-1).getDuration()
                                >= time_delay_part2){
                            tasks[array_of_sequence_machine2[position_on_machine2]-1]
                                    .setTime_start(machinenr2.get(position_on_machine2-1)
                                            .getTime_start()+machinenr2.get(position_on_machine2-1).getDuration());
                        }else{
                            tasks[array_of_sequence_machine2[position_on_machine2]-1].setTime_start(time_delay_part2);
                        }
                        machinenr2.addLast(tasks[array_of_sequence_machine2[position_on_machine2]-1]);
                        position_on_machine2++;
                    }
                    /**
                     * nie znaleziono to zmiana maszyny
                     */
                    else {
                        number_of_machine = !number_of_machine;
                    }
                }
            }
            /**
             * Jeśli nic nie pasowało to zmień maszynę
             */
            else {
                number_of_machine = !number_of_machine;
            }
        }

        Solution solution = new Solution(machinenr1,machinenr2);
        solution.setFunction_target();

        return solution;
    }

    /**
     * Test tablic
     * @param ints1
     * @param ints2
     */
    private static void displayArray(int[] ints1, int[] ints2){
        for (int x: ints1){
            System.out.print(x+" ");
        }
        System.out.println();
        for (int x: ints2){
            System.out.print(x+" ");
        }
        System.out.println();
    }



    /**
     * Metoda wykonuje kopię głęboką tablicy przerw podanej na wejściu.
     * @param maintanances - tablica przerw
     * @return zwraca klona tablicy przerw
     */
    private static Maintanance[] cloneMaintananceArray(Maintanance[] maintanances){
        Maintanance[] maintanances_clone = new Maintanance[maintanances.length];
        for (int k = 0; k<maintanances.length; k++){
            Maintanance nowa_przerwa = maintanances[k].cloneMaintanance();
            maintanances_clone[k] = nowa_przerwa;
        }
        return maintanances_clone;
    }

    /**
     * Metoda wykonuje kopię głęboką tablicy zadań podanej na wejściu.
     * @param tasks - tablica zadań
     * @return zwraca klona tablicy zadań
     */
    private static Task[] cloneTaskArray(Task[] tasks){
        Task[] tasks_clone = new Task[tasks.length];
        for (int k = 0; k<tasks.length; k++){
            if (tasks[k].getTask_name().equals("part1")){
                PartFirst nowe_zadanie = tasks[k].cloneFirst();
                tasks_clone[k] = nowe_zadanie;
            }else if (tasks[k].getTask_name().equals("part2")){
                PartSecond nowe_zadanie = tasks[k].cloneSecond();
                tasks_clone[k] = nowe_zadanie;
            }
        }
        return tasks_clone;
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
     * Metoda wyświetla na konsoli wszystkie dane obiektów umieszczonych na maszynach 1 i 2.
     * @param machine1 - maszyna 1
     * @param machine2 - maszyna 2
     */
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

    /**
     * Generator randomowych instancji wejściowych, w pełni działający. Tworzy rozwiązania na zasadzie wprowadzenia
     * najpierw do niego wszystkich rozwiązań, a następnie losuje zadanie, i umieszcza je na pierwszym możliwym
     * miejscu w tablicy rozwiązań.
     * @param tasks - tablica zadań
     * @param maintanances - tablica przerw
     * @return zwraca obiekt Solution będący obiektem zawierającym w sobie obie maszyny rozwiązania
     */
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

//        displayTest(machine1,machine2);


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
     * Generator uwzględniający macierz feromonową. Buduję funkcję "prawdopodobieństwa", a następnie losuje wartość 0-100
     * w zależności od wartości używa macierzy lub nie. Jeśli to robi to szuka następnego wierzchołka, jeśli takowy
     * został już użyty to przechodzi do wstawienia losowego zadania z tablicy.
     * @param tasks tablica zadań
     * @param maintanances tablica przerw
     * @param feromonMatrix1 macierz feromonowa dla maszyny nr 1
     * @param feromonMatrix2 macierz feromonowa dla maszyny nr 2
     * @param time_param czas do sparametryzowania
     * @param actual_time aktualny czas; x w funkcji prawdopodobieństwa
     * @return zwraca nowe rozwiązanie
     */
    private static Solution generatorWithFeromonMatrix(Task[] tasks, Maintanance[] maintanances
            , FeromonMatrix feromonMatrix1, FeromonMatrix feromonMatrix2, long time_param, long actual_time){
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
        int instance_size = tasks.length/2;
        boolean[] tasks_uses_test = new boolean[tasks.length];
        for (int i = 0 ; i< tasks_uses_test.length; i++) tasks_uses_test[i] = false;

        double a = (-100)/time_param;
        double probability = a * actual_time + 100;
        int previous_task = -1;
        boolean use_random = false;


        while (checkAllTasksWasUses(tasks_uses_test)) {
            int check_option = random.nextInt(101);
            use_random = false;
            /**
             * Jeśli jest większe, znaczy znajduje się nad osią wykresu funkcji to użyj macierzy feromonowej
             * jeśli nie to używaj randoma
             */
            if (check_option > probability && previous_task != -1) {

                int step[] = {-1, -1};
                int next_task = -1;
                //co ja tu robię ?
                //wyciągam kolejną wartość z macierzy feromonowej i przypisuję jemu numer zadania z tablicy zadań
                if (tasks[previous_task].getMachine_number() == 1) {
                    step = feromonMatrix1.useFeromonMatrix(previous_task);
                    if (step[1] != -1) {
                        for (int i = 0; i < tasks.length; i++) {
                            if (tasks[i].getNumber_task() == step[1] && tasks[previous_task].getMachine_number() == 1)
                                next_task = i;
                        }
                    }
                } else if (tasks[previous_task].getMachine_number() == 2) {
                    step = feromonMatrix2.useFeromonMatrix(previous_task);
                    if (step[1] != -1) {
                        for (int i = 0; i < tasks.length; i++) {
                            if (tasks[i].getNumber_task() == step[1] && tasks[previous_task].getMachine_number() == 2)
                                next_task = i;
                        }
                    }
                }

                // okej, co mam ?  Mam numer kolejnego zadania, jego pozycję w tablicy, więć mogę sprawdzić czy zadanie już wystąpiło
                if (next_task != -1 && !tasks_uses_test[next_task]) {
                    if (tasks[next_task].getMachine_number() == 1) {
                        int position_last_task = searchPositionLastTask(machine1);
                        if (tasks[next_task].getTask_name().equals("part1")) {
                            if (position_last_task == machine1.size() - 1) {
                                if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                        >= tasks[next_task].getTime_delay()) {
                                    tasks[next_task].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                } else {
                                    tasks[next_task].setTime_start(tasks[next_task].getTime_delay());
                                }
                                tasks_uses_test[next_task] = true;
                                machine1.addLast(tasks[next_task]);
                                previous_task = next_task;
                            } else {//za zadaniem jest jeszcze jakiś maintanance
                                int i = 1;
                                while (true) {
                                    /**
                                     * Wsadzenie pomiędzy zadania
                                     */
                                    if (position_last_task + i <= machine1.size() - 1) {
                                        if (machine1.get(position_last_task + i - 1).getTime_start() + machine1.get(position_last_task + i - 1).getDuration()
                                                >= tasks[next_task].getTime_delay()
                                                && (machine1.get(position_last_task + i).getTime_start() - (machine1.get(position_last_task+ i - 1).getTime_start()
                                                + machine1.get(position_last_task+ i - 1).getDuration())) >= tasks[next_task].getDuration()) {
                                            tasks[next_task].setTime_start(machine1.get(position_last_task+ i - 1).getTime_start() + machine1.get(position_last_task+ i - 1).getDuration());
                                            machine1.add(position_last_task+i,tasks[next_task]);
                                            tasks_uses_test[next_task] = true;
                                            previous_task = next_task;
                                            break;
                                        } else {
                                            i++;
                                        }
                                    } else {
                                        if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                                >= tasks[next_task].getTime_delay()) {
                                            tasks[next_task].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                        } else {
                                            tasks[next_task].setTime_start(tasks[next_task].getTime_delay());
                                        }
                                        tasks_uses_test[next_task] = true;
                                        machine1.addLast(tasks[next_task]);
                                        previous_task = next_task;
                                        break;
                                    }
                                }
                            }
                        } else if (tasks[next_task].getTask_name().equals("part2")) {
                            Task tmp = checkPartFirst(machine1, machine2, tasks[next_task].getNumber_task());
                            int delay_time_part2 = 0;
                            if (tmp != null) {
                                delay_time_part2 = tmp.getTime_start() + tmp.getDuration();
                                if (position_last_task == machine1.size() - 1) {
                                    if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                            >= delay_time_part2) {
                                        tasks[next_task].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                    } else {
                                        tasks[next_task].setTime_start(delay_time_part2);
                                    }
                                    tasks_uses_test[next_task] = true;
                                    machine1.addLast(tasks[next_task]);
                                    previous_task = next_task;
                                } else {//za zadaniem jest jeszcze jakiś maintanance
                                    int i = 1;
                                    while (true) {
                                        /**
                                         * Wsadzenie pomiędzy zadania
                                         */
                                        if (position_last_task + i <= machine1.size() - 1) {
                                            if (machine1.get(position_last_task + i - 1).getTime_start() + machine1.get(position_last_task + i - 1).getDuration()
                                                    >= delay_time_part2
                                                    && (machine1.get(position_last_task + i).getTime_start() - (machine1.get(position_last_task+ i - 1).getTime_start()
                                                    + machine1.get(position_last_task+ i - 1).getDuration())) >= tasks[next_task].getDuration()) {
                                                tasks[next_task].setTime_start(machine1.get(position_last_task+ i - 1).getTime_start() + machine1.get(position_last_task+ i - 1).getDuration());
                                                machine1.add(position_last_task+i,tasks[next_task]);
                                                tasks_uses_test[next_task] = true;
                                                previous_task = next_task;
                                                break;
                                            } else {
                                                i++;
                                            }
                                        } else {
                                            if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                                    >= delay_time_part2) {
                                                tasks[next_task].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                            } else {
                                                tasks[next_task].setTime_start(delay_time_part2);
                                            }
                                            tasks_uses_test[next_task] = true;
                                            machine1.addLast(tasks[next_task]);
                                            previous_task = next_task;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }else if (tasks[next_task].getMachine_number() == 2) {
                            int position_last_task = searchPositionLastTask(machine2);

                        if (tasks[next_task].getTask_name().equals("part1")) {
                            if (position_last_task == machine2.size() - 1) {
                                if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                        >= tasks[next_task].getTime_delay()) {
                                    tasks[next_task].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                } else {
                                    tasks[next_task].setTime_start(tasks[next_task].getTime_delay());
                                }
                                tasks_uses_test[next_task] = true;
                                machine2.addLast(tasks[next_task]);
                                previous_task = next_task;
                            } else {//za zadaniem jest jeszcze jakiś maintanance
                                int i = 1;
                                while (true) {
                                    /**
                                     * Wsadzenie pomiędzy zadania
                                     */
                                    if (position_last_task + i <= machine2.size() - 1) {
                                        if (machine2.get(position_last_task + i - 1).getTime_start() + machine2.get(position_last_task + i - 1).getDuration()
                                                >= tasks[next_task].getTime_delay()
                                                && (machine2.get(position_last_task + i).getTime_start() - (machine2.get(position_last_task+ i - 1).getTime_start()
                                                + machine2.get(position_last_task+ i - 1).getDuration())) >= tasks[next_task].getDuration()) {
                                            tasks[next_task].setTime_start(machine2.get(position_last_task+ i - 1).getTime_start() + machine2.get(position_last_task+ i - 1).getDuration());
                                            machine2.add(position_last_task+i, tasks[next_task]);
                                            tasks_uses_test[next_task] = true;
                                            previous_task = next_task;
                                            break;
                                        } else {
                                            i++;
                                        }
                                    } else {
                                        if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                                >= tasks[next_task].getTime_delay()) {
                                            tasks[next_task].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                        } else {
                                            tasks[next_task].setTime_start(tasks[next_task].getTime_delay());
                                        }
                                        tasks_uses_test[next_task] = true;
                                        machine2.addLast(tasks[next_task]);
                                        previous_task = next_task;
                                        break;
                                    }
                                }
                            }
                        } else if (tasks[next_task].getTask_name().equals("part2")) {
                            Task tmp = checkPartFirst(machine1, machine2, tasks[next_task].getNumber_task());
                            if (tmp != null) {
                                int delay_time_part2 = tmp.getTime_start() + tmp.getDuration();
                                if (position_last_task == machine2.size() - 1) {
                                    if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                            >= delay_time_part2) {
                                        tasks[next_task].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                    } else {
                                        tasks[next_task].setTime_start(delay_time_part2);
                                    }
                                    tasks_uses_test[next_task] = true;
                                    machine2.addLast(tasks[next_task]);
                                    previous_task = next_task;
                                } else {//za zadaniem jest jeszcze jakiś maintanance
                                    int i = 1;
                                    while (true) {
                                        /**
                                         * Wsadzenie pomiędzy zadania
                                         */
                                        if (position_last_task + i <= machine2.size() - 1) {
                                            if (machine2.get(position_last_task + i - 1).getTime_start() + machine2.get(position_last_task + i - 1).getDuration()
                                                    >= delay_time_part2
                                                    && (machine2.get(position_last_task + i).getTime_start() - (machine2.get(position_last_task+ i - 1).getTime_start()
                                                    + machine2.get(position_last_task+ i - 1).getDuration())) >= tasks[next_task].getDuration()) {
                                                tasks[next_task].setTime_start(machine2.get(position_last_task+ i - 1).getTime_start() + machine2.get(position_last_task+ i - 1).getDuration());
                                                machine2.add(position_last_task+i,tasks[next_task]);
                                                tasks_uses_test[next_task] = true;
                                                previous_task = next_task;
                                                break;
                                            } else {
                                                i++;
                                            }
                                        } else {
                                            if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                                    >= delay_time_part2) {
                                                tasks[next_task].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                            } else {
                                                tasks[next_task].setTime_start(delay_time_part2);
                                            }
                                            tasks_uses_test[next_task] = true;
                                            machine2.addLast(tasks[next_task]);
                                            previous_task = next_task;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //jeśli było już użyte to generuj randoma
                    use_random = true;
                }
            }


            if (check_option <= probability || use_random) {
                int wylosowana = random.nextInt(instance_size * 2);
                /**
                 * Wylosowana część pierwsza zadania
                 */
                if (!tasks_uses_test[wylosowana]) {
                    if (tasks[wylosowana].getMachine_number() == 1) {
                        int position_last_task = searchPositionLastTask(machine1);
                        if (tasks[wylosowana].getTask_name().equals("part1")) {
                            if (machine1.size() == 0) {
                                tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                tasks_uses_test[wylosowana] = true;
                                machine1.addFirst(tasks[wylosowana]);
                            }else if (position_last_task == machine1.size() - 1) {
                                if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                        >= tasks[wylosowana].getTime_delay()) {
                                    tasks[wylosowana].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                } else {
                                    tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                }
                                tasks_uses_test[wylosowana] = true;
                                machine1.addLast(tasks[wylosowana]);
                                previous_task = wylosowana;
                            } else {//za zadaniem jest jeszcze jakiś maintanance
                                int i = 1;
                                while (true) {
                                    /**
                                     * Wsadzenie pomiędzy zadania
                                     */
                                    if (position_last_task + i <= machine1.size() - 1) {
                                        if (machine1.get(position_last_task + i - 1).getTime_start() + machine1.get(position_last_task + i - 1).getDuration()
                                                >= tasks[wylosowana].getTime_delay()
                                                && (machine1.get(position_last_task + i).getTime_start() - (machine1.get(position_last_task+ i - 1).getTime_start()
                                                + machine1.get(position_last_task+ i - 1).getDuration())) >= tasks[wylosowana].getDuration()) {
                                            tasks[wylosowana].setTime_start(machine1.get(position_last_task+ i - 1).getTime_start() + machine1.get(position_last_task+ i - 1).getDuration());
                                            tasks_uses_test[wylosowana] = true;
                                            machine1.add(position_last_task+i, tasks[wylosowana]);
                                            previous_task = wylosowana;
                                            break;
                                        } else {
                                            i++;
                                        }
                                    } else {
                                        if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                                >= tasks[wylosowana].getTime_delay()) {
                                            tasks[wylosowana].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                        } else {
                                            tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                        }
                                        tasks_uses_test[wylosowana] = true;
                                        machine1.addLast(tasks[wylosowana]);
                                        previous_task = wylosowana;
                                        break;
                                    }
                                }
                            }
                        } else if (tasks[wylosowana].getTask_name().equals("part2")) {
                            Task tmp = checkPartFirst(machine1, machine2, tasks[wylosowana].getNumber_task());
                            if (tmp != null) {
                                int delay_time_part2 = tmp.getTime_start() + tmp.getDuration();
                                if (machine1.size() == 0) {
                                    tasks[wylosowana].setTime_start(delay_time_part2);
                                    tasks_uses_test[wylosowana] = true;
                                    machine1.addFirst(tasks[wylosowana]);
                                }else if (position_last_task == machine1.size() - 1) {
                                    if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                            >= delay_time_part2) {
                                        tasks[wylosowana].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                    } else {
                                        tasks[wylosowana].setTime_start(delay_time_part2);
                                    }
                                    tasks_uses_test[wylosowana] = true;
                                    machine1.addLast(tasks[wylosowana]);
                                    previous_task = wylosowana;
                                } else {//za zadaniem jest jeszcze jakiś maintanance
                                    int i = 1;
                                    while (true) {
                                        /**
                                         * Wsadzenie pomiędzy zadania
                                         */
                                        if (position_last_task + i <= machine1.size() - 1) {
                                            if (machine1.get(position_last_task + i - 1).getTime_start() + machine1.get(position_last_task + i - 1).getDuration()
                                                    >= delay_time_part2
                                                    && (machine1.get(position_last_task + i).getTime_start() - (machine1.get(position_last_task+ i - 1).getTime_start()
                                                    + machine1.get(position_last_task+ i - 1).getDuration())) >= tasks[wylosowana].getDuration()) {
                                                tasks[wylosowana].setTime_start(machine1.get(position_last_task+ i - 1).getTime_start() + machine1.get(position_last_task+ i - 1).getDuration());
                                                tasks_uses_test[wylosowana] = true;
                                                machine1.add(position_last_task+i, tasks[wylosowana]);
                                                previous_task = wylosowana;
                                                break;
                                            } else {
                                                i++;
                                            }
                                        } else {
                                            if (machine1.getLast().getTime_start() + machine1.getLast().getDuration()
                                                    >= delay_time_part2) {
                                                tasks[wylosowana].setTime_start(machine1.getLast().getTime_start() + machine1.getLast().getDuration());
                                            } else {
                                                tasks[wylosowana].setTime_start(delay_time_part2);
                                            }
                                            tasks_uses_test[wylosowana] = true;
                                            machine1.addLast(tasks[wylosowana]);
                                            previous_task = wylosowana;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }else if (tasks[wylosowana].getMachine_number() == 2) {
                        int position_last_task = searchPositionLastTask(machine2);

                        if (tasks[wylosowana].getTask_name().equals("part1")) {
                            if (machine2.size() == 0) {
                                tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                tasks_uses_test[wylosowana] = true;
                                machine2.addFirst(tasks[wylosowana]);
                            }else if (position_last_task == machine2.size() - 1) {
                                if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                        >= tasks[wylosowana].getTime_delay()) {
                                    tasks[wylosowana].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                } else {
                                    tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                }
                                tasks_uses_test[wylosowana] = true;
                                machine2.addLast(tasks[wylosowana]);
                                previous_task = wylosowana;
                            } else {//za zadaniem jest jeszcze jakiś maintanance
                                int i = 1;
                                while (true) {
                                    /**
                                     * Wsadzenie pomiędzy zadania
                                     */
                                    if (position_last_task + i <= machine2.size() - 1) {
                                        if (machine2.get(position_last_task + i - 1).getTime_start() + machine2.get(position_last_task + i - 1).getDuration()
                                                >= tasks[wylosowana].getTime_delay()
                                                && (machine2.get(position_last_task + i).getTime_start() - (machine2.get(position_last_task+ i - 1).getTime_start()
                                                + machine2.get(position_last_task+ i - 1).getDuration())) >= tasks[wylosowana].getDuration()) {
                                            tasks[wylosowana].setTime_start(machine2.get(position_last_task+ i - 1).getTime_start() + machine2.get(position_last_task+ i - 1).getDuration());
                                            machine2.add(position_last_task+i,tasks[wylosowana]);
                                            tasks_uses_test[wylosowana] = true;
                                            previous_task = wylosowana;
                                            break;
                                        } else {
                                            i++;
                                        }
                                    } else {
                                        if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                                >= tasks[wylosowana].getTime_delay()) {
                                            tasks[wylosowana].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                        } else {
                                            tasks[wylosowana].setTime_start(tasks[wylosowana].getTime_delay());
                                        }
                                        tasks_uses_test[wylosowana] = true;
                                        machine2.addLast(tasks[wylosowana]);
                                        previous_task = wylosowana;
                                        break;
                                    }
                                }
                            }
                        } else if (tasks[wylosowana].getTask_name().equals("part2")) {
                            Task tmp = checkPartFirst(machine1, machine2, tasks[wylosowana].getNumber_task());
                            if (tmp != null) {
                                int delay_time_part2 = tmp.getTime_start() + tmp.getDuration();
                                if (machine2.size() == 0) {
                                    tasks[wylosowana].setTime_start(delay_time_part2);
                                    tasks_uses_test[wylosowana] = true;
                                    machine2.addFirst(tasks[wylosowana]);
                                }else if (position_last_task == machine2.size() - 1) {
                                    if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                            >= delay_time_part2) {
                                        tasks[wylosowana].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                    } else {
                                        tasks[wylosowana].setTime_start(delay_time_part2);
                                    }
                                    tasks_uses_test[wylosowana] = true;
                                    machine2.addLast(tasks[wylosowana]);
                                    previous_task = wylosowana;
                                } else {//za zadaniem jest jeszcze jakiś maintanance
                                    int i = 1;
                                    while (true) {
                                        /**
                                         * Wsadzenie pomiędzy zadania
                                         */
                                        if (position_last_task + i <= machine2.size() - 1) {
                                            if (machine2.get(position_last_task + i - 1).getTime_start() + machine2.get(position_last_task + i - 1).getDuration()
                                                    >= delay_time_part2
                                                    && (machine2.get(position_last_task + i).getTime_start() - (machine2.get(position_last_task+ i - 1).getTime_start()
                                                    + machine2.get(position_last_task+ i - 1).getDuration())) >= tasks[wylosowana].getDuration()) {
                                                tasks[wylosowana].setTime_start(machine2.get(position_last_task+ i - 1).getTime_start() + machine2.get(position_last_task+ i - 1).getDuration());
                                                tasks_uses_test[wylosowana] = true;
                                                machine2.add(position_last_task+i,tasks[wylosowana]);
                                                previous_task = wylosowana;
                                                break;
                                            } else {
                                                i++;
                                            }
                                        } else {
                                            if (machine2.getLast().getTime_start() + machine2.getLast().getDuration()
                                                    >= delay_time_part2) {
                                                tasks[wylosowana].setTime_start(machine2.getLast().getTime_start() + machine2.getLast().getDuration());
                                            } else {
                                                tasks[wylosowana].setTime_start(delay_time_part2);
                                            }
                                            tasks_uses_test[wylosowana] = true;
                                            machine2.addLast(tasks[wylosowana]);
                                            previous_task = wylosowana;
                                            break;
                                        }
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
     * Metoda szuka pozycji ostatniego zadania na liście maszyny
     * @param machine lista maszyny
     * @return zwraca pozycję na maszynie
     */
    private static int searchPositionLastTask(LinkedList<Task> machine){
        int last_index = 0;
        for (int i =0 ; i<machine.size();i++){
            if (!machine.get(i).getTask_name().equals("maintanance")) last_index = i;
        }
        return last_index;
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

//        System.out.println(path);
        Task[] tasks = new Task[count_objects*2];
        scanner.nextLine();
        scanner.nextLine();
        for (int i = 0; i<count_objects; i++){
            String tmp = scanner.nextLine();
            String[] strings_array = null;
            strings_array = tmp.split(";");

//            System.out.println(strings_array[0] + " " +strings_array[1] + " "
//                    +strings_array[2] + " " +strings_array[3] + " " + strings_array[4]);
//            System.out.println(Integer.parseInt(strings_array[0]) + " " + Integer.parseInt(strings_array[1])+ " "
//                    +Byte.parseByte(strings_array[2]) + " " + Byte.parseByte(strings_array[3]) + " "+ Integer.parseInt(strings_array[4]));

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
