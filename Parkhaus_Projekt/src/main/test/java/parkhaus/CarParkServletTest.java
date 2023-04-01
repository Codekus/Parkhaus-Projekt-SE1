package parkhaus;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


class CarParkServletTest {

    @Spy
    private CarParkServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    RequestDispatcher dispatcher;


    private static final String COUNT_PARKED_CARS = "countParkedCars";
    private static final String MAX_COUNT_CARS = "maxCountCars";
    private static final String SUM_FEES = "sumFees";
    private static final String CAPACITY = "capacity";
    private static final String CHANGED_TYPE = "changedType";
    private static final String AVG_PARK_DURATION = "avgParkDuration";

    private static final String ENTER = "enter";
    private static final String LEAVE = "leave";
    private static final String ENTER_NORMAL = "enterNormal";
    private static final String ENTER_FAMILY = "enterFamily";
    private static final String ENTER_SPN_NEED = "enterSpecialNeed";
    private static final String DOWNLOAD_STATS = "Download Stats";
    private static final String DOWNLOAD_CONFIG = "Download Config";
    private static final String DOWNLOAD_HISTORY = "Download History";
    private static final String LOAD_FROM_CONFIG = "Load from Config";
    private static final String CMD = "cmd";



    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }



    @Test
    @DisplayName("doPost: doPost forwards Response to CarParkSimulation.jsp")
    void doPost_sendPostRequestLoadFromConfig_forwardsResponse() throws Exception {
        loadCarParkFromConfig();
    }

    @Test
    @DisplayName("doPost: doPost forwards Response to configDetails.jsp")
    void doPost_sendPostRequestEditConfig_forwardsResponse() throws Exception {
        when(request.getParameter(CMD)).thenReturn("Edit Config");
        when(request.getRequestDispatcher("configDetails.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).getRequestDispatcher("configDetails.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost: overrides CarPark config and generates CarPark")
    void doPost_generateCarPark_forwardsResponse() throws Exception {
        generateCarPark();
    }

    @Test
    @DisplayName("doPost: adds cars to car park")
    void doPost_entersCar_carCountIncreases() throws Exception {
        CarPark.setNull();
        loadCarParkFromConfig();
        CarPark carPark = CarPark.getInstance();
        when(request.getParameter(CMD)).thenReturn(ENTER);
        for(int i = 1; i < 4; i++){
            servlet.doPost(request, response);
            Assertions.assertEquals(i, carPark.getStatsJson().get(COUNT_PARKED_CARS));
        }
    }

    @Test
    @DisplayName("doPost: each car type gets added to car park")
    void doPost_enterEachCarOnce_carCountIncreases() throws Exception {
        CarPark.setNull();
        loadCarParkFromConfig();
        CarPark carPark = CarPark.getInstance();
        String enterString;
        for(int i = 1; i < 4; i++){
            if(i == 1){
                enterString = ENTER_NORMAL;
            } else if (i == 2){
                enterString = ENTER_FAMILY;
            } else {
                enterString = ENTER_SPN_NEED;
            }
            when(request.getParameter(CMD)).thenReturn(enterString);
            servlet.doPost(request, response);
            Assertions.assertEquals(i, carPark.getStatsJson().get(COUNT_PARKED_CARS));
        }
    }

    @Test
    @DisplayName("doPost: cars enter and leave the car park")
    void doPost_leaveCarsAfterEnter_carCountDecreases() throws Exception {
        CarPark.setNull();
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        for(int i = 0; i < 3; i++){
            when(request.getParameter(CMD)).thenReturn(ENTER_NORMAL);
            servlet.doPost(request, response);
        }

        CarPark carPark = CarPark.getInstance();
        Assertions.assertEquals(3, carPark.getStatsJson().get(COUNT_PARKED_CARS));
        for(int i = 0; i <= 2; i++){
            when(request.getParameter("parkingSlot")).thenReturn(i + "");
            when(request.getParameter(CMD)).thenReturn(LEAVE);
            servlet.doPost(request, response);
            Assertions.assertEquals(3-(i+1), carPark.getStatsJson().get(COUNT_PARKED_CARS));
        }
    }



    @Test
    @DisplayName("doGet: amount of maximumum number of cars gets forwarded correctly")
    void doGet_maxCountCars_returnsRightCount() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(CMD)).thenReturn(MAX_COUNT_CARS);
        servlet.doGet(request, response);

        verify(request, atLeast(1)).getParameter(CMD);
        writer.flush();
        Assertions.assertTrue(stringWriter.toString().contains("9"));
    }

    @Test
    @DisplayName("doGet: currently parked cars gets forwarded correctly")
    void doGet_currentlyCountCars_returnsRightCount() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter(CMD)).thenReturn(ENTER);
        servlet.doPost(request, response);

        when(request.getParameter(CMD)).thenReturn(COUNT_PARKED_CARS);
        servlet.doGet(request, response);

        verify(request, atLeast(1)).getParameter(CMD);
        writer.flush();
        Assertions.assertTrue(stringWriter.toString().contains("1"));
    }

    @Test
    @DisplayName("doGet: fees are forwarded correctly")
    void doGet_sumFees_sendRequest() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        enterAndLeaveCar();

        when(request.getParameter(CMD)).thenReturn(SUM_FEES);
        servlet.doGet(request, response);

        verify(request, atLeast(3)).getParameter(CMD);
        writer.flush();

    }

    @Test
    @DisplayName("doGet: download stats content gets forwarded correctly")
    void doGet_downloadStats_sendRequest() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        CarPark carPark = CarPark.getInstance();

        enterAndLeaveCar();

        when(request.getParameter(CMD)).thenReturn(DOWNLOAD_STATS);
        servlet.doGet(request, response);

        verify(request, atLeast(3)).getParameter(CMD);

        Assertions.assertTrue(stringWriter.toString().contains(carPark.getStatsJson().toString()));
        writer.flush();
    }



    @Test
    @DisplayName("doGet:  download config content gets forwarded correctly")
    void doGet_downloadConfig_sendRequest() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        CarPark carPark = CarPark.getInstance();

        when(request.getParameter(CMD)).thenReturn(DOWNLOAD_CONFIG);
        servlet.doGet(request, response);

        verify(request, atLeast(1)).getParameter(CMD);
        Assertions.assertTrue(stringWriter.toString().contains(carPark.getJson().toString()));

        writer.flush();
    }

    @Test
    @DisplayName("doGet:  download history content gets forwarded correctly")
    void doGet_downloadHistory_sendRequest() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        CarPark carPark = CarPark.getInstance();

        enterAndLeaveCar();

        when(request.getParameter(CMD)).thenReturn(DOWNLOAD_HISTORY);
        servlet.doGet(request, response);

        verify(request, atLeast(1)).getParameter(CMD);
        Assertions.assertTrue(stringWriter.toString().contains(carPark.getCarParkHistoryJSON().toString()));

        writer.flush();
    }

    @Test
    @DisplayName("doGet: average park duration gets forwarded correctly")
    void doGet_avgParkDuration_sendRequest() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        CarPark carPark = CarPark.getInstance();

        enterAndLeaveCar();

        when(request.getParameter(CMD)).thenReturn(AVG_PARK_DURATION);
        servlet.doGet(request, response);

        verify(request, atLeast(4)).getParameter(CMD);
        Assertions.assertTrue(stringWriter.toString().replaceAll(",",".").contains(carPark.getStatsJson().get(AVG_PARK_DURATION).toString()));
        writer.flush();
    }

    @Test
    @DisplayName("doGet: changed type stat gets forwarded correctly")
    void doGet_changedType_sendRequest() throws Exception {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        CarPark carPark = CarPark.getInstance();

        when(request.getParameter(CMD)).thenReturn(ENTER_FAMILY);
        servlet.doPost(request, response);
        servlet.doPost(request, response);
        servlet.doPost(request, response);
        servlet.doPost(request, response);

        when(request.getParameter(CMD)).thenReturn(CHANGED_TYPE);
        servlet.doGet(request, response);

        verify(request, atLeast(3)).getParameter(CMD);
        Assertions.assertTrue(stringWriter.toString().contains(carPark.getStatsJson().get(CHANGED_TYPE).toString()));
        writer.flush();
    }

    @Test
    @DisplayName("statusUpdate: Returns the right status for updating Parking slot simulation")
    void statusUpdate_callsCarParkStatus_sendsStringToBrowser() throws ServletException, IOException {
        generateCarPark();
        CarPark carPark = CarPark.getInstance();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(CMD)).thenReturn("status");
        Thread.currentThread().interrupt();
        servlet.doGet(request, response);
        Assertions.assertTrue(stringWriter.toString().contains(carPark.getStatus()));
    }

    @Test
    @DisplayName("CapacityUpdate: calls the handleEventStream and forward response")
    void statusUpdate_callsCapacityUpdate_sendsStringToBrowser() throws ServletException, IOException {
        generateCarPark();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(CMD)).thenReturn(CAPACITY);
        Thread.currentThread().interrupt();
        servlet.doGet(request, response);
        Assertions.assertNotNull(stringWriter.toString());
        verify(request, times(3)).getParameter(request.getParameter("cmd"));
    }

    @Test
    @DisplayName("statusUpdate: sends right response to browser")
    void chartCountUpdate_callsChartCountUpdate_sendsStringToBrowser() throws ServletException, IOException {
        generateCarPark();
        CarPark carPark = CarPark.getInstance();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(CMD)).thenReturn("chartCount");
        Thread.currentThread().interrupt();
        servlet.doGet(request, response);
        Assertions.assertTrue(stringWriter.toString().contains(carPark.getStatsJson().getInt(COUNT_PARKED_CARS)+ ""));
        Assertions.assertTrue(stringWriter.toString().contains(carPark.getStatsJson().getInt("maxCars")+ ""));
    }


    private void loadCarParkFromConfig() throws ServletException, IOException {
        when(request.getParameter(CMD)).thenReturn(LOAD_FROM_CONFIG);
        when(request.getRequestDispatcher("CarParkSimulation.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).getRequestDispatcher("CarParkSimulation.jsp");
        verify(dispatcher).forward(request, response);
    }

    private void generateCarPark() throws ServletException, IOException {
        when(request.getParameter(CMD)).thenReturn("Generate");
        when(request.getRequestDispatcher("CarParkSimulation.jsp")).thenReturn(dispatcher);
        when(request.getParameter("levelAmount")).thenReturn("1");
        when(request.getParameter("levelName1")).thenReturn("testLevel");

        when(request.getParameter("normalAmount1")).thenReturn("3");
        when(request.getParameter("familyAmount1")).thenReturn("3");
        when(request.getParameter("disabledAmount1")).thenReturn("3");
        when(request.getParameter("fees")).thenReturn("5");
        servlet.doPost(request, response);

        verify(request).getRequestDispatcher("CarParkSimulation.jsp");
        verify(dispatcher).forward(request, response);
    }

    private void enterAndLeaveCar() {
        when(request.getParameter(CMD)).thenReturn(ENTER_NORMAL);
        servlet.doPost(request, response);

        when(request.getParameter("parkingSlot")).thenReturn("1");
        servlet.doPost(request, response);
        when(request.getParameter(CMD)).thenReturn(LEAVE);
        servlet.doPost(request, response);
    }



}
