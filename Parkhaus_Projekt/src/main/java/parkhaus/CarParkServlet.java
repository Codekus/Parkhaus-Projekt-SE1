package parkhaus;

import command.*;
import org.json.JSONException;
import template.ChartTemplate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.apache.commons.text.StringEscapeUtils;

@WebServlet(name = "CarParkServlet", value = "/CarParkServlet")
public class CarParkServlet extends HttpServlet {
    private static int levelCount;
    private static CarPark carPark;
    private static final Logger LOGGER = Logger.getLogger(CarParkServlet.class.getName());
    private static final String EVENT_STREAM = "text/event-stream";
    private static final String UTF = "UTF-8";
    private static final String DATA = "data: ";
    private static final String COUNT_PARKED_CARS = "countParkedCars";
    private static final String OCTET_STREAM = "application/octet-stream";
    private static final String CONTENT_DISPOSITION = "Content-disposition";
    private static final String CAPACITY = "capacity";
    private static final String JSON_ERROR_MSG = "Failed to Write to config!";
    private static final String SERVLET_EXCEPTION_MSG = "Servlet exception!";
    private static final String IO_ERROR_MSG = "IO exception!";
    static ChartTemplate chartTemplate = new ChartTemplate();
    static ChartTemplate chartTemplateCapacity = new ChartTemplate();

    // Commands
    private static Command command;
    private static CarParkCommands carParkCommands;
    private static Command undo;
    private static Command enter;
    private static Command enterNormal;
    private static Command enterFamily;
    private static Command enterSpnNeed;


    public static void safeCommand(Command command){
        CarParkServlet.command = command;
    }

    public void activateCommand()  {
        command.execute();
    }




    protected synchronized void handleEventStream(HttpServletRequest request, HttpServletResponse response){
        String cmd = request.getParameter("cmd");
        response.setContentType(EVENT_STREAM);
        response.setCharacterEncoding(UTF);
        PrintWriter out = null;
        while (true) {
            try {
                out = response.getWriter();
                switch (cmd) {
                    case "status":
                        out.write(statusUpdate());
                        break;
                    case "chartCount":
                        out.write(chartCountUpdate());
                        break;
                    case CAPACITY:
                        out.write(capacityUpdate());
                        break;
                    default:
                        response.setContentType("text/html");
                        break;
                }
                out.flush();
                wait();
            }
            catch (IOException | InterruptedException e) {
                assert out != null;
                out.close();
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response){
        String cmd = request.getParameter("cmd");
        if(cmd == null){
            cmd = "";
        }
        PrintWriter out;
        try {
            switch (cmd) {
                case "status":
                case CAPACITY:
                case "chartCount":
                    handleEventStream(request, response);
                    break;
                case "maxCountCars":
                    out = response.getWriter();
                    out.println(carPark.getStatsJson().get("maxCars"));
                    out.flush();
                    break;
                case COUNT_PARKED_CARS:
                    out = response.getWriter();
                    out.print(carPark.getStatsJson().get(COUNT_PARKED_CARS));
                    out.flush();
                    break;
                case "sumFees":
                    out = response.getWriter();
                    out.println(String.format("%.2f", (carPark.getStatsJson().get("sumFees"))));
                    out.flush();
                    break;
                case "Download Stats":
                    response.setContentType(OCTET_STREAM);
                    response.setHeader(CONTENT_DISPOSITION, "attachment; filename=stats.json");
                    out = response.getWriter();
                    out.write(carPark.getStatsJson().toString());
                    out.flush();
                    break;
                case "Download Config":
                    carPark = CarPark.getInstance();
                    carPark.loadJson();
                    response.setContentType(OCTET_STREAM);
                    response.setHeader(CONTENT_DISPOSITION, "attachment; filename=config.json");
                    out = response.getWriter();
                    out.write(carPark.getJson().toString());
                    out.flush();
                    break;
                case "Download History":
                    carPark = CarPark.getInstance();
                    carPark.loadJson();
                    response.setContentType(OCTET_STREAM);
                    response.setHeader(CONTENT_DISPOSITION, "attachment; filename=history.json");
                    out = response.getWriter();
                    out.write(carPark.getCarParkHistoryJSON().toString());
                    out.flush();
                    break;
                case "avgParkDuration":
                    out = response.getWriter();
                    out.println(String.format("%.2f", (carPark.getStatsJson().get("avgParkDuration"))));
                    out.flush();
                    break;
                case "changedType":
                    out = response.getWriter();
                    out.println(carPark.getStatsJson().get("changedType"));
                    out.flush();
                    break;
                case "changedTypeOverall":
                    out = response.getWriter();
                    out.println(carPark.getStatsJson().get("changedTypeOverall"));
                    out.flush();
                    break;
                default:
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    break;
            }
        }
        catch(IOException ioException){
            LOGGER.warning(IO_ERROR_MSG);
        }
        catch (JSONException e) {
            LOGGER.warning(JSON_ERROR_MSG);
        }
        catch(ServletException servletException){
            LOGGER.warning(SERVLET_EXCEPTION_MSG);
        }
    }



    @Override
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response){
        try{
        PrintWriter out = response.getWriter();

        if(request.getParameter("enter") != null){
            request.setAttribute("CarPark", carPark);
        }


            String cmd = request.getParameter("cmd");
            switch(cmd){
                case "enter":
                    safeCommand(enter);
                    activateCommand();
                    notifyAll();
                    break;
                case "enterNormal":
                    safeCommand(enterNormal);
                    activateCommand();
                    notifyAll();
                    break;
                case "enterSpecialNeed":
                    safeCommand(enterSpnNeed);
                    activateCommand();
                    notifyAll();
                    break;
                case "enterFamily":
                    safeCommand(enterFamily);
                    activateCommand();
                    notifyAll();
                    break;
                case "leave":
                    Command leave = new LeaveCar(carParkCommands, Integer.parseInt(request.getParameter("parkingSlot")));
                    safeCommand(leave);
                    activateCommand();

                    notifyAll();
                    response.setContentType(EVENT_STREAM);
                    out.println(carPark.getLeaveCarJSON());
                    out.flush();
                    break;
                case "Generate":
                    writeToConfig(request);
                    CarPark.setNull();
                    carPark = CarPark.getInstance();
                    carPark.loadJson();
                    initCommands();
                    request.getRequestDispatcher("CarParkSimulation.jsp").forward(request, response);
                    break;
                case "Load from Config":
                    carPark = CarPark.getInstance();
                    carPark.loadJson();
                    initCommands();
                    request.getRequestDispatcher("CarParkSimulation.jsp").forward(request, response);
                    break;
                case "Edit Config":
                    levelCount = JSON.getConfigJson().getJSONArray("levels").length();
                    request.getRequestDispatcher("configDetails.jsp").forward(request, response);
                    break;
                case "undo":
                    safeCommand(undo);
                    activateCommand();
                    notifyAll();
                    break;
                default:
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    break;
            }

        } catch (IllegalStateException e){
            LOGGER.warning("Failure inside doPost");
        }
        catch(IOException ioException){
            LOGGER.warning(IO_ERROR_MSG);
        }
        catch (JSONException e) {
            LOGGER.warning(JSON_ERROR_MSG);
        }
        catch(ServletException servletException){
            LOGGER.warning(SERVLET_EXCEPTION_MSG);
        }
        catch(NumberFormatException numberFormatException){
            LOGGER.warning("Wrong number format!");
        }
    }

    private static void initCommands(){
        carParkCommands = new CarParkCommands();
        enter = new EnterCar(carParkCommands);
        enterNormal = new EnterNormalCar(carParkCommands);
        enterFamily = new EnterFamilyCar(carParkCommands);
        enterSpnNeed = new EnterSpecialNeedsCar(carParkCommands);
        undo = new Undo(carParkCommands);
    }

    public static void writeToConfig(HttpServletRequest request){
        try {

            JSON json = JSON.getConfigJson();
            List<String> nameList = new ArrayList<>();
            List<Integer> normalAmount = new ArrayList<>();
            List<Integer> familyAmount = new ArrayList<>();
            List<Integer> disabledAmount = new ArrayList<>();
            levelCount = Integer.parseInt(request.getParameter("levelAmount"));
            for (int i = 1; i <= levelCount; i++) {
                nameList.add(StringEscapeUtils.escapeHtml4(request.getParameter("levelName" + i)));
                normalAmount.add(Integer.parseInt((request.getParameter("normalAmount" + i))));
                familyAmount.add(Integer.parseInt((request.getParameter("familyAmount" + i))));
                disabledAmount.add(Integer.parseInt((request.getParameter("disabledAmount" + i))));
            }

            assert json != null;

            json.addObject("levels", ConfigHelper.createJSONArray(levelCount, nameList, normalAmount, familyAmount, disabledAmount));
            json.addObject("fees", request.getParameter("fees"));
            JSON.writeToConfig(Objects.requireNonNull(json));

        }  catch (JSONException e) {
            LOGGER.warning(JSON_ERROR_MSG);
        }
    }

    private String statusUpdate(){
        return DATA + carPark.getStatus() + "\n\n";
    }

    private String chartCountUpdate(){
        int max = carPark.getStatsJson().getInt("maxCars");
        int count = carPark.getStatsJson().getInt(COUNT_PARKED_CARS);
        chartTemplate.setX("Free parkslots", "Taken parkslots");
        chartTemplate.setY(max - count, count);
        return DATA + chartTemplate.chart() + "\n\n";
    }

    private String capacityUpdate(){
        List<ArrayList<Double>> list = carPark.getStatsJson().getDoubleArray(CAPACITY);
        JSON tmp = new JSON();
        tmp.put("y", chartTemplateCapacity.valueHelper(list));
        tmp.put("x", chartTemplateCapacity.nameHelper(carPark.getStatus()));
        return DATA + tmp + "\n\n";
    }


}
