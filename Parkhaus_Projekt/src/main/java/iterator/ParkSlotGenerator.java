package iterator;

import parkhaus.ParkSlot;
import parkhaus.Type;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ParkSlotGenerator implements Iterable<ParkSlot>, Serializable {

    private int normCount;
    private int famCount;
    private int spnCount;

    public void setCounts(int normCount, int famCount, int spnCount){
        this.normCount = normCount;
        this.famCount = famCount;
        this.spnCount = spnCount;
    }

    @Override
    public Iterator<ParkSlot> iterator() {
        return new ParkSlotIterator();
    }

    private class ParkSlotIterator implements Iterator<ParkSlot>, Serializable{
        int count = 1;

        @Override
        public boolean hasNext() {
           return (normCount > 0 || famCount > 0 || spnCount > 0);
        }
        @Override
        public ParkSlot next() {
            if(hasNext()){
                if(normCount > 0){
                    normCount--;
                    return new ParkSlot(Type.NORMAL, count++);
                } else if(famCount > 0){
                    famCount--;
                    return new ParkSlot(Type.FAMILY, count++);
                } else {
                    spnCount--;
                    return new ParkSlot(Type.SPECIAL_NEED, count++);
                }
            }
            throw new NoSuchElementException();
        }
    }
}
