# Parkhaus Team 1
### Praxisprojekt Software Engineering I (SE1)

**Autoren:**
- [*Markus Klassen* (mklass2s)](mailto:markus.klassen@smail.inf.h-brs.de)
- [*Leart Podrimqaku* (lpodri2s)](mailto:leart.podrimqaku@smail.inf.h-brs.de)
- [*Kevin Hahn* (khahn2s)](mailto:kevin.hahn@smail.inf.h-brs.de)
- [*Issam Hbib* (ihbib2s)](mailto:issam.hbib@smail.inf.h-brs.de)


SoSe 2022, Hochschule Bonn-Rhein-Sieg (H-BRS)

---

### Inhaltsverzeichnis
___
1. [Projektverzeichnis](#projektverzeichnis)
2. [Videos](#videos)
___


### Projektverzeichnis
___
1. Unser [Digitalisierungskonzept](wiki/Digitalisierungskonzept.pdf).
2. Die (priorisierten) [UserStories](wiki/User_Stories.pdf).
3. Unser [MVP](wiki/MVP.pdf) (Minimum Viable Product). 
4. Auswahl an UML-Diagrammen:
   - [UML Use Case-Diagramm](wiki/uml/UML_Use_Case_Diagramm.pdf).
   - [UML Klassendiagramm](wiki/uml/parkhaus.png)


5. Java-Interfaces
    <details><summary>parkhaus</summary>

    - [CarIF](src/main/java/parkhaus/CarIF.java)
    - [CarParkIF](src/main/java/parkhaus/CarParkIF.java)
    - [LevelIF](src/main/java/parkhaus/LevelIF.java)
    - [ParkSlotIF](src/main/java/parkhaus/ParkSlotIF.java)
   </details>

   <details><summary>command</summary>

    - [Command](src/main/java/command/Command.java)
   </details>
6. JUnit-Tests
    <details><summary>parkhaus</summary>

    - [CarParkServletTest](src/main/test/java/parkhaus/CarParkServletTest.java)
    - [CarParkTest](src/main/test/java/parkhaus/CarParkTest.java)
    - [CarTest](src/main/test/java/parkhaus/CarTest.java)
    - [JSONTest](src/main/test/java/parkhaus/JSONTest.java)
    - [LevelTest](src/main/test/java/parkhaus/LevelTest.java)
    - [ParkSlotTest](src/main/test/java/parkhaus/ParkSlotTest.java)

    </details>

7. Java-Klassen

   <details><summary>command</summary>

    - [CarParkCommands](src/main/java/command/CarParkCommands.java)
    - [EnterCar](src/main/java/command/EnterCar.java)
    - [EnterFamilyCar](src/main/java/command/EnterFamilyCar.java)
    - [EnterNormalCar](src/main/java/command/EnterNormalCar.java)
    - [EnterSpecialNeedsCar](src/main/java/command/EnterSpecialNeedsCar.java)
    - [LeaveCar](src/main/java/command/LeaveCar.java)
    - [Undo](src/main/java/command/Undo.java)
   </details>
   <details><summary>iterator</summary>

    - [ParkSlotGenerator](src/main/java/iterator/ParkSlotGenerator.java)
   </details>
   <details><summary>parkhaus</summary>

    - [Car](src/main/java/parkhaus/Car.java)
    - [CarPark](src/main/java/parkhaus/CarPark.java)
    - [CarParkServlet](src/main/java/parkhaus/CarParkServlet.java)
    - [ConfigHelper](src/main/java/parkhaus/ConfigHelper.java)
    - [JSON](src/main/java/parkhaus/JSON.java)
    - [Level](src/main/java/parkhaus/Level.java)
    - [ParkSlot](src/main/java/parkhaus/ParkSlot.java)
    - [Type](src/main/java/parkhaus/Type.java)
   </details>
   <details><summary>statistics</summary>

    - [AvgParkDuration](src/main/java/statistics/AvgParkDuration.java)
    - [ChangedType](src/main/java/statistics/ChangedType.java)
    - [ParkSlotCapacity](src/main/java/statistics/ParkSlotCapacity.java)
    - [SumFees](src/main/java/statistics/ChangedType.java)
   </details>
   <details><summary>template</summary>

    - [ChartTemplate](src/main/java/template/ChartTemplate.java)
    - [StatisticsTemplate](src/main/java/template/StatisticsTemplate.java)
   </details>

8. Patternverzeichnis
    - **Singleton Pattern**:
        - [CarPark](src/main/java/parkhaus/CarPark.java)
    - **Template Pattern**:
        - [StatisticsTemplate](src/main/java/template/StatisticsTemplate.java)
            - [AvgParkDuration](src/main/java/statistics/AvgParkDuration.java)
            - [ChangedType](src/main/java/statistics/ChangedType.java)
            - [ParkSlotCapacity](src/main/java/statistics/ParkSlotCapacity.java)
            - [SumFees](src/main/java/statistics/ChangedType.java)
    - **Multiton**:
        - [Type](src/main/java/parkhaus/Type.java)
    - **Iterator**:
        - [ParkSlotGenerator](src/main/java/iterator/ParkSlotGenerator.java)
    - **Observer (MVC)**:
        - **Controller**
            - [CarParkServlet](src/main/java/parkhaus/CarParkServlet.java)
        - **Model**
            - [CarPark](src/main/java/parkhaus/CarPark.java)
        - **View**
            - [CarParkSimulation.jsp](web/CarParkSimulation.jsp)
    - **Adapter**:
        - [JSON](src/main/java/parkhaus/JSON.java)
    - **Command und State**:
        - [CarParkCommands](src/main/java/command/CarParkCommands.java)
        - [EnterCar](src/main/java/command/EnterCar.java)
        - [EnterFamilyCar](src/main/java/command/EnterFamilyCar.java)
        - [EnterNormalCar](src/main/java/command/EnterNormalCar.java)
        - [EnterSpecialNeedsCar](src/main/java/command/EnterSpecialNeedsCar.java)
        - [LeaveCar](src/main/java/command/LeaveCar.java)
        - [Undo](src/main/java/command/Undo.java)
        - [CarParkServlet](src/main/java/parkhaus/CarParkServlet.java)
    - **Builder**:
        - Indirekt wurde [org.json](https://mvnrepository.com/artifact/org.json/json) als Builder benutzt.
            - [JSON](src/main/java/parkhaus/JSON.java)


9. [Sicherheitsmaßnahmen](wiki/Sicherheitsmaßnahmen.pdf)
10. [Zielkonflikte](wiki/Digitalisierungskonzept.pdf).

11. [Gesamt-Retrospektive](wiki/Gesamt_Retroperspektive.pdf)

### Videos
- [Demo-Video (YouTube)]()
- [Präsentationsvideo (YouTube)]()