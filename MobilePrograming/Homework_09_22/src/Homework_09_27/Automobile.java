package Homework_09_27;

public class Automobile extends Car{
    int SeatNum;

    int getSeatNum() {
        return SeatNum;
    }

    void upSpeed(int value) {
        if (speed + value >= 300) {
            speed = 300;
        } else {
            speed = speed + (int) value;
        }
    }
}
