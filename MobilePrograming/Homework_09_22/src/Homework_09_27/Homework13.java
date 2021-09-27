package Homework_09_27;

interface clickListener {
    public void print();
}

public class Homework13 {
    public static void main(String[] args) {
        clickListener listener =
                (new clickListener() {
                    @Override
                    public void print() {
                        System.out.println("클릭 리스너입니다.");
                    }
                });

        listener.print();
    }
}
