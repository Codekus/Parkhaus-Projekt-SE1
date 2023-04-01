package parkhaus;

public interface ParkSlotIF {

    boolean entry(Car car);
    boolean leave(int id);
    Car getCar();
    boolean block();
    boolean unblock();
    boolean isFree();
    Type getType();
    int getId();
}
