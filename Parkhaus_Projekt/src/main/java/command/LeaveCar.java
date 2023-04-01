package command;


import java.io.Serializable;

public class LeaveCar implements Command, Serializable {

    private CarParkCommands carParkCommands;
    private int pos;

    public LeaveCar(CarParkCommands carParkCommands, int pos){
        this.carParkCommands = carParkCommands;
        this.pos = pos;
    }

    @Override
    public void execute()  {
        carParkCommands.leave(pos);
    }
}
