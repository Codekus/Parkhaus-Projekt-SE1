package template;

import parkhaus.JSON;

public abstract class StatisticsTemplate {

    protected String key;
    protected Object object;


    public final void addToJson(JSON json){
        json.addObject(key, object);
    }

    public abstract void setParams(Object object);

    public Object getObject() {
        return object;
    }
}
