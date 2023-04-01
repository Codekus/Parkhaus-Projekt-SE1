package parkhaus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.NoSuchElementException;

class ParkSlotTest {

    ParkSlot parkslot;

    @BeforeEach
    void setUp(){
        parkslot = new ParkSlot(Type.NORMAL, 1);
    }

    @Test
    @DisplayName("getCar: returns correct car when getCar() called")
    void getCar_getsTheCarOnParkingSlot_returnsCar(){
        Car car = new Car(Type.NORMAL);
        parkslot.entry(car);

        Assertions.assertEquals(car, parkslot.getCar());
    }

    @Test
    @DisplayName("getCar: throwsing exception if parking is unsuccessful")
    void getCar_noParkedCar_throwsException(){

        Assertions.assertThrows(NoSuchElementException.class,
            parkslot::getCar
        );
    }

    @Test
    @DisplayName("entry: successfully enters parkslot")
    void entry_carEnters_returnsTrue(){
        for(int i = 0; i < 10; i++){
            Assertions.assertTrue(new ParkSlot(Type.NORMAL, i).entry(new Car(Type.NORMAL)));
        }
    }

    @Test
    @DisplayName("entry: car can't enter parkslot")
    void entry_carEntersBookedParkslot_returnsFalse(){
        Car car = new Car(Type.NORMAL);
        parkslot.entry(car);

        Assertions.assertFalse(parkslot.entry(new Car(Type.NORMAL)));

    }

    @Test
    @DisplayName("leave: car successfully leaves car")
    void leave_carLeavesParklot_returnsTrue(){
        Car car = new Car(Type.NORMAL);
        parkslot.entry(car);

        Assertions.assertTrue(parkslot.leave(1));
    }

    @Test
    @DisplayName("leave: car unsuccessfully leaves car")
    void leave_carLeavesParklot_returnsFalse(){
        Car car = new Car(Type.NORMAL);
        parkslot.entry(car);
        Assertions.assertFalse(parkslot.leave(2));
    }

    @Test
    @DisplayName("block: blocks parkslot")
    void block_parkslotGetsTaken_returnsTrue(){
        Assertions.assertTrue(parkslot.block());
    }

    @Test
    @DisplayName("block: blocking already blocked parkslot is unsuccessful")
    void block_blockingTakenParkSlot_returnsFalse(){
        parkslot.block();
        Assertions.assertFalse(parkslot.block());
    }

    @Test
    @DisplayName("unblock: unblocks parkslot")
    void unblock_parkslotGetsTaken_returnsTrue(){
        parkslot.block();
        Assertions.assertTrue(parkslot.unblock());
    }

    @Test
    @DisplayName("isFree: parkslot is free")
    void isFree_freeParkslot_returnsTrue(){
        Assertions.assertTrue(parkslot.isFree());
        parkslot.entry(new Car(Type.NORMAL));
        parkslot.leave(1);
        Assertions.assertTrue(parkslot.isFree());
    }

    @Test
    @DisplayName("isFree: parkslot is taken")
    void isFree_takenParkslot_returnsFalse(){
        parkslot.entry(new Car(Type.NORMAL));
        Assertions.assertFalse(parkslot.isFree());
    }

    @Test
    @DisplayName("getType: returns type of parkslot")
    void getType_getsParkslotsType_returnsType(){
        Type[] types = {Type.NORMAL, Type.FAMILY, Type.SPECIAL_NEED};

        for(Type type : types){
            ParkSlot tmp = new ParkSlot(type, 1);
            Assertions.assertEquals(type, tmp.getType());
        }

    }

    @Test
    @DisplayName("getId: returns parkslotID")
    void getId_getsParkslotsId_returnsId(){
        for(int i = 0; i < 10; i++){
            Assertions.assertEquals(i, new ParkSlot(Type.NORMAL, i).getId());
        }
    }

}
