package Homework_09_22;

public class Homework05 {
    static int var = 100;
    public static void main(String args[]) {
        int var = 0;
        System.out.println(var);

        int sum = addFunction(10, 20);
        System.out.println(sum);
    }

    static int addFunction(int num1 ,int num2) {
        return num1 + num2 + var;
    }
}
