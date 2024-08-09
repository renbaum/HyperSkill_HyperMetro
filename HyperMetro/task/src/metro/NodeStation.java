package metro;

import java.util.*;

class Edge{
    NodeStation to = null;
    String transitionLine = "";
    private boolean visited = false;
    int weight = 0;

    public Edge(NodeStation n, int weight){
        to = n;
        this.weight = weight;
    }

    public void setVisited(boolean v){
        visited = v;
    }

    public boolean isVisited(){
        return visited;
    }

    public void setTransitionLine(String t){
        transitionLine = t;
    }

    public String getTransitionLine(){
        return transitionLine;
    }
}

public class NodeStation{
    Station station = null;
    List<Edge> edges = new ArrayList<>();
    private boolean visited = false;
    int distance = 0;
    public NodeStation from = null;
    String stationTransfer = "";
    int time = 0;

    public NodeStation(Station station) {
        this.station = station;
    }

    public boolean addEdge(NodeStation n, int weight){
        addSingleEdge(n, weight);
        n.addEdge(this, weight);
        return true;
    }

    public List<Edge> getEdges(){
        List<Edge> dummy = new ArrayList<>();
        for(Edge e : edges){
            dummy.add(e);
        }
        return dummy;
    }

    private Edge findEdge(NodeStation n){
        for(Edge e: edges){
            if (e.to == n) return e;
        }
        return null;
    }

    public boolean removeEdge(NodeStation n){
        removeSingleEdge(n);
        n.removeSingleEdge(this);
        return true;
    }

    public boolean removeSingleEdge(NodeStation n){
        Edge e = findEdge(n);
        if(e != null) edges.remove(e);
        return true;
    }

    public Station getStation() {
        return station;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Edge> getConnections() {
        List<Edge> connections = new ArrayList<>();
        for(Edge e: edges){
            connections.add(e);
        }
        return connections;
    }

    public void setStationTransfer(String stationTransfer) {
        this.stationTransfer = stationTransfer;
    }

    public void printRoute() {
        if(!stationTransfer.isEmpty()) {
            System.out.printf("Transition to line %s\n", stationTransfer);
//            System.out.printf("%s\n", stationTransfer.station);
        }


        System.out.println(station.getName());
    }

    public Edge addSingleEdge(NodeStation node, int weight) {
        Edge edge = new Edge(node, weight);
        edges.add(edge);
        return edge;
    }

    public void printConnection() {
        System.out.printf("%s: [", station.getName());
        for(Edge e: edges){
            if(!e.getTransitionLine().isEmpty()){
                System.out.printf("Transition to line %s - Station ", e.getTransitionLine());
            }
            System.out.printf("%s, ", e.to.station.getName());
        }
        System.out.printf("]\n");
    }
}
