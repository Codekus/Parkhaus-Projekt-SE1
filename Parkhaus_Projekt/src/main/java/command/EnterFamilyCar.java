package command;

import java.io.Serializable;

public class EnterFamilyCar implements Command, Serializable {

    private CarParkCommands carParkCommands;

    @Override
    public void execute()  {
        carParkCommands.enterFamily();
    }

    public EnterFamilyCar(CarParkCommands carParkCommands){
        this.carParkCommands = carParkCommands;
    }
}
