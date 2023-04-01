package statistics;

import org.json.JSONArray;
import parkhaus.*;
import template.StatisticsTemplate;

import java.io.Serializable;

public class ParkSlotCapacity extends StatisticsTemplate implements Serializable {

    private static final String FALSE = "false";
    private static final String FREE_STATUS = "freeStatus";
    private static final String TYPE = "type";
    public ParkSlotCapacity(){
        this.key = "capacity";
        this.object = new String[][] {{"0.0","0.0","0.0"}};
    }
    @Override
    public void setParams(Object object) {
        this.object = object;
    }

    public String[][] calcValues(){
        CarPark carPark = CarPark.getInstance();
        int[][] maxParkSlots = carPark.getMaxParkSlots();
        String[][] values = new String[carPark.getLevels().size()][3];
        double[][] freeSlotsCount = getFreeSlotsCount();
        for(int i = 0; i < maxParkSlots.length; i++){
            values[i][0] = ((freeSlotsCount[i][0] / maxParkSlots[i][0]) *100) + "";
            values[i][1] = ((freeSlotsCount[i][1] / maxParkSlots[i][1]) *100) + "";
            values[i][2] = ((freeSlotsCount[i][2] / maxParkSlots[i][2]) *100) + "";
        }
        return values;
    }


    private double[][] getFreeSlotsCount() {
        CarPark carPark = CarPark.getInstance();
        double[][] freeSlots = new double[carPark.getLevels().size()][3];
        JSON statusJSON = new JSON(carPark.getStatus());
        JSONArray allLevels = statusJSON.getJSONArray("levels");
        for(int i = 0; i < allLevels.length(); i++){
            JSONArray jsonArray = allLevels.getJSONObject(i).getJSONArray("parkSlots");
            int normal = 0;
            int family = 0;
            int disabled = 0;
            for(int j = 0; j < jsonArray.length(); j++){
                if (jsonArray.getJSONObject(j).get(TYPE).equals("NORMAL") && jsonArray.getJSONObject(j).get(FREE_STATUS).equals(FALSE)){
                    freeSlots[i][0] = ++normal;
                } else if(jsonArray.getJSONObject(j).get(TYPE).equals("FAMILY") && jsonArray.getJSONObject(j).get(FREE_STATUS).equals(FALSE)){
                    freeSlots[i][1] = ++family;
                } else if(jsonArray.getJSONObject(j).get(TYPE).equals("SPECIAL_NEED") && jsonArray.getJSONObject(j).get(FREE_STATUS).equals(FALSE)){
                    freeSlots[i][2] = ++disabled;
                }
            }
        }
        return freeSlots;

    }

}
