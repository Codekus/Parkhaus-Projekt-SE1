package parkhaus;


import java.security.SecureRandom;
import java.util.NoSuchElementException;

public class ParkSlot implements ParkSlotIF{

    private final int id;
    private boolean freeStatus;
    private final Type type;
    private Car car;

    private String color;

    SecureRandom rand = new SecureRandom();

    public ParkSlot(Type type, int id){
        this.type = type;
        this.id = id;
        freeStatus = true;
    }

    private ParkSlot(Type type, int id, boolean freeStatus, Car car, String color){
        this.type = type;
        this.id = id;
        this.freeStatus = freeStatus;
        this.car = car;
        this.color = color;
    }


    public boolean entry(Car car){
        if(isFree()){
            this.car = car;
            block();
            this.car.setSlot(id);
            if(!car.getType().equals(type)){
                if (type == Type.FAMILY) {
                    car.setToFam();
                } else if (type == Type.NORMAL) {
                    car.setToNormal();
                }
            }
            return true;
        }
        return false;
    }

    public boolean leave(int id){
        if(id == getId() && !isFree()){
            unblock();
            car = null;
            return true;
        } else {
            return false;
        }
    }

    public Car getCar(){
        if(!isFree()){
            return car;
        }
        throw new NoSuchElementException();
    }

    public boolean block(){
        if(isFree()){
            freeStatus = false;
            return true;
        }
        return false;

    }

    public boolean unblock(){
        if(!isFree()){
            freeStatus = true;
            return true;
        }
        return false;
    }

    public boolean isFree(){
        return freeStatus;
    }

    public Type getType(){
        return type;
    }

    public int getId(){
        return id;
    }

    public void setColor(){
        this.color = String.format("#%06x", this.rand.nextInt(0xffffff + 1));
    }
    public String getColor(){
        if(color == null){
            setColor();
        }
        return color;
    }
    public JSON toJson(){
        JSON json = new JSON();
        json.addObject("id",getId());
        json.addObject("freeStatus",isFree());
        json.addObject("type",getType().toString());
        json.addObject("color",getColor());
        return json;
    }

    public ParkSlot copy(){
        return new ParkSlot(type,id,freeStatus,car,color);
    }

}
