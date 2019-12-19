import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class MoveCalculator2 {

	int [] firstMoveWins = null;
	ArrayList<Move> firstMoves;
	public int [][][] winningArray = null;

	private boolean win = false;


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


		for(int x = 0; x< firstMoves.size(); x++){

			int [][] adj = makeMove(adjMatrix,firstMoves.get(x));

			if(wonState(adj, false)){
				if(!isKnownWinningPosition(adjMatrix)){

					addWinningPosition(adjMatrix);
					//addIsomorphicWinningStates(adjMatrix);
				}
				System.out.println("I will win no matter what");
				win = true;
				return firstMoves.get(x);

			}
		}
		win = false;
		System.out.println("I lose on perfect play");
		//System.out.println(firstMoves.size());

		return getPassiveNextMove(adjMatrix);
	}

	public boolean wonState(int [][] matrix, boolean myTurn)
	{

		//		if(countOnes(matrix) %2 == 0 && matrix.length%2 == 0){
		//
		//			return !myTurn;
		//		}
//		if(matrix.length == 0){
//
//			return !myTurn;
//		}

		//		if(countOnes(matrix) == 1){
		//
		//			return myTurn;
		//		}

		if(countOnes(matrix) %2 == 1 || matrix.length%2== 1){
			return myTurn;
		}
		else{
			return !myTurn;
		}


		//		
		//
		//		if(isKnownWinningPosition(matrix)){
		//
		//			return myTurn;
		//		}


		//		if(oddNumberOfWinStates(matrix)){
		//
		//			return myTurn;
		//		}
		//				if(isWinningStateIso(matrix,0)){
		//					return myTurn;
		//				}


//		ArrayList<Move> moves = generateMoveList(matrix);
//		//System.out.println("Moves "+moves.size());
//		int [][][] arrayOfArrays = new int [moves.size()][][];
//		for(int y = 0; y< moves.size(); y++){
//			int [][] mat = makeMove(matrix, moves.get(y));
//			arrayOfArrays[y] = mat;
//		}
//
//		if(myTurn){
//			return helperMethod1(arrayOfArrays, myTurn);
//
//		}
//		else{
//			return helperMethod2(arrayOfArrays, myTurn);
//		}
	}

	public boolean helperMethod1(int [][][] arrayOfArrays, boolean myTurn){
		int arrayLength = arrayOfArrays.length;
		boolean val = wonState(arrayOfArrays[0], !myTurn);

		for(int x = 0; x< arrayLength; x++){
			boolean b = wonState(arrayOfArrays[x] , !myTurn);
			val = val || b;

		}

		return val;
	}

	public boolean helperMethod2(int [][][] arrayOfArrays, boolean myTurn){
		int arrayLength = arrayOfArrays.length;
		boolean val = wonState(arrayOfArrays[0], !myTurn);

		for(int x = 0; x< arrayLength; x++){
			boolean b = wonState(arrayOfArrays[x] , !myTurn);
			val = val && b;
			if(b&&!isKnownWinningPosition(arrayOfArrays[x])){
				//System.out.println("Adding: ");
				//printState(arrayOfArrays[x]);
				addWinningPosition(arrayOfArrays[x]);
				//addIsomorphicWinningStates(arrayOfArrays[x]);
			}
		}

		return val;
	}


	//Returning false is inconclusive
	public boolean oddNumberOfWinStates(int [][] matrix){

		ArrayList<Integer> indexQueue = new ArrayList<Integer>();
		ArrayList<Integer> lookList = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> funTime = new ArrayList<ArrayList<Integer>>(); 
		for(int x = 0; x < matrix.length; x++){
			lookList.add(x);
		}

		//printState(matrix);

		int funtimeIndex = 0;
		while(lookList.size()>0){

			funTime.add(new ArrayList<Integer>());

			int next = lookList.remove(0);

			indexQueue.add(next);

			int numVertices = 0;

			ArrayList<Integer> hashPiece = new ArrayList<Integer>();

			while(indexQueue.size() > 0){
				int searchVal = indexQueue.remove(0);
				if(!hashPiece.contains(searchVal)){
					hashPiece.add(searchVal);
				}
				numVertices ++;



				for(int x = 0; x < matrix.length; x ++){
					if(matrix[searchVal][x] == 1 && lookList.contains(x)){
						indexQueue.add(x);
						//funTime.get(funtimeIndex).add(x);
						if(!hashPiece.contains(x)){
							hashPiece.add(x);
						}

						int remove = lookList.indexOf(x);
						lookList.remove(remove);


						int valA = hashPiece.indexOf(searchVal);
						int valB = hashPiece.indexOf(x);
						funTime.get(funtimeIndex).add(valA);
						funTime.get(funtimeIndex).add(valB);

						funTime.get(funtimeIndex).add(valB);
						funTime.get(funtimeIndex).add(valA);

						//						for(int s : funTime.get(funtimeIndex)){
						//							System.out.print(s+" ");
						//						}
						//						System.out.println();
					}
				}

			}

			funTime.get(funtimeIndex).add(numVertices);
			funtimeIndex++;



		}
		//System.out.println("Number of pieces: "+funTime.size());
		if(funTime.size()%2 == 0){
			//System.out.println("WE LOSE "+funTime.size());
			return false;
		}



		int numWins = 0;
		for(ArrayList<Integer> al:funTime){
			if(al.size() == 1){
				numWins ++;

			}
			else{


				int size = al.remove(al.size()-1);
				int adjMatrix [][] = new int [size][size];

				for(int x = 0; x < al.size(); x+=2){
					int row = al.get(x);
					int col = al.get(x + 1);
					//System.out.println(adjMatrix.length+" "+al.size()+ " "+row+" "+col);
					adjMatrix[row][col] = 1;
				}

				if(adjMatrix.length == matrix.length){
					return false;
				}

				else if(wonState(adjMatrix, true)){
					numWins ++;
				}
			}

		}
		if(numWins == funTime.size()){
			//System.out.println("WE WIN");
			return true;

		}
		System.out.println(numWins +" "+funTime.size());
		return false;
	}

	public void buildWinningArray() throws IOException{
		String str = "";
		String curLine = "";

		FileReader fr = new FileReader("win.txt");
		BufferedReader br = new BufferedReader(fr);

		while ((curLine = br.readLine()) != null) {
			str+=curLine;
		}
		str.trim();
		String [] m = str.split("-");



		int mostVert = Integer.parseInt(m[m.length-1].split("I")[0]);

		winningArray = new int[mostVert + 1][][];
		for(int x = 0; x < mostVert; x++){
			String [] thisMatrix = m[x].split("I");
			winningArray[x + 1] = new int[thisMatrix.length - 1][];

			for(int y = 1; y< thisMatrix.length; y++){ //skip the first number
				String [] adjMatrix = thisMatrix[y].split(",");
				winningArray[x + 1][y - 1] = new int[adjMatrix.length];
				for(int z = 0; z < adjMatrix.length; z++){
					winningArray[x + 1][y - 1][z] = Integer.parseInt(adjMatrix[z]);
				}
			}
		}
	}

	public boolean isKnownWinningPosition(int [][] matrix){
		int vertices = matrix.length;
		int [] temp = new int[vertices];		//Will temporarily store the integer representations of the adjacency matrix

		for(int x = 0; x < vertices; x++){
			temp[x] = getIntFromArray(matrix[x]);
		}

		if(winningArray!= null && vertices < winningArray.length && winningArray[vertices]!=null){
			for(int x = 0; x < winningArray[vertices].length; x++){

				if(equals(winningArray[vertices][x],temp)){
					return true;
				}
			}
		}

		return false;
	}

	public boolean equals(int [] a, int []b){
		if(a == null || b == null | a.length != b.length){
			return false;
		}
		for(int x = 0; x<a.length; x++){
			if(a[x] != b[x]){
				return false;
			}
		}
		return true;
	}

	public void writeToFile() throws IOException{

		FileWriter fw = new FileWriter("win.txt");


		for(int x = 1; x < winningArray.length; x++){

			if(x!=1){

				fw.write("-");
			}
			fw.write(String.valueOf(x));

			if(winningArray[x]!= null){
				for(int y = 0; y< winningArray[x].length; y++){
					fw.write("I");

					for(int z = 0; z < winningArray[x][y].length; z++){

						fw.write(String.valueOf(winningArray[x][y][z]));

						if(z!=winningArray[x][y].length-1){

							fw.write(",");
						}
					}
				}
			}

		}
		fw.close();

	}

	public void addWinningPosition(int [][] matrix){

		if(matrix.length == 0)
			return;


		int [] myArr = new int [matrix.length];

		for(int x = 0; x < myArr.length; x++){
			myArr[x] = getIntFromArray(matrix[x]);
		}


		int vertices = matrix.length;
		//int ln = winningArray[vertices].length;
		if(winningArray!=null){

			if(vertices < winningArray.length && winningArray[vertices]!=null){
				int [][] temp = Arrays.copyOf(winningArray[vertices], winningArray[vertices].length + 1);
				temp[temp.length - 1] = Arrays.copyOf(myArr, myArr.length);
				winningArray[vertices] = Arrays.copyOf(temp, temp.length);


			}
			else if(vertices >= winningArray.length){

				int [][][] temp = Arrays.copyOf(winningArray, vertices + 1);
				temp[vertices] = new int [1][];
				winningArray = Arrays.copyOf(temp, temp.length);
				winningArray[vertices][0] = Arrays.copyOf(myArr, myArr.length);


			}
		}
		else{
			System.out.println("winning Array is null");

		}
	}




	public void printArray(){
		for(int x = 1; x < winningArray.length; x++){
			System.out.println(x+": ");
			for(int y = 0; y < winningArray[x].length; y++){

				for(int z = 0; z < winningArray[x][y].length ; z++){
					System.out.print(winningArray[x][y][z] + " ");

				}
			}
			System.out.println();
		}
	}

	public int getIntFromArray(int [] myArr){
		String s = "";
		for(int x = 0; x < myArr.length; x++){
			s+= myArr[x];
		}
		int ans = Integer.parseInt(s,2);
		return ans;
	}


	public void printState(int [][] matrix){
		for(int x = 0; x < matrix.length; x++){
			for(int y = 0; y < matrix.length; y++){
				System.out.print(matrix[x][y]+" ");
			}
			System.out.println();
		}
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

	public int countOnes(int [][] matrix){
		int count = 0;
		for(int x = 0; x < matrix.length; x++){
			for(int y = x; y < matrix.length; y++){
				if(matrix[x][y] == 1)count ++;
			}
		}
		return count;
	}

	public int[][] makeMove(int [][] matrix, Move m){
		if(m.getType().equals("vertex")){
			int index = m.getIndex();

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

	public boolean isWinningStateIso(int [][] matrix, int startIndex){
		int size = matrix.length;
		boolean b = false;

		if(isKnownWinningPosition(matrix)){
			return true;
		}
		if(startIndex == matrix.length - 1)
		{
			return false;
		}


		for(int x = startIndex; x < matrix.length; x ++){
			for(int x2 = x; x2 < matrix.length; x2++){

				if(x!=x2){
					int [][] m = new int [size][size];
					for(int z = 0; z < matrix.length; z ++){
						for(int a = 0; a < matrix.length; a++){
							m[z][a] = matrix[z][a];
						}
					}
					for(int y = 0; y < size; y++){
						int temp = m[x][y];
						m[x][y] = m [x2][y];
						m[x2][y] = temp;
					}
					for(int y = 0; y < size; y++){
						int temp = m[y][x];
						m[y][x] = m [y][x2];
						m[y][x2] = temp;
					}
					b = b || isWinningStateIso(m, ++startIndex);
				}
			}
		}
		return b;

	}


	public void addIsomorphicWinningStates(int [][] matrix){
		int size = matrix.length;


		for(int x = 0; x < matrix.length; x++){
			for(int x2 = 0; x2 < matrix.length; x2++){
				if(x!=x2){
					int [][] m = new int [size][size];
					for(int z = 0; z < matrix.length; z ++){
						for(int a = 0; a < matrix.length; a++){
							m[z][a] = matrix[z][a];
						}
					}
					for(int y = 0; y < size; y++){
						int temp = m[x][y];
						m[x][y] = m [x2][y];
						m[x2][y] = temp;
					}
					for(int y = 0; y < size; y++){
						int temp = m[y][x];
						m[y][x] = m [y][x2];
						m[y][x2] = temp;
					}
					//					printState(m);
					//					System.out.println();
					if(!isKnownWinningPosition(m)){
						addWinningPosition(m);
						//addIsomorphicWinningStates(m);
					}
					else{

					}
				}
			}
		}

	}

	public Move getPassiveNextMove(int [][] matrix){
		int minDegree = 9999;
		int minIndex = -1;

		if(countOnes(matrix) > 4){
			for(int x = 0; x< matrix.length;x++){
				for(int y =0; y<matrix.length; y++){
					if(matrix[x][y] == 1){
						Move m = new Move(x,y);
						return m;
					}
				}
			}
		}

		for(int x = 0; x< matrix.length; x++){
			int curDegree = 0;
			for(int y = 0; y < matrix.length; y++){
				if(matrix[x][y] == 1){
					curDegree ++;
				}
			}
			if(minDegree > curDegree){
				minDegree = curDegree;
				minIndex = x;
			}


		}
		//figure out why a passive move is not offered for 4 vertices {2,2,1,1}
		return new Move(minIndex);


	}
	public boolean win(){
		return win;

	}



}