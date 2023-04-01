package parkhaus;

public interface CarIF {

    Type getType();
    String getLicensePlate();
    double getDuration();
    //Converts Milliseconds to hours and multiplies the value by factor x
    static double milliToHours(double d) {
        int x = 720;
        return ((d / 1000d) * x) / 60d / 60d;
    }

    void setSlot(int slotId);
    int getSlot();
    void setLevel(String level);
    String getLevel();
    void setToNormal();
    void setToFam();
    Type getOrigin();
    void setPrice(float price);
    float getPrice();
    JSON getLeaveData();
}
