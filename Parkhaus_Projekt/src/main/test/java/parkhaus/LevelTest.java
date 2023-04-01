package parkhaus;

import org.junit.jupiter.api.*;



class LevelTest {
    Level level;
    Car carN;
    Car carF;
    Car carS;

    @BeforeEach
    void setup(){
        level = new Level("test",2, 2, 2,2,2);
        carN = new Car(Type.NORMAL);
        carF = new Car(Type.FAMILY);
        carS = new Car(Type.SPECIAL_NEED);
    }
    @Test
    @DisplayName("entry: returns true if successful, false otherwise, when entering a level")
    void entry_RetrunsParkslotIDOrError_ParkslotIDOrError(){
        Assertions.assertTrue(level.entry(carN,1));
        Assertions.assertTrue( level.entry(carF,3));
        Assertions.assertTrue(level.entry(carF,4));
        Assertions.assertTrue( level.entry(carN,2));
        Assertions.assertTrue(level.entry(carS,5));
        Assertions.assertTrue(level.entry(carS,6));
        Assertions.assertFalse(level.entry(carF,4));
    }

    @Test
    @DisplayName("leave: returns true if successful, false otherwise, when leaving a level")
    void leave_ReturnsTrueORFalse_TrueFalse(){
        level.entry(carN,1);
        Assertions.assertTrue(level.leave(0));
        Assertions.assertFalse(level.leave(0));
    }

    @Test
    @DisplayName("getName: returns correct name of the level")
    void getName_ReturnsNameOfLevel_test(){
        Assertions.assertEquals("test", level.getName());
    }

    @Test
    @DisplayName("getFamCap: returns capacity of family parkslots in a level")
    void getFamCap_ReturnsFamilyCapacity_2(){
        Assertions.assertEquals(2, level.getFamCap());
    }

    @Test
    @DisplayName("getNormCap: returns capacity of normal parkslots in a level")
    void getNormCap_ReturnsNormalCapacity_2(){
        Assertions.assertEquals(2, level.getNormCap());
    }

    @Test
    @DisplayName("getSpnCap: returns capacity of special needs parkslots in a level")
    void getSpnCap_ReturnsSpecialNeedCapacity_2(){
        Assertions.assertEquals(2, level.getSpnCap());
    }
}
