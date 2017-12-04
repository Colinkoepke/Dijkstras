import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Graph{
	
	//create variable to hold edge
	//create map contain City Name as string and List of edges as value
	List<Edge> e;
	Map<String, List<Edge>> g = new LinkedHashMap<>();
	List<String> vertices;
	List<String> previous;
	List<Integer> distance;

		
	
	/*
	 * Edge class
	 * Each edge contains a weight w, connected city v, & boolean to see if edge is visited
	 * Once a new edge is created, the edge is added to the edge list for each city
	 */
	class Edge{
		int w;
		String v;
		boolean visited = false;
		
		public Edge(int w, String v){
			this.w = w;
			this.v = v;
			e.add(this);
		}

		public int getW() { return w; }

		public void setW(int w) { this.w = w; }

		public String getV() { return v; }

		public void setV(String v) { this.v = v; }
		
		public boolean getVisited() { return visited; }
		
		public void setVisited(boolean visited) { this.visited = visited; }
	}
		
	//Method to read graph from file and make corresponding clusters
	
	public void readFromFile(){
		
		try{
			//get input file and create input stream
		    FileInputStream fstream = new FileInputStream("graph.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		          
		    /*
		     * read each line from the file and place into String array for each token
		     * if the first index of each array is CLUSTER then that means the cluster is over and subgraph should be added to cluster list
		     * if the first index is not CLUSTER, then place odd indices as weights and even indices as city names
		     */
		    while ((strLine = br.readLine()) != null)   {
		    	String[] tokens = strLine.split(" "); 
		        	e = new ArrayList<>();
			        for(int i = 1; i < tokens.length; i++){
			        	int weightFromFile = Integer.parseInt(tokens[i]);
			        	Edge e1 = new Edge(weightFromFile, tokens[i+1]);
			        	i++;
			        }
			        g.put(tokens[0], e);
		        }
		   
		    in.close();
			}catch (Exception e){
				
			}
	}
		
	/**
	 * @param endpt
	 */
	void dijkstra(String endpt){
		
		//distance
		vertices = new ArrayList<>(g.keySet());
		previous = new ArrayList<>();
		distance = new ArrayList<>();
		
		boolean end = false;
		
		
		//initialize graph
		for(int i=0; i < vertices.size(); i++){
			distance.add(Integer.MAX_VALUE);
			previous.add("");
		}
		//set first index ditance to 0
		distance.set(0, 0);
		
		int counter = 0;
		String smallest = "";
		int indexToGet = -1;
		List<Edge> edgeList;
		int toAdd = 0;
		
		while(!end){
			
			
			
			smallest = findMinCurrent(); //finds node with the smallest dist from current
			
			if (smallest.equals(endpt)){
				end = true;
				break;
			}
			int indexOfSmallest = vertices.indexOf(smallest);
			int smallestDistance = distance.get(indexOfSmallest);
			vertices.remove(indexOfSmallest); // remove the smallest node from list of vertices
			
			
			toAdd = 0;

			edgeList = g.get(smallest);
			distance.remove(indexOfSmallest);
			for(int i=0; i < edgeList.size(); i++){
				toAdd = smallestDistance + edgeList.get(i).w;
				
				String vertexAt = edgeList.get(i).v;
				if(vertices.contains(vertexAt))	
					indexToGet = vertices.indexOf(vertexAt);
				else{
					break;
				}
				if(toAdd < distance.get(indexToGet)){
					distance.set(indexToGet, toAdd);
					previous.set(counter, smallest);
				}
				
			}
			
			counter++;
			
		}
		System.out.println("total cost" + toAdd);
		for (int i = 0; i < previous.size(); i++){
			System.out.println(previous.get(i));
		}
	}
	
	String findMinCurrent(){
		String city; 
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int i = 0; i < distance.size(); i++){
			if(distance.get(i) < min){
				min = distance.get(i);
				minIndex = i;
			}
		}
		city = vertices.get(minIndex);
		return city;
	}
	
	
}

public class DijkstraTSP{
	public static void main(String[] args){
		Graph g = new Graph();
		g.readFromFile();
		g.dijkstra("Boise");
		
	}
}