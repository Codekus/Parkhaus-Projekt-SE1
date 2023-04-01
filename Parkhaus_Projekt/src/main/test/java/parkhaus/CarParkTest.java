package parkhaus;

import org.awaitility.Duration;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

class CarParkTest {

    CarPark carPark;
    Car car;
    static final double EPSILON = 0.3;
    static final String COUNTPARKEDCARS = "countParkedCars";

    @BeforeEach
    void setUp() {
        JSON testConfig = new JSON("{\"fees\":\"2\",\"levels\":[{\"normal\":2,\"name\":\"A\",\"disabled\":2,\"family\":2},{\"normal\":2,\"name\":\"B\",\"disabled\":2,\"family\":2}]}");
        JSON.writeToConfig(testConfig);
        CarPark.setNull();
        carPark = CarPark.getInstance();
        car = new Car(Type.NORMAL);
    }


    @Test
    @Order(1)
    @DisplayName("entry: adds a car to the car park")
    void entryAddsCarsToCarPark_countParkedCarsIncreases() {

        Assertions.assertEquals(0, carPark.getStatsJson().get(COUNTPARKEDCARS));

        carPark.entry(new Car(Type.NORMAL));
        carPark.entry(new Car(Type.FAMILY));
        carPark.entry(new Car(Type.SPECIAL_NEED));
        Assertions.assertEquals(3, carPark.getStatsJson().get(COUNTPARKEDCARS));

    }

    @Test
    @DisplayName("entry: returns an error message, when entering full car park")
    void entryReturnsError_normalFamilySpecialNeed(){
        for(int i = 1; i <= 4; i++){
            carPark.entry(new Car(Type.NORMAL));
            carPark.entry(new Car(Type.FAMILY));
            carPark.entry(new Car(Type.SPECIAL_NEED));
        }

        Assertions.assertFalse(carPark.entry(new Car(Type.NORMAL)));
        Assertions.assertFalse(carPark.entry(new Car(Type.FAMILY)));
        Assertions.assertFalse(carPark.entry(new Car(Type.SPECIAL_NEED)));
    }

    @Test
    @DisplayName("leave: car tries to leave empty car park")
    void leaveCar_leavesEmptyCarPark_returnsFalse(){
        Assertions.assertFalse(carPark.leave(1));
    }

    @Test
    @DisplayName("getPrice: calculates 'sumFees' correctly after cars leave")
    void getPrice_carLeavesCarPark_sumsUpPriceCorrectly() {
        carPark.entry(car);
        await().pollInterval(Duration.FIVE_SECONDS).atLeast(Duration.ONE_SECOND).atMost(Duration.TEN_SECONDS).untilAsserted(() -> {
            for(int i = 0; i < 13; i++){
                carPark.leave(i);
            }
            double sum = Math.abs((float) carPark.getStatsJson().get("sumFees") - 2);
            Assertions.assertTrue( sum <= EPSILON);
        });

        carPark.entry(car);
        await().pollInterval(Duration.FIVE_SECONDS).atLeast(Duration.ONE_SECOND).atMost(Duration.TEN_SECONDS).untilAsserted(() -> {
            for(int i = 0; i < 13; i++){
                carPark.leave(i);
            }
            double sum = Math.abs((float) carPark.getStatsJson().get("sumFees") - 4);
            Assertions.assertTrue( sum <= EPSILON);
        });

    }

    @Test
    @DisplayName("getStatus: formats JSON correctly")
    void getStatus_emptyCarParkStatus_formatsCorrectly() {
        final String REGEX_LEVELS = "\"normCap\":\\d*,\"famCap\":\\d*,\"name\":\"[a-zA-Z]\",\"parkSlots\":";
        final String REGEX_PARKING_SLOTS = "\\{\"color\":\"[#\\da-zA-Z]*\",\"freeStatus\":((\"true\",)|(\"false\",))\"id\":\\d*,\"type\":\"[A-Z_]*\"}";
        final String carParkStatus = carPark.getStatus();
        Assertions.assertTrue(Pattern.compile(REGEX_LEVELS).matcher(carParkStatus).find());
        Assertions.assertTrue(Pattern.compile(REGEX_PARKING_SLOTS).matcher(carParkStatus).find());
    }

    @Test
    @DisplayName("getMaxParkSlots: checks if maximum parkslots gets displayed correctly")
    void getMaxParkSlots_checksMaxParkslotsAfterCreating_returnsCorrectAmount() {
        int[][] maxParkSlots = carPark.getMaxParkSlots();
        int sum = Arrays.stream(maxParkSlots).collect(Collectors.toList()).stream().mapToInt(i-> Arrays.stream(i).sum()).sum();
        Assertions.assertEquals(12, sum);
    }


}
