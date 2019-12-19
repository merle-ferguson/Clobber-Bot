import java.awt.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;



class Main {

	static ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	static ArrayList<Edge> edges = new ArrayList<Edge>();


	public static void main(String[] args) throws IOException {
		
								Screen frame = new Screen();
						
								frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						
								frame.setVisible(true);


//		//		for(int x = 8; x < 15; x ++){
//		//			for(int z = 3; z < x*(x-1)/4; z ++){
//		//		for(int x = 5; x < 15; x ++){
//		//			for(int z = 3; z < x*(x-1)/4; z ++){
//		int x = 7;
//		int z = 7;
//
//		int wins = 0;
//		int losses = 0;
//		for(int y= 0; y < 1000; y ++){
//			vertices = new ArrayList<Vertex>();
//			edges = new ArrayList<Edge>();
//
//			MoveCalculator2 mc = new MoveCalculator2();
//			mc.buildWinningArray();
//			makeGraph(x,z);
//			//makeGraph(8,12);
//			mc.makeNextMove(edges, vertices);
//			if(mc.win()){
//				wins++;
//
//				//System.out.println(wins);
//			}
//			else{
//				losses ++;
//				if(vertices.size()%2 == 1 || edges.size()%2 == 1){
//					int [][] adjMatrix = new int [vertices.size()][vertices.size()];
//					for(Edge e: edges){
//						int index1 = vertices.indexOf(e.getV1());
//						int index2 = vertices.indexOf(e.getV2());
//						adjMatrix[index1][index2] = 1;
//						adjMatrix[index2][index1] = 1;
//					}
//					mc.printState(adjMatrix);
//					System.out.println();
//				}
//			}
//			//mc.writeToFile();
//			System.out.println("Edges: "+edges.size()+" Vertices: "+vertices.size()+" Percent win: "+(double)wins/(double)(losses+ wins)*100.0);
//		}

	
	}


	//
	//}
	//}


	public static void makeGraph(int numVertices, int numEdges){
		Random gen = new Random();


		//int numEdges = gen.nextInt(numVertices *(numVertices - 1)/6);

		//Adds vertices to random locations
		for(int x = 0; x < numVertices; x++){	
			Vertex v = new Vertex();
			vertices.add(v);
		}

		ArrayList<Vertex> color1 = new ArrayList<Vertex>();
		ArrayList<Vertex> color2 = new ArrayList<Vertex>();

		for(int x = 0; x < vertices.size(); x++){
			if(x >= vertices.size()/2)
				color1.add(vertices.get(x));
			else
				color2.add(vertices.get(x));
		}


		for(int x = 0; x < numEdges; x++){
			int a = gen.nextInt(color1.size());
			int b = gen.nextInt(color2.size());
			while(null !=findEdgeGivenVertices(color1.get(a),color2.get(b))){
				a = gen.nextInt(color1.size());
				b = gen.nextInt(color2.size());
			}

			edges.add(new Edge(color1.get(a),color2.get(b)));
		}




	}
	public static Edge findEdgeGivenVertices(Vertex a, Vertex b){
		for(Edge e: edges){
			if((e.getV1() == a && e.getV2() == b) || (e.getV2() ==a && e.getV1() == b))
			{
				return e;
			}
		}
		return null;

	}
}