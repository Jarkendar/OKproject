package ProgramPackage;

import ProgramPackage.TaskPackage.Maintanance;
import ProgramPackage.TaskPackage.PartFirst;
import ProgramPackage.TaskPackage.PartSecond;
import ProgramPackage.TaskPackage.Task;

import java.util.LinkedList;

/**
 * Created by Jarek on 2016-12-06.
 */
public class Solution {
    private int function_target = -1;
    //solution_array[0] - maszyna nr 1 , solution_array[1] - maszyna nr 2
    private LinkedList <Task> machine1;
    private LinkedList <Task> machine2;

    /**
     * Konstruktor klasy Solution
     * @param machine1 - lista zadań na maszynie nr 1, ustawione w kolejności
     * @param machine2 - lista zadań na maszynie nr 2, ustawione w kolejności
     */
    public Solution(LinkedList<Task> machine1, LinkedList<Task> machine2) {
        this.setMachine1(machine1);
        this.setMachine2(machine2);

    }

    /**
     * Metoda klonuje rozwiazanie.
     * @return zwraca klona rozwiązania
     */
    public Solution cloneSolution(){
        LinkedList<Task> clone_machine1 = this.cloneMachineList(this.getMachine1());
        LinkedList<Task> clone_machine2 = this.cloneMachineList(this.getMachine2());
        Solution new_solution = new Solution(clone_machine1,clone_machine2);
        new_solution.setFunction_target();
        return new_solution;
    }

    /**
     * Metoda klonuje listę zadań
     * @param tasks - maszyna z listą zadań
     * @return zwraca klona maszyny
     */
    private LinkedList<Task> cloneMachineList(LinkedList<Task> tasks){
        LinkedList<Task> new_list = new LinkedList<>();
        for (int i = 0; i<tasks.size(); i++){
            if (tasks.get(i).getTask_name().equals("part1")){
                PartFirst partFirst = tasks.get(i).cloneFirst();
                new_list.addLast(partFirst);
            }else if (tasks.get(i).getTask_name().equals("part2")){
                PartSecond partSecond = tasks.get(i).cloneSecond();
                new_list.addLast(partSecond);
            }
            if (tasks.get(i).getTask_name().equals("maintanance")){
                Maintanance maintanance = tasks.get(i).cloneMaintanance();
                new_list.addLast(maintanance);
            }
        }
        return new_list;
    }

    public int getFunction_target() {
        return function_target;
    }

    public void displayMachine1(){
        System.out.println("Maszyna nr 1");
        for (Task x: getMachine1()){
            System.out.println("nazwa operacji : " + x.getTask_name() + " " + x.getNumber_task()
                    + " ; czas pracy : " + x.getTime_start()
                    + " - " + (x.getTime_start()+x.getDuration()));
        }
    }

    public void displayMachine2(){
        System.out.println("Maszyna nr 2");
        for (Task x: getMachine2()){
            System.out.println("nazwa operacji : " + x.getTask_name() + " " + x.getNumber_task()
                    + " ; czas pracy : " + x.getTime_start()
                    + " - " + (x.getTime_start()+x.getDuration()));
        }

    }

    /**
     * Funkcja obliczająca, wstępnie do zadania nr 3, czas wykonania ostatniego zadania.
     */
    public void setFunction_target() {

        int m1_end_time = getMachine1().getLast().getTime_start() + getMachine1().getLast().getDuration();
        int m2_end_time = getMachine2().getLast().getTime_start() + getMachine2().getLast().getDuration();

        if (m1_end_time > m2_end_time) {
            this.function_target = m1_end_time;
        }
        else {
            this.function_target = m2_end_time;
        }
    }

    public LinkedList<Task> getMachine1() {
        return machine1;
    }

    public void setMachine1(LinkedList<Task> machine1) {
        this.machine1 = machine1;
    }

    public LinkedList<Task> getMachine2() {
        return machine2;
    }

    public void setMachine2(LinkedList<Task> machine2) {
        this.machine2 = machine2;
    }
}
