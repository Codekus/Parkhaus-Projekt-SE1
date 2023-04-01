package template;
import org.json.JSONArray;

import parkhaus.JSON;

import java.util.ArrayList;
import java.util.List;

public class ChartTemplate {

    JSON charts = new JSON();

    public JSONArray chart(){
        charts.addObject("type", "bar");
        return new JSONArray().put(charts);
    }

    public void setX(String... keys) {
        charts.put("x", keys);

    }

    public void setY(Object... objects){
        charts.put("y", objects);
    }

    public List<Double> valueHelper(List<ArrayList<Double>> list){
        List<Double> list1 = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).size(); j++){
                list1.add(list.get(i).get(j));
            }
        }
        return list1;
    }

    public List<String> nameHelper(String str){
        JSONArray status = new JSON(str).getJSONArray("levels");
        List<String> list = new ArrayList<>();
        for(int i = 0; i < status.length(); i++){
            list.add((String) status.getJSONObject(i).get("name"));
        }

        return list;
    }

}
