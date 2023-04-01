package statistics;


import template.StatisticsTemplate;

import java.io.Serializable;

public class AvgParkDuration extends StatisticsTemplate implements Serializable {
    private int count = 0;
    private double duration = 0;
    public AvgParkDuration(double obj){
        this.object = obj;
    }
    @Override
    public void setParams( Object object) {
        count++;
        key = "avgParkDuration";
        duration += (double) object;
        this.object = calcAvgDuration();
    }

    private double calcAvgDuration(){
        return duration / count;
    }

}
