package command;

import parkhaus.*;
import statistics.ChangedType;
import statistics.ChangedTypeOverall;

import java.util.List;
import java.util.Stack;

public class CarParkCommands {

    Stack<List<Level>> levelStack = new Stack<>();
    Stack<List<JSON>> lastLeaveCar = new Stack<>();
    Stack<List<JSON>> carParkHistory = new Stack<>();
    Stack<ChangedType> changedType = new Stack<>();
    Stack<ChangedTypeOverall> changedTypeOverall = new Stack<>();
    CarPark carPark = CarPark.getInstance();

    public void enter()  {
        pushProgress();
        if(!carPark.entry(new Car(Type.randomType()))){
            popProgress();
        }
    }

    public void enterNormal()  {
        pushProgress();
        if(!carPark.entry(new Car(Type.NORMAL))){
            popProgress();
        }
    }

    public void enterFamily()  {
        pushProgress();
        if(!carPark.entry(new Car(Type.FAMILY))){
            popProgress();
        }
    }

    public void enterSpecialNeed()  {
        pushProgress();
        if(!carPark.entry(new Car(Type.SPECIAL_NEED))){
            popProgress();
        }
    }

    public void leave(int pos)  {
        pushProgress();
        if(!carPark.leave(pos)){
            popProgress();
        }
    }

    public void undo(){
        popProgress();
    }

    private void pushProgress()  {
        levelStack.push(carPark.getCopyLevels());
        lastLeaveCar.push(carPark.getLeaveCarJSONList());
        carParkHistory.push(carPark.getCarParkHistory());
        changedType.push(carPark.getCopiedChangedType());
        changedTypeOverall.push(carPark.getCopiedChangedTypeOverall());

    }

    private void popProgress(){
        carPark.setLevels(levelStack.pop());
        carPark.setLeaveCarJSON(lastLeaveCar.pop());
        carPark.setCarParkHistory(carParkHistory.pop());
        carPark.setChangedType(changedType.pop());
        carPark.setChangedTypeOverall(changedTypeOverall.pop());
    }
}
