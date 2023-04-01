package statistics;

import parkhaus.CarPark;
import parkhaus.Type;
import template.StatisticsTemplate;

import java.io.Serializable;
import java.util.Arrays;



public class ChangedTypeOverall extends StatisticsTemplate implements Serializable {

    private int fam = 0;
    private int spn = 0;

    public ChangedTypeOverall(){
        this.key = "changedTypeOverall";
        this.object = new double[] {0.0, 0.0};
    }

    private ChangedTypeOverall(int fam, int spn, Object obj){
        this.key = "changedTypeOverall";
        this.fam = fam;
        this.spn = spn;
        this.object = obj;
    }

    @Override
    public void setParams(Object object) {
        if(Type.FAMILY == object){
            fam++;
        }
        else if(Type.SPECIAL_NEED == object){
            spn++;
        }
        this.object = calcAvgDifferentSlot();
    }

    private String calcAvgDifferentSlot(){
        int[] tmp = new int[2];
        int count = CarPark.getInstance().getOverallCounter();
        tmp[0] =(int) (((double)fam / count)*100.0);
        tmp[1] =(int) (((double)spn / count)*100.0);
        return Arrays.toString(tmp);
    }

    public ChangedTypeOverall copy(){
        return new ChangedTypeOverall(fam,spn,object);
    }

}
