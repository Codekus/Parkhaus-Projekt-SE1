package parkhaus;

public interface CarParkIF {

    boolean entry(Car car);
    boolean leave(int pos);
    String getStatus();
}
