package command;

import java.io.Serializable;

public class Undo implements Command , Serializable {
    private CarParkCommands carParkCommands;

    @Override
    public void execute() {
        carParkCommands.undo();
    }

    public Undo(CarParkCommands carParkCommands){
        this.carParkCommands = carParkCommands;
    }
}