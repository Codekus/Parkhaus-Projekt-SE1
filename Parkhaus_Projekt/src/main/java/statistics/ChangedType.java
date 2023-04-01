package statistics;

import parkhaus.CarPark;
import parkhaus.Type;
import template.StatisticsTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;


public class ChangedType extends StatisticsTemplate implements Serializable {

    private int fam = 0;
    private int spn = 0;

    public ChangedType(){
        this.key = "changedType";
        this.object = new double[] {0.0, 0.0};
    }

    private ChangedType(int fam, int spn, Object obj){
        this.key = "changedType";
        this.fam = fam;
        this.spn = spn;
        this.object = obj;
    }

    @Override
    public void setParams(Object object) {
        helper(object,+1);
        this.object = calcAvgDifferentSlot();
    }

    public void reduce(Object object){
        helper(object,-1);
        this.object = calcAvgDifferentSlot();
    }
    private void helper(Object object, int i){
        if(Type.FAMILY == object){
            fam += i;
        }
        else if(Type.SPECIAL_NEED == object){
            spn += i;
        }
    }

    private String calcAvgDifferentSlot(){
        int[] tmp = new int[2];
        Map<Type,Integer> count = CarPark.getInstance().getCounter();
        tmp[0] =(int) (((double)fam / count.get(Type.FAMILY))*100.0);
        tmp[1] =(int) (((double)spn / count.get(Type.SPECIAL_NEED))*100.0);
        return Arrays.toString(tmp);
    }

    public ChangedType copy(){
        return new ChangedType(fam,spn,object);
    }

}
