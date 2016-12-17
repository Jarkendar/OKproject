package ProgramPackage;

import ProgramPackage.TaskPackage.Task;

/**
 * Created by Jarek on 2016-12-17.
 */
public class TestsClass {

    /**
     * Metoda sprawdza czy zadania się nakładają.
     * @param solution - rozwiązanie
     * @return - true=nakładają się , false=nie nakładają się
     */
    public boolean sprawdzNakładanieZadan(Solution solution){
        for (int i = 0 ; i<solution.getMachine1().size() -1; i++){
            if(solution.getMachine1().get(i).getTime_start()+solution.getMachine1().get(i).getDuration()
                    > solution.getMachine1().get(i+1).getTime_start()){
                System.out.println("1 - "+ i);
                return true;
            }
        }
        for (int i = 0 ; i<solution.getMachine2().size() -1; i++){
            if(solution.getMachine2().get(i).getTime_start()+solution.getMachine2().get(i).getDuration()
                    > solution.getMachine2().get(i+1).getTime_start()) {
                System.out.println("2 - " + i);
                return true;
            }
        }
        return false;
    }
}
