package parkhaus;


import org.json.JSONArray;
import statistics.*;

import java.util.*;
import java.util.stream.Collectors;

public class CarPark implements CarParkIF{
    private JSON json;

    private final int[][] maxParkSlots;
    private int counter = 0;


    private List<JSON> carParkHistory = new ArrayList<>();
    private List<JSON> lastLeaveCar = new ArrayList<>();


    private final ParkSlotCapacity parkSlotCapacity = new ParkSlotCapacity();

    private ChangedType changedType = new ChangedType();
    private ChangedTypeOverall changedTypeOverall = new ChangedTypeOverall();
    private static CarPark instance = null;
    private static final String COUNT_PARKED_CARS = "countParkedCars";
    private static final String MAX_CARS = "maxCars";
    private static final String SUM_FEES = "sumFees";
    private static final String AVG_PARK_DURATION = "avgParkDuration";


    public static synchronized CarPark getInstance() {
        if (instance == null) {
            instance = new CarPark();
        }
        return instance;
    }

    List<Level> levels = new ArrayList<>();

    private CarPark(){

        loadJson();

        JSONArray allLevels = json.getJSONArray("levels");
        maxParkSlots = new int[allLevels.length()][3];

        for(int i = 0; i < allLevels.length(); i++){
            String name = allLevels.getJSONObject(i).getString("name");
            int normalAmount = allLevels.getJSONObject(i).getInt("normal");
            int familyAmount = allLevels.getJSONObject(i).getInt("family");
            int disableAmount = allLevels.getJSONObject(i).getInt("disabled");
            maxParkSlots[i][0] = normalAmount;
            maxParkSlots[i][1] = familyAmount;
            maxParkSlots[i][2] = disableAmount;
            this.levels.add(new Level(name, normalAmount,familyAmount, disableAmount, json.getInt("fees"), i+1));
        }
    }

    public JSON getStatsJson(){
        JSON tmpJson = new JSON();
        tmpJson.addObject(MAX_CARS,levels.stream().map(Level::getMaxCount).reduce(0, Integer::sum));
        tmpJson.addObject(SUM_FEES,levels.stream().map(Level::getSum).reduce(0.0F, Float::sum));
        tmpJson.addObject(AVG_PARK_DURATION,levels.stream().map(Level::getAvgParkDuration).reduce(0.0, (sum, x) -> (sum + x) / levels.size()));
        tmpJson.addObject(COUNT_PARKED_CARS,levels.stream().map(Level::getOccupied).reduce(0, Integer::sum));
        changedType.addToJson(tmpJson);
        changedTypeOverall.addToJson(tmpJson);
        parkSlotCapacity.addToJson(tmpJson);
        return tmpJson;
    }


    public boolean entry(Car car) {

        List<Map.Entry<Level, Optional<ParkSlot>>> parkSlot;
        int parkSlotID;
        switch (car.getType()) {
            case SPECIAL_NEED:
                parkSlot =  levels.stream().collect(Collectors.toMap(level -> level, level -> level.getParkSlots().stream().filter(ParkSlot::isFree).filter(x -> x.getType() == Type.SPECIAL_NEED).findFirst())).entrySet().stream().filter(y -> (y.getValue().isPresent())).collect(Collectors.toList());

                parkSlotID = getParkSlotValue(parkSlot);
                if(parkSlotID > 0) {
                    break;
                }
                // fallthrough
            case FAMILY:
                parkSlot =  levels.stream().collect(Collectors.toMap(level -> level, level -> level.getParkSlots().stream().filter(ParkSlot::isFree).filter(x -> x.getType() == Type.FAMILY).findFirst())).entrySet().stream().filter(y -> (y.getValue().isPresent())).collect(Collectors.toList());

                parkSlotID = getParkSlotValue(parkSlot);
                if(parkSlotID > 0) {
                    break;
                }
                // fallthrough
            default:
                parkSlot =  levels.stream().collect(Collectors.toMap(level -> level, level -> level.getParkSlots().stream().filter(ParkSlot::isFree).filter(x -> x.getType() == Type.NORMAL).findFirst())).entrySet().stream().filter(y -> (y.getValue().isPresent())).collect(Collectors.toList());

                parkSlotID = getParkSlotValue(parkSlot);
        }

        if(parkSlot.size() >=2){
            parkSlot = parkSlot.stream().sorted(Comparator.comparing(level -> level.getKey().getId())).collect(Collectors.toList());
        }
        boolean tmp = false;
        if(parkSlotID != -1){
            tmp = parkSlot.get(0).getKey().entry(car,parkSlotID);
        }

        if (tmp) {
            counter++;
            changedType.setParams(car.getOrigin());
            changedTypeOverall.setParams(car.getOrigin());
            parkSlotCapacity.setParams(parkSlotCapacity.calcValues());
            return true;
        }

        return false;
    }

    private int getParkSlotValue(List<Map.Entry<Level, Optional<ParkSlot>>> parkSlot){
        if(!parkSlot.isEmpty()){
            Optional<ParkSlot> value = parkSlot.get(0).getValue();
            if(value.isPresent()){
                return value.get().getId();
            }
        }
        return -1;
    }

    public boolean leave(int pos) {
        if(levels.stream().map(Level::getOccupied).reduce(0, Integer::sum) == 0){
            return false;
        }
        boolean leaveBool = false;
        int sum = 0;
        for(Level level: levels){
            int temp = level.getFamCap()+ level.getNormCap()+level.getSpnCap();

            if(sum+temp > pos){
                Type tmp = level.getCar(pos-sum).getOrigin();
                leaveBool = level.leave(pos-sum);
                changedType.reduce(tmp);
                lastLeaveCar.add(level.getLastLeaveCar());
                carParkHistory.add(level.getLastLeaveCar());
                break;
            }
            sum+=temp;
        }


        parkSlotCapacity.setParams(parkSlotCapacity.calcValues());

        return leaveBool;
    }

    public String getStatus() {
        JSON status = new JSON();

        JSONArray jsonArray = new JSONArray();
        for (Level level : levels) {
            jsonArray.put(level.toJson());
        }
        status.addObject(SUM_FEES,getStatsJson().get(SUM_FEES));
        status.addObject("changedType",getStatsJson().get("changedType"));
        status.addObject("changedTypeOverall",getStatsJson().get("changedTypeOverall"));
        status.addObject(AVG_PARK_DURATION,getStatsJson().get(AVG_PARK_DURATION));
        status.addObject(COUNT_PARKED_CARS,getStatsJson().get(COUNT_PARKED_CARS));
        status.addObject("levels",jsonArray);
        return status.toString();
    }


    public void loadJson() {
        this.json = JSON.getConfigJson();
    }

    public List<Level> getLevels() {
        return levels;
    }

    public static void setNull(){
        instance = null;
    }

    public int[][] getMaxParkSlots() {
        return maxParkSlots;
    }

    public JSON getJson(){
        return this.json;
    }

    public JSON getLeaveCarJSON(){
        return lastLeaveCar.remove(lastLeaveCar.size()-1);
    }

    public List<JSON> getLeaveCarJSONList(){
        List<JSON> deepCopyList = new ArrayList<>();
        for(JSON l : lastLeaveCar){
            deepCopyList.add(l.copy());
        }
        return deepCopyList;
    }

    public void setLeaveCarJSON(List<JSON> lastLeaveCar){
        this.lastLeaveCar = lastLeaveCar;
    }

    /**
     *
     * @return deepClones the carPark History to put it on Stack
     */
    public List<JSON> getCarParkHistory(){
        List<JSON> deepCopyList = new ArrayList<>();
        for(JSON l : carParkHistory){
            deepCopyList.add(l.copy());
        }
        return deepCopyList;
    }

    public List<JSON> getCarParkHistoryJSON(){
        return carParkHistory;
    }

    public void setCarParkHistory(List<JSON> carParkHistory){
        this.carParkHistory = carParkHistory;
    }

    public Map<Type,Integer> getCounter(){
        int tempFam = levels.stream().collect(Collectors.toMap(Level::getName, level -> level.getParkSlots().stream().filter(parkslot -> !parkslot.isFree()).filter(parkSlot -> parkSlot.getCar().getType() == Type.FAMILY).count())).values().stream().reduce((long) 0, Long::sum).intValue();
        int tempSpn = levels.stream().collect(Collectors.toMap(Level::getName, level -> level.getParkSlots().stream().filter(parkslot -> !parkslot.isFree()).filter(parkSlot -> parkSlot.getCar().getType() == Type.SPECIAL_NEED).count())).values().stream().reduce((long) 0, Long::sum).intValue();
        return Map.of(Type.FAMILY, tempFam,Type.SPECIAL_NEED,tempSpn);
    }

    public int getOverallCounter(){
        return counter;
    }
    public void setLevels(List<Level> levels){
        this.levels = levels;
    }

    public ChangedType getCopiedChangedType(){
        return changedType.copy();
    }

    public ChangedTypeOverall getCopiedChangedTypeOverall(){
        return changedTypeOverall.copy();
    }
    public void setChangedType(ChangedType changedType){
        this.changedType = changedType;
    }
    public void setChangedTypeOverall(ChangedTypeOverall changedTypeOverall){
        this.changedTypeOverall = changedTypeOverall;
    }
    public List<Level> getCopyLevels(){
        return this.levels.stream().map(Level::copy).collect(Collectors.toList());
    }
}
