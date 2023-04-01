package parkhaus;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

public class Car implements CarIF, Serializable {

    private final Instant entryPoint;
    private String licensePlate;
    private Type type;
    SecureRandom rand = new SecureRandom();
    private int slotId;
    private String level;
    private Type origin = null;
    private float price;
    private double duration = -1;

    public Car(Type type){
        setLicensePlate();
        entryPoint = Instant.now();
        this.type = type;
    }

    private void setLicensePlate(){
        String[] cityLP = {"K", "SU", "BN"};
        String[] randomLetters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        licensePlate = cityLP[rand.nextInt(2)] + " " + randomLetters[rand.nextInt(25)] + randomLetters[rand.nextInt(25)] +
                " " + rand.nextInt(10000 - 1000 +1);
    }
    public Type getType(){
        return type;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public double getDuration() {
        if(duration > 0){
            return duration;
        }
        double tmp = CarIF.milliToHours((Duration.between(entryPoint, Instant.now()).toMillis()));
        duration = (Math.round(tmp * 100.0) /100.0);
        return duration;
    }
    public void setSlot(int slotId){
        this.slotId = slotId;
    }
    public int getSlot(){
        return slotId;
    }
    public void setLevel(String level){
        this.level = level;
    }
    public String getLevel(){
        return level;
    }
    public void setToNormal(){
        if(origin == null) {
            origin = type;
        }
    }
    public void setToFam(){
        if(origin == null) {
            origin = type;
        }
    }
    public Type getOrigin() {
        return origin;
    }

    public void setPrice(float price){
        this.price = Math.round(price * getDuration()*100f) / 100f;

    }
    public float getPrice() {
        return price;
    }

    public JSON getLeaveData(){
        JSON json = new JSON();
        json.addObject("ID", getLevel() + " " + getSlot());
        json.addObject("type", getType().toString());
        json.addObject("licensePlate", getLicensePlate());
        json.addObject("duration", getDuration());
        json.addObject("fee", getPrice());

        return json;
    }


}
