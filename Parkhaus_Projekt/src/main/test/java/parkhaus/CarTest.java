package parkhaus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.Instant;

class CarTest {

    Car car1;
    Car car2;
    Car car3;
    Instant entryPoint;
    static final String REGEX = "[A-ZÖÜÄ]{1,3} [A-ZÖÜÄ]{1,2} [1-9]{1}[0-9]{1,3}";

    @BeforeEach
    void setUp(){
        entryPoint = Instant.now();
        car1 = new Car(Type.NORMAL);
        car2 = new Car(Type.FAMILY);
        car3 = new Car(Type.SPECIAL_NEED);
    }

    @Test
    @DisplayName("getType: Gibt Typ des Autofahrers an")
    void getType_ReturnsTypeOfCar_NormalFamilySpecialNeed(){
        Assertions.assertEquals(Type.NORMAL, car1.getType());
        Assertions.assertEquals(Type.FAMILY, car2.getType());
        Assertions.assertEquals(Type.SPECIAL_NEED, car3.getType());
    }

    @Test
    @DisplayName("getDuration: Gibt die Zeit des Aufenthalts des Autofahrers an")
    void getDuration_ReturnsDurationOfParkedCar_AboutFiveSeconds() throws InterruptedException {
        Thread.sleep(5000);
        Assertions.assertEquals((float) Math.round((CarIF.milliToHours(Duration.between(entryPoint, Instant.now()).toMillis()))*100.0) / 100.0 , car1.getDuration());
        Assertions.assertEquals((float) Math.round((CarIF.milliToHours(Duration.between(entryPoint, Instant.now()).toMillis()))*100.0) / 100.0 , car2.getDuration());
        Assertions.assertEquals((float) Math.round((CarIF.milliToHours(Duration.between(entryPoint, Instant.now()).toMillis()))*100.0) / 100.0 , car3.getDuration());
    }

    @Test
    @DisplayName("getLicensePlate: Gibt ein gueltiges Kennzeichen zurueck")
    void getLicensePlate_ReturnsLicensePlateAndChecksIfCorrect_MatchesRegex() {
        Assertions.assertTrue(car1.getLicensePlate().matches(REGEX));
        Assertions.assertTrue(car2.getLicensePlate().matches(REGEX));
        Assertions.assertTrue(car3.getLicensePlate().matches(REGEX));
    }

    @Test
    @DisplayName("getSlot: returns cars slotID")
    void getSlot_ReturnsSlotID_1_2_3(){
        car1.setSlot(1);
        car2.setSlot(2);
        car3.setSlot(3);
        Assertions.assertEquals(1, car1.getSlot());
        Assertions.assertEquals(2, car2.getSlot());
        Assertions.assertEquals(3, car3.getSlot());
    }

    @Test
    @DisplayName("getLevel: returns parked level of the car")
    void getLevel_ReturnsLevel_A_B_C(){
        car1.setLevel("A");
        car2.setLevel("B");
        car3.setLevel("C");
        Assertions.assertEquals("A", car1.getLevel());
        Assertions.assertEquals("B", car2.getLevel());
        Assertions.assertEquals("C", car3.getLevel());
    }

    @Test
    @DisplayName("getLevel: returns the right price")
    void getPrice_ReturnsPrice_1_2_3() throws InterruptedException {
        Thread.sleep(5000);
        car1.setPrice(1);
        car2.setPrice(2);
        car3.setPrice(3);
        Assertions.assertEquals(1.0, car1.getPrice());
        Assertions.assertEquals(2.0, car2.getPrice());
        Assertions.assertEquals(3.0, car3.getPrice());
    }
}
