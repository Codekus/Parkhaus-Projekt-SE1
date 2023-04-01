package command;


import java.io.Serializable;

public class EnterCar implements Command, Serializable {

    private CarParkCommands carParkCommands;

    @Override
    public void execute()  {
        carParkCommands.enter();
    }

    public EnterCar(CarParkCommands carParkCommands){
        this.carParkCommands = carParkCommands;
    }
}
