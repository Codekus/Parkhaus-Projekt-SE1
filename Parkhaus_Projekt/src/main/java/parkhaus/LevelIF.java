package parkhaus;

public interface LevelIF {

    //Zuweisung eines Autos auf diesem Level und Rückgabe der Parkplatz ID
    boolean entry(Car car, int id);
    //Verlassen eines Autos
    boolean leave(int pos);
    //Name das Levels
    String getName();
    int getFamCap();
    int getNormCap();
    int getSpnCap();
    JSON toJson();

}
