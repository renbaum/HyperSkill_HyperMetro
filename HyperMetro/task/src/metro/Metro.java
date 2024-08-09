package metro;

import com.google.gson.*;

import java.io.*;
import java.util.*;


public class Metro{
    Map<String, MetroLine> metroLine = new HashMap<String, MetroLine>();
    private static Metro instance = null;

    public Metro(){
        Metro.instance = this;
    }

    public static Metro getInstance(){
        return Metro.instance;
    }

    private MetroLine createNewMetroLine(String line){
        MetroLine m = new MetroLine(line);
        metroLine.put(line, m);
        return m;
    }

    public void createTreeNode(){
        clearTreeNode();
        for(MetroLine m : metroLine.values()){
            m.createNodeNetwork();
        }
    }

    public void clearTreeNode(){
        for(MetroLine m : metroLine.values()){
            m.clearTreeNodes();
        }
    }

    public void printTreeNode(){
        for(MetroLine m : metroLine.values()){
            m.printTreeNode();
        }
    }

    public boolean initializeFromJson(String filename){
        JsonParser jSonParser = new JsonParser();
        Gson gson = new Gson();

        try(FileReader reader = new FileReader(filename)){
            // parse the JSON file and convert JsonElement
            JsonElement jE = jSonParser.parse(reader);

            // check if the parsed element is a JsonObject
            if(jE.isJsonObject()){
                JsonObject jsonObject = jE.getAsJsonObject();

                // access data from JsonObject
                // get a map of all lines and extract the name of the line
                Map<String, JsonElement> metroMap = jsonObject.asMap();
                Set<String> keySet = metroMap.keySet();
                for(String metro : keySet){
                    MetroLine metroline = createNewMetroLine(metro);
                    // get all stations of the list
                    JsonArray array = jsonObject.get(metro).getAsJsonArray();
                    for(JsonElement o: array){
                        Station station = gson.fromJson(o, Station.class);
                        metroline.add(station);
                        station.checkTransfer();

                    }
                    metroline.setStationConnection();
/*                    JsonObject metroObject = metroMap.get(metro).getAsJsonObject();
                    Map<String, JsonElement> metroLineMap = metroObject.asMap();

                    // now add all the stations to the line
                    for(int i = 1; i <= metroLineMap.size(); i++){
                        JsonElement metroLineElement = metroLineMap.get(String.valueOf(i));
                        try{
                            Station station = gson.fromJson(metroLineElement, Station.class);
                            if(station == null) throw new JsonSyntaxException("station is null");
                            station.checkTransfer();
                            metroline.addLastStation(station);
                        }catch(JsonSyntaxException e){
                            DummyStation station = gson.fromJson(metroLineElement, DummyStation.class);
                            metroline.addLastStation(station);
                        }
                    }*/
                }
            }
            return true;
        }catch (FileNotFoundException e) {
            System.out.println("Error! Such a file doesn't exist!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean initializeFromFile(String filename){
        try {
            MetroLine m = new MetroLine("line");

            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                m.add(line, 0);
            }
            br.close();
            fr.close();

            metroLine.put(m.getName(), m);
            return true;

        }catch(IOException e){
            System.out.println("Error! Such a file doesn't exist!");
        }
        return false;
    }

    public static Station getStation(String line, String stationName){
        MetroLine m = instance.metroLine.get(line);
        if(m == null){
            System.out.println("Error! Such line doesn't exist!");
            return null;
        }
        return m.getStation(stationName);
    }

    /*
    private String getPrevStationName(Station station){
        if(station.getPrevStation() == null) return "depot";
        return station.getPrevStation().getName();
    }

    private String getNextStationName(Station station){
        if(station.getNextStation() == null) return "depot";
        return station.getNextStation().getName();
    }

     */

    public void printLine(String line) {
        MetroLine m = metroLine.get(line);
        if (m != null) {
            m.printLine();
        } else {
            System.out.println("Line not found!");
        }
    }

    public void addFirst(String line, String station, String stime){
        int time = Integer.parseInt(stime);
        MetroLine metro = metroLine.get(line);
        if (metro == null) {
            return;
        }
        metro.add(station, time);
    }

    public void addLast(String line, String station, String stime){
        int time = Integer.parseInt(stime);
        MetroLine metro = metroLine.get(line);
        if (metro == null) {
            return;
        }
        metro.add(station, time);
    }

    public void delete(String line, String station){
        MetroLine metro = metroLine.get(line);
        if (metro == null) {
            return;
        }
        metro.deleteStation(station);
    }

    public void connect(String line1, String station1, String line2, String station2) {
        MetroLine metro1 = metroLine.get(line1);
        MetroLine metro2 = metroLine.get(line2);
        if(metro1 == null || metro2 == null){
            System.out.println("Error! Such metro line doesn't exist!");
            return;
        }

        Station stationObj1 = metro1.stationMap.get(station1);
        Station stationObj2 = metro2.stationMap.get(station2);
        if(stationObj1 == null || stationObj2 == null){
            System.out.println("Error! Such a station doesn't exist!");
            return;
        }
        stationObj1.addTransfer(metro2, stationObj2);
        stationObj2.addTransfer(metro1, stationObj1);
    }

    public void fastestRoute(String lineSource, String stationSource, String lineTarget, String stationTarget) {
        createDijkstra(lineSource, stationSource);

        NodeStation targetNode = getNodeStation(lineTarget, stationTarget);
        if(targetNode == null){return;}

        List<NodeStation> lstRoute = new ArrayList<>();

        lstRoute.add(targetNode);
        NodeStation currentNode = targetNode;
        while (currentNode.from != null) {
            lstRoute.add(currentNode.from);
            currentNode = currentNode.from;
        }
        Collections.reverse(lstRoute);
        for(NodeStation nodeStation : lstRoute){
            nodeStation.printRoute();
        }
        System.out.printf("Total: %d minutes in the way", targetNode.time);

    }

    public void route(String lineSource, String stationSource, String lineTarget, String stationTarget) {

        createBFS(lineSource, stationSource);
        //printTreeNode();

        NodeStation targetNode = getNodeStation(lineTarget, stationTarget);
        if(targetNode == null){return;}

        List<NodeStation> lstRoute = new ArrayList<>();

        lstRoute.add(targetNode);
        NodeStation currentNode = targetNode;
        while (currentNode.from != null) {
            lstRoute.add(currentNode.from);
            currentNode = currentNode.from;
        }
        Collections.reverse(lstRoute);
        for(NodeStation nodeStation : lstRoute){
            nodeStation.printRoute();
        }

    }

    private NodeStation getNodeStation(String line, String stationName){
        Station s = getStation(line, stationName);
        if(s == null){
            System.out.println("No stations found!");
            return null;
        }
        NodeStation sourceNode = s.getNodeStation();
        return sourceNode;
    }

    public void createDijkstra(String lineSource, String stationSource) {
        PriorityQueue<NodeStation> pq = new PriorityQueue<>(new Comparator<NodeStation>() {
                public int compare(NodeStation n1, NodeStation n2) {
                    return Integer.compare(n1.time, n2.time);
                }
            });

        createTreeNode();

        NodeStation sourceNode = getNodeStation(lineSource, stationSource);
        if(sourceNode == null){return;}

        sourceNode.time = 0;
        pq.add(sourceNode);
        while(!pq.isEmpty()){
            NodeStation current = pq.poll();
            List<Edge> edges = current.getEdges();
            for (Edge edge : edges) {
                NodeStation neighbor = edge.to;
                if(neighbor.isVisited() && current.from != neighbor){
                    current.removeEdge(neighbor);
                    continue;
                }
                int weight = edge.weight;
                int distance = current.time + weight;
                if (distance < neighbor.time) {
                    pq.remove(neighbor);
                    neighbor.time = distance;
                    neighbor.from = current;
                    neighbor.setStationTransfer(edge.transitionLine);
                    pq.add(neighbor);
                }
            }
            current.setVisited(true);
        }

    }

    public void createBFS(String line, String station){
        Deque<NodeStation> deque = new LinkedList<>();
        createTreeNode();

        NodeStation sourceNode = getNodeStation(line, station);
        if(sourceNode == null){return;}

        deque.add(sourceNode);
        while(!deque.isEmpty()) {
            NodeStation node = deque.pop();
            node.setVisited(true);
            List<Edge> lst = node.getConnections();
            for(Edge e : lst){
                NodeStation n = e.to;
                if (!n.isVisited()) {
                    n.setVisited(true);
                    n.setDistance(node.getDistance() + 1);
                    n.from = node;
                    n.setStationTransfer(e.transitionLine);
                    deque.add(n);
                }else{
                    if(node.from != n) {
                        node.removeSingleEdge(n);
                    }
                }
            }
        }
    }

}
