package Homework_09_27;

public class Homework09 {
    public static void main(String[] args) {
        Automobile auto = new Automobile();

        auto.upSpeed(250);
        System.out.println("승용차의 속도는 " +
                auto.getSpeed() + "km 입니다.");
    }
}
