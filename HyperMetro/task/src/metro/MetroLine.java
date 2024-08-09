package metro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetroLine{
    private String name;
    Station firstStation = null;
    Station lastStation = null;
    Map<String, Station> stationMap = new HashMap<String, Station>();

    public MetroLine(String name){
        this.name = name;
    }

    public void setStationConnection(){
        try {
            for (Station station : stationMap.values()) {
                if(station.nextStation == null){
                    station.nextStation = new ArrayList<>();
                }
                if(station.prevStation == null){
                    station.prevStation = new ArrayList<>();
                }
                for (String prev : station.prev) {
                    Station prevStation = stationMap.get(prev);
                    station.prevStation.add(prevStation);
                }
                for (String next : station.next) {
                    Station nextStation = stationMap.get(next);
                    station.nextStation.add(nextStation);
                }
            }
        }catch (Exception e){
            System.out.println("Fehler beim Speichern der MetroLine");
        }
    }

    public void add(Station station) {
        stationMap.put(station.getName(), station);
    }

    public void add(String name, int time){
        Station station = new Station(name, time);
        add(station);
    }

    public void addLastStation(DummyStation dummystation){
        if(dummystation == null) return;

        Station station = new Station(dummystation);
        add(station);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return stationMap.size();
    }

    public void deleteStation(String name){
        Station stationToDelete = stationMap.get(name);
        if (stationToDelete == null) return;

/*        if (stationToDelete == firstStation) firstStation = stationToDelete.getNextStation();
        if (stationToDelete == lastStation) lastStation = stationToDelete.getPrevStation();


        stationToDelete.delete();

 */
        stationMap.remove(name);
    }

    public void printLine() {
        System.out.println("Not going to work");
/*        Station currentStation = firstStation;
        if(getSize() == 0) return;

        System.out.println("depot");
        while (currentStation != null) {
            currentStation.print();
            currentStation = currentStation.getNextStation();
        }
        System.out.println("depot");*/
    }

    public Station getStation(String stationName) {
        return stationMap.get(stationName);
    }

    public void createNodeNetwork() {
        for(Station station : stationMap.values()){
            station.createNodeNetwork();
        }
    }

    public void printTreeNode() {
        for(Station station : stationMap.values()){
            station.getNodeStation().printConnection();
        }
    }

    public void clearTreeNodes() {
        for(Station station : stationMap.values()){
            station.clearNode();
        }
    }

}
