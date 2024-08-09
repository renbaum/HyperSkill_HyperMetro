package metro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class StationTransfer{
    String line;
    String station;

    public StationTransfer(String line, String station){
        this.line = line;
        this.station = station;
    }
}

class DummyStation{
    String name = null;
    StationTransfer transfer = null;
    int time = 0;
}

public class Station{
    transient List<Station> nextStation = null;
    transient List<Station> prevStation = null;
    transient private NodeStation node = null;

    private String name = null;
    List<StationTransfer> transfer = new ArrayList<StationTransfer>();
    int time = 0;
    List<String> next = new ArrayList<>();
    List<String> prev = new ArrayList<>();

    public Station(String name, int time){
        this.name = name;
        this.time = time;
        nextStation = new ArrayList<>();
        prevStation = new ArrayList<>();
    }

    public void checkTransfer(){
        if(transfer == null){
            transfer = new ArrayList<StationTransfer>();
        }
    }

    public Station(DummyStation dummy){
        this.name = dummy.name;
        transfer.add(dummy.transfer);
        time = dummy.time;
    }


    public String getName() {
        return name;
    }

    public void print() {
        String strTransfer = "";

        for (StationTransfer transfer : transfer) {
            strTransfer += String.format(" - %s (%s line)", transfer.station, transfer.line);
        }
        System.out.printf("%s%s\n", name, strTransfer);
    }

    public void addTransfer(MetroLine metro, Station station) {
        StationTransfer tr = new StationTransfer(metro.getName(), station.getName());
        transfer.add(tr);
    }

    public NodeStation getNodeStation() {
        if(node == null){
            node = new NodeStation(this);
        }
        return node;
    }

    public void createNodeNetwork(){
        NodeStation node = getNodeStation();
        node.time = Integer.MAX_VALUE;
        for(StationTransfer tr : transfer){
            NodeStation transferNode = Metro.getInstance().getStation(tr.line, tr.station).getNodeStation();
            Edge edge = node.addSingleEdge(transferNode, 5);
            edge.setTransitionLine(tr.line);
        }
        for(Station station : nextStation){
            node.addSingleEdge(station.getNodeStation(), this.time);
        }
        for(Station station : prevStation){
            node.addSingleEdge(station.getNodeStation(), station.time);
        }
    }

    public void clearNode() {
        node = null;
    }
}
