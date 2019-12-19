import java.util.ArrayList;
import java.util.Random;


public class MoveCalculator {

	int [] firstMoveWins = null;
	ArrayList<Move> firstMoves;
	Random gen = new Random();
	MoveCalculator2 mc2 = null;
	public MoveCalculator(MoveCalculator2 m){
		mc2 = m;
	}
	public Move makeNextMove(ArrayList<Edge> edges, ArrayList<Vertex> vertices){
		int [][] adjMatrix = new int [vertices.size()][vertices.size()];
		for(Edge e: edges){
			int index1 = vertices.indexOf(e.getV1());
			int index2 = vertices.indexOf(e.getV2());
			adjMatrix[index1][index2] = 1;
			adjMatrix[index2][index1] = 1;
		}

		firstMoves = generateMoveList(adjMatrix);
		firstMoveWins = new int [firstMoves.size()];
		//System.out.println(firstMoves.size());

		for(int x = 0; x< firstMoves.size(); x++){
			for(int y = 0; y< 100; y++){
				
				int [][] adj = makeMove(adjMatrix,firstMoves.get(x));
				if(playGame(adj, false)){
					firstMoveWins[x]++;
				}
			}
		}
		
//		for(int x = 0; x < firstMoveWins.length; x++){
//			System.out.print(firstMoveWins[x] + " ");
//		}
//		System.out.println();
		
		int max = -1;
		int maxIndex = -1;
		for(int x = 0; x< firstMoveWins.length; x++){
			if(firstMoveWins[x] > max){
				max = firstMoveWins[x];
				maxIndex = x;
			}
		}
		return firstMoves.get(maxIndex);
	}

	public boolean playGame(int [][] matrix, boolean myTurn){

		if(matrix.length == 0){
			return !myTurn;
		}
		if(countOnes(matrix) == 1){
			return myTurn;
		}
		if(countOnes(matrix) == 2 && matrix.length%2 == 0){
			return !myTurn;
		}
		if(matrix.length + countOnes(matrix)/2 < 12){
			//System.out.println("This is called a lot");
			
			return(mc2.wonState(matrix, myTurn));
		}
		

		Move nextMove = pickRandomMove(generateMoveList(matrix));
		int [][] m = makeMove(matrix, nextMove);

		return playGame(m, !myTurn);
	}

	public ArrayList<Move> generateMoveList(int [][] matrix){
		ArrayList<Move> temp = new ArrayList<Move>();
		for(int x = 0; x < matrix.length; x++){
			Move m = new Move(x);
			temp.add(m);
		}

		for(int x = 0; x< matrix.length; x++){
			for(int y = x; y< matrix.length; y++){
				if(matrix[x][y] == 1){
					Move m = new Move(x,y);
					temp.add(m);
				}
			}
		}
		return temp;
	}

	public Move pickRandomMove(ArrayList<Move> moves){
		int index = gen.nextInt(moves.size());
		return moves.get(index);

	}
	
	public int countOnes(int [][] matrix){
		int count = 0;
		for(int x = 0; x < matrix.length; x++){
			for(int y = x; y < matrix.length; y++){
				count ++;
			}
		}
		return count;
	}

	public int[][] makeMove(int [][] matrix, Move m){
		if(m.getType().equals("vertex")){
			int index = m.getIndex();
			//System.out.println(index);
			int [][] newMatrix = new int [matrix.length-1][matrix.length - 1];

			int a = 0;
			int b = 0;
			for(int x = 0; x< matrix.length; x++){
				for(int y = 0; y< matrix.length; y++){
					if(x!=index && y!= index){
						//System.out.println(b + " " + a +" "+y+" "+x);
						newMatrix[a][b] = matrix[x][y];
						b++;
						if(b == newMatrix.length){
							b= 0;
							a++;
						}
					}
					else{

					}
				}
			}
			return newMatrix;

		}
		else{

			int v1 = m.getV1Index();
			int v2 = m.getV2Index();	
			
			int [][] newMatrix = new int [matrix.length][matrix.length];
			
			for(int x = 0; x< newMatrix.length;x++){
				for(int y =0; y<newMatrix.length; y++){
					newMatrix[x][y] = matrix[x][y];
				}
			}
			
			newMatrix[v1][v2] = 0;
			newMatrix[v2][v1] = 0;
			return newMatrix;
		}



	}


}