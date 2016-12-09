package ProgramPackage;

import ProgramPackage.TaskPackage.Task;

import java.util.LinkedList;

/**
 * Created by Jarek on 2016-12-06.
 */
public class Solution {
    private int function_target;
    //solution_array[0] - maszyna nr 1 , solution_array[1] - maszyna nr 2
    private LinkedList <Task> machine1;
    private LinkedList <Task> machine2;

    public int getFunction_target() {
        return function_target;
    }

    public void setFunction_target(int function_target) {
        this.function_target = function_target;
    }
}
