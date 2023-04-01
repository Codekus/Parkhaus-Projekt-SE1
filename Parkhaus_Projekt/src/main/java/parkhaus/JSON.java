package parkhaus;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.json.simple.parser.JSONParser;

public final class JSON extends JSONObject implements Serializable  {

    private static File configFile;
    private static JSON configJSON;
    private static boolean failed = false;


    private static final Logger LOGGER = Logger.getLogger(JSON.class.getName());
    public JSON() {
        super();
    }
    public JSON(String toParse) {
        super(toParse);
    }

    public JSON(String key, Object val) {
        super();
        this.addObject(key,val);
    }

    public void addObject(String key, Object val){
        if(val instanceof String || val instanceof Number || val instanceof JSONObject || val instanceof JSONArray){
            super.put(key, val);
        }
        else if(val instanceof Boolean) {
            this.addObject(key, val.toString());
        }
        else if(val == null)  {
            this.addObject(key, "null");
        }
        else if(val.getClass().isArray()) {
            this.addObject(key, new JSONArray(val));
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public List<ArrayList<Double>> getDoubleArray(String key){
        JSONArray temp = (JSONArray) this.get(key);
        List<ArrayList<Double>> outerArrayList = new ArrayList<>();
        for(int i = 0; i<temp.length(); i++){
            ArrayList<Double> innerArrayList = new ArrayList<>();
            for(int j = 0; j < ((JSONArray) temp.get(i)).length(); j++){
                innerArrayList.add(Double.parseDouble((String) ((JSONArray) temp.get(i)).get(j)) );
            }
            outerArrayList.add(innerArrayList);
        }
        return outerArrayList;
    }

    private static void setConfigPath(){
        String path = JSON.class.getResource("").getPath();
        File thisFile = new File(path);
        File parent = thisFile.getParentFile();

        while (parent != null) {
            File file = new File(parent.getAbsolutePath() + File.separatorChar + "config.json");
            if (file.exists()) {
                configFile = file;
                return;
            }
            parent = parent.getParentFile();
        }
        configFile = null;
        File myObj = new File("./config.json");
        try{
            if (myObj.createNewFile()) {
                configFile = myObj;
            }else{
                configFile = myObj;
            }
            FileWriter f = new FileWriter(configFile);
            f.write("{\"fees\":\"5\",\"levels\":[{\"normal\":3,\"name\":\"testLevel\",\"disabled\":3,\"family\":3},{\"normal\":3,\"name\":\"testLevel\",\"disabled\":3,\"family\":3}]}");
            f.flush();

        }catch (Exception e){
            LOGGER.warning("Failed to create Json Config");
        }

    }


    public static JSON getConfigJson() {

        if(configFile == null){
            setConfigPath();
        }
        if(!failed){
            try (BufferedReader br = new BufferedReader(new FileReader(configFile))){
                String brinput = br.readLine();
                if(brinput == null){
                    JSON.writeToConfig(new JSON("{\"fees\":0}"));
                }
                JSONParser parser = new JSONParser();

                Object obj = parser.parse(new FileReader(configFile));

                assert obj != null;

                configJSON = new JSON(obj.toString());
            } catch (Exception e) {
                configJSON = null;
                LOGGER.warning("Failed to get Json Config");
            }
        }

        if(configJSON == null && !failed){
            configJSON = new JSON("{\"fees\":\"2\",\"levels\":[{\"normal\":2,\"name\":\"A\",\"disabled\":2,\"family\":2},{\"normal\":2,\"name\":\"B\",\"disabled\":2,\"family\":2}]}");
            failed = true;
        }
        return configJSON;

    }

    public static void writeToConfig(JSON json) {
        if(configFile == null){
            setConfigPath();
        }
        if(!failed ){
            try (FileWriter f = new FileWriter(configFile)) {
                f.write(json.toString());
                f.flush();

            } catch (IOException e) {
                LOGGER.warning("Failed to write Json Config!");
            }
        }
        else{
            configJSON = json;
        }

    }

    public JSON copy() {
        return SerializationUtils.clone(this);
    }


}