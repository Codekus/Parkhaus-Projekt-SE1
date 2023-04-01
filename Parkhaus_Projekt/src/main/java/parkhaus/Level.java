package parkhaus;


import iterator.ParkSlotGenerator;

import org.json.JSONArray;
import statistics.AvgParkDuration;
import statistics.SumFees;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Level implements LevelIF{

    //Name of this Level
    private final String name;
    private final List<ParkSlot> parkSlots;
    private JSON lastLeaveCar;
    private final int normCap;
    private final int famCap;
    private final int spnCap;
    private final int fees;
    private final int id;
    private AvgParkDuration avgParkDuration = new AvgParkDuration(0.0);

    private SumFees sumFees = new SumFees(0.0f);
    ParkSlotGenerator parkSlotsGenerator = new ParkSlotGenerator();
    Iterator<ParkSlot> parkSlotIterator = parkSlotsGenerator.iterator();


    public Level(String name, int norm, int fam, int spn, int fees, int id){
        this.name = name;
        normCap = norm;
        famCap = fam;
        spnCap = spn;
        this.fees = fees;
        parkSlots = new ArrayList<>();
        this.id = id;
        genParkSlot(norm, fam, spn);
    }

    private Level(String name, int norm, int fam, int spn, int fees, int id,List<ParkSlot> parkSlots, AvgParkDuration avgParkDuration, SumFees sumFees){
        this.name = name;
        normCap = norm;
        famCap = fam;
        spnCap = spn;
        this.fees = fees;
        this.parkSlots = parkSlots;
        this.avgParkDuration = avgParkDuration;
        this.sumFees = sumFees;
        this.id = id;
    }
    //Generate an Array of ParkSlots, this simulates an Level
    private void genParkSlot(int norm, int fam, int spn){
        parkSlotsGenerator.setCounts(norm, fam, spn);
        while(parkSlotIterator.hasNext()){
            parkSlots.add(parkSlotIterator.next());
        }
    }
    public List<ParkSlot> getParkSlots(){
        return parkSlots;
    }

    public boolean entry(Car car, int tmpID) {

        //In the case that no slot is available
        if(tmpID < 0){
            return false;
        }
        car.setLevel(name);
        return parkSlots.get(tmpID-1).entry(car);
    }

    public int getOccupied(){
        return parkSlots.stream().filter(parkSlot -> !parkSlot.isFree()).map(parkSlot -> 1).reduce(0, Integer::sum);
    }

    public int getMaxCount(){
        return normCap+famCap+spnCap;
    }
    public boolean leave(int pos) {
        if(!parkSlots.get(pos).isFree()){
            Car car = parkSlots.get(pos).getCar();
            if(parkSlots.get(pos).leave(pos+1)){
                car.setPrice(fees);
                sumFees.setParams((Float) sumFees.getObject() + (car.getPrice()));
                avgParkDuration.setParams(car.getDuration());

                lastLeaveCar = car.getLeaveData();

                return true;
            }
        }

        return false;
    }

    public Car getCar(int pos) {
        if(pos < 0 || pos >= parkSlots.size()){
            throw new IllegalArgumentException();
        }

        return parkSlots.get(pos).getCar();
    }

    public JSON getLastLeaveCar() {
        return lastLeaveCar;
    }

    public String getName() {
        return name;
    }
    public int getFamCap() {
        return famCap;
    }

    public int getNormCap() {
        return normCap;
    }

    public int getSpnCap() {
        return spnCap;
    }
//addToJson for stats
    public JSON toJson(){
        JSON json = new JSON();
        json.addObject("name",getName());
        json.addObject("normCap",getNormCap());
        json.addObject("famCap",getFamCap());
        json.addObject("spnCap",getSpnCap());
        JSONArray jsonarray = new JSONArray();

        for(ParkSlot parkSlot: parkSlots){
            jsonarray.put(parkSlot.toJson());
        }
        json.addObject("parkSlots",jsonarray);
        return json;
    }

    public float getSum(){
        return (float) sumFees.getObject();
    }

    public double getAvgParkDuration(){
        return (double) avgParkDuration.getObject();
    }

    public int getId() {
        return id;
    }

    public Level copy() {
        SumFees cpSumFees = new SumFees((Float) this.sumFees.getObject());
        AvgParkDuration cpAvgParkDuration = new AvgParkDuration((Double) this.avgParkDuration.getObject());
        List<ParkSlot> cpParkSlots = this.parkSlots.stream().map(ParkSlot::copy).collect(Collectors.toList());

        return new Level(name,normCap,famCap,spnCap,fees,id, cpParkSlots,cpAvgParkDuration,cpSumFees);
    }
}
