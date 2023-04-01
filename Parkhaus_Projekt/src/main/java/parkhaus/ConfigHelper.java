package parkhaus;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

public class ConfigHelper implements Serializable {

    private ConfigHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static JSON createJSON(String key, Object value) {

        return new JSON(key, value);
    }

    public static JSONArray createJSONArray(int amount, List<String> name, List<Integer> normalAmount,
                                            List<Integer> familyAmount, List<Integer> disabledAmount) {

        JSONArray jsonarray = new JSONArray();
        for(int i = 0; i < amount; i++){
            JSON json = new JSON();
            json.addObject("name", name.get(i));
            json.addObject("normal", normalAmount.get(i));
            json.addObject("family", familyAmount.get(i));
            json.addObject("disabled", disabledAmount.get(i));
            jsonarray.put(json);
        }
        return jsonarray;
    }

}
