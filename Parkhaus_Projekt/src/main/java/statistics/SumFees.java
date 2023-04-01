package statistics;

import template.StatisticsTemplate;

import java.io.Serializable;

public class SumFees extends StatisticsTemplate implements Serializable {
    public SumFees(float sum){
        this.object = sum;
    }
    @Override
    public void setParams(Object object) {
        this.key = "sumFees";
        this.object = object;
    }

}
