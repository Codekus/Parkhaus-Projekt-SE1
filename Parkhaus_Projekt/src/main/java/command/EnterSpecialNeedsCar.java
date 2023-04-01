package command;

import java.io.Serializable;

public class EnterSpecialNeedsCar implements Command, Serializable {

    private CarParkCommands carParkCommands;

    @Override
    public void execute()  {
        carParkCommands.enterSpecialNeed();
    }

    public EnterSpecialNeedsCar(CarParkCommands carParkCommands){
        this.carParkCommands = carParkCommands;
    }
}
