package command;

import java.io.Serializable;

public class EnterNormalCar implements Command, Serializable {

    private CarParkCommands carParkCommands;

    @Override
    public void execute()  {
        carParkCommands.enterNormal();
    }

    public EnterNormalCar(CarParkCommands carParkCommands){
        this.carParkCommands = carParkCommands;
    }
}
