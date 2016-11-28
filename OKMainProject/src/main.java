import java.util.Scanner;

/**
 * Created by Jarek on 2016-11-26.
 */
public class main {
    public static void main(String[] args) {
        System.out.println(Integer.toString(addition(1,2)));
        System.out.println("Hello");
        for(String i:args){
            System.out.println(i);
        }

        Scanner scanner = new Scanner(System.in);
        String name = "";
        do{
            name = scanner.nextLine();
        }while (name.length() != 4);
        System.out.println(name);
    }


    private static int addition(int x, int y){
        return x+y;
    }
}
