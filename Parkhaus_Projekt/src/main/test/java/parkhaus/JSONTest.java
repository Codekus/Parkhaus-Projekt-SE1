package parkhaus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
class JSONTest {

    Integer[] inter;
    JSON json;
    JSON json1;
    JSON json2;
    JSON json3;

    @BeforeEach
    void setUp() {
        json = new JSON();
        json1 = new JSON();
        json2 = new JSON();
        json3 = new JSON();

        inter = new Integer[5];
        for(int i = 0; i<inter.length; i++){
            inter[i] = i;
        }
    }

    @Test
    @DisplayName("addObject: adds multiple objects and checks format")
    void addObject_AddVariousObjects_JSONFormatsCorrectly(){
        String key1 = "testKey1";
        String key2 = "testKey2";
        String key3 = "testKey3";
        int value1 = 5;
        double value2 = 4.0;
        String value3 = "StringValue";

        json.addObject(key1, value1);
        json.addObject(key2, value2);
        json.addObject(key3, value3);

        Assertions.assertEquals(json.get(key1), value1);
        Assertions.assertEquals(json.get(key2), value2);
        Assertions.assertEquals(json.get(key3), value3);

        Assertions.assertTrue(json.has(key1));
        Assertions.assertTrue(json.has(key2));
        Assertions.assertTrue(json.has(key3));
    }

    @Test
    @DisplayName("addObject: add JSON objects another JSON object and checks format")
    void addObject_addSeveralJSONObjects_returnsCorrectFormat(){
        json1.addObject("first",1);
        json.addObject("second",2);
        json.addObject("third",inter);
        json.addObject("fourth", json1);

        Assertions.assertEquals("{\"third\":[0,1,2,3,4],\"fourth\":{\"first\":1},\"second\":2}", json.toString(),
                "Json wurde falsch ausgegeben!");
    }

    @Test
    @DisplayName("addObject: add nested JSON objects upon other JSON objects and checks format")
    void addObject_addJSONObjectsOnEachOther_returnsCorrectFormat(){
        json3.addObject("sixth",inter);
        json2.addObject("fifth", json3);
        json1.addObject("first", json2);
        json.addObject("second",2);
        json.addObject("third",inter);
        json.addObject("fourth", json1);

        Assertions.assertEquals("{\"third\":[0,1,2,3,4],\"fourth\":{\"first\":{\"fifth\":{\"sixth\":[0,1,2,3,4]}}},\"second\":2}", json.toString(),
                "Json wurde falsch ausgegeben!");
    }


}
