/**
 * 
 */
package assignment2;
import java.io.*;
import java.util.*;


import java.util.Arrays;
import java.util.Random;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
/**
 * @author Chandrashekar Akkenapally
 *
 */
//The RowColumn class is created in order store the row column dimensions of penguin positions or puzzle size. It is also used to pass 
//the row and column values to different functions.
class RowColumn{
	public int row=0;
	public int col=0;
	   String line;	
	public int getRow() {
		 
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}	
}

//The Puzzle is class which is created to store the puzzle and its size parameters. The Puzzle class object is also used to pass
//as a method variable under different circumstances.
class Puzzle{
	int row,col;
	char [][] ch;
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public char[][] getCh() {
		return ch;
	}
	public void setCh(char[][] ch) {
		this.ch = ch;
	}
}
public class Assignment2 {
	/**
	 * @param args
	 */
	//This method is used to create puzzle by providing the row and column size dynamically.
	public static char[][] createPuzzle(RowColumn puzzlesize){
		char[][] puz= new char[puzzlesize.getRow()][puzzlesize.getCol()];
		return puz;
	}
	
	//This function helps us to read the puzzle data from the input text file.
public static Puzzle readPuzzle(String input){
		
		int count=1;
		Puzzle puzzle=new Puzzle();
		RowColumn puzzlesize= new RowColumn();
		Scanner sc = null;
        try {
        	
            File file = new File(input); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner 
            String line;
            line=sc.nextLine();  //We are trying to fetch and read the first line from the input file
            String split[] = line.split(" ");//We are splitting the first line into two parts and converting
            puzzlesize.setRow(Integer.parseInt(split[0]));//This gives the row value
            puzzlesize.setCol(Integer.parseInt(split[1])); //This gives the column value
            puzzle.setRow(puzzlesize.getRow());
            puzzle.setCol(puzzlesize.getCol());
              char[][] temp_puz=createPuzzle(puzzlesize);//Creating an dynamic character array with above dimension provided in input file
            int j=0;
            while (sc.hasNextLine()) {  //We are trying to read each line from the input file and copy it to a string
            	 line = sc.nextLine();       	
            	if (count>0)
            	{      		
            			for(int k=0;k<line.length();k++) {          				
            				temp_puz[j][k]=line.charAt(k);   //We are trying to read each character of each line and trying to store in a matrix       				
            			}         			
            	 j++;
            	}
            	count++;  
            }
           puzzle.setCh(temp_puz);//Assigning the temporary character array puzzle to character array of puzzle object
          }
          catch(FileNotFoundException e)//Catching the file not found exception
          {
              e.printStackTrace();
          }
          finally {
            if (sc != null) sc.close();
          }
        
        return puzzle;
	}
//By using the findPenguin method we are trying to find the penguins location whenever it is needed.
// Here we are passing Puzzle class object as a method input parameter and penguin location data which is stored in RowColumn class object
//is passed as function return parameter.
public static RowColumn findPenguin(Puzzle puzzle) {
	RowColumn penguin_position= new RowColumn();			
	
	for (int i=0; i<puzzle.getRow();i++) {
		for (int j=0; j<puzzle.getCol();j++) {
			if((puzzle.getCh()[i][j]=='P')||(puzzle.getCh()[i][j]=='R')||(puzzle.getCh()[i][j]=='X'))
			{ 
				//We will find the entire matrix for the penguin by searching each element and if we get the below values
				//P penguin is alive in that position
				//R penguin is on snow cell in that position
				//X means penguin is dead at the position
				penguin_position.setRow(i);
				penguin_position.setCol(j);
			}
			else {		//System.out.println("Penguin not found");		
			}
		}
	}	
	return penguin_position;
}

//The updatePuzzle method helps us to move the penguin in the required direction by taking direction of move, penguin current position
// and the puzzle object as input paramters and the return type of the method is a Puzzle object which consists of updated puzzle and size of puzzle.
public static Puzzle updatePuzzle(int move, RowColumn penguin_position,Puzzle puzzle)
{
	RowColumn penguin_newposition=new RowColumn();
	RowColumn penguin_oldposition=new RowColumn();
	RowColumn path=new RowColumn();
	 int n=0, count=0;// Variable n is used to store the case and the count is used to break the loop when penguin reaches ice cell.
	 int l=0,k=0;//These variable are created to store the values that need to be subtracted or added when moving in different directions.
	 penguin_oldposition.setRow(penguin_position.getRow());//Assigning the current position to penguin
	 penguin_oldposition.setCol(penguin_position.getCol());	 
		 path=Direction(move);//The direction function returns the row and column increment or decrement values
		 k=path.getRow();//Assigning the row increment or decrement value to variable
		 l=path.getCol();//Assigning the column increment or decrement value to variable
		 while((puzzle.getCh()[penguin_oldposition.getRow()][penguin_oldposition.getCol()]!='X')&&(puzzle.getCh()[penguin_oldposition.getRow()][penguin_oldposition.getCol()]!='#')&&
				 (count!=1)&&((penguin_oldposition.getRow()>0)&&(penguin_oldposition.getCol()>0)
				 &&(penguin_oldposition.getRow()<(puzzle.getRow()-1))&&(penguin_oldposition.getCol()<(puzzle.getCol()-1)))) {	 
			 penguin_newposition.setRow((penguin_oldposition.getRow()+k));
			 penguin_newposition.setCol((penguin_oldposition.getCol()+l));
			 n=findCase(puzzle.getCh()[penguin_newposition.getRow()][penguin_newposition.getCol()]);
			//This findCase method helps us to pick a  case based on next element(#or*or orhazzard) in the path
			 // and execute the direction movement statements accordingly
			 puzzle=updatePosition(n,penguin_newposition,penguin_oldposition,puzzle);
			 //This function is going to update the penguin's position with respect to the next and current position of penguin
			 penguin_oldposition.setRow(penguin_newposition.getRow());//Updating the old positions with new position of penguin
			 penguin_oldposition.setCol(penguin_newposition.getCol());
			 if(puzzle.getCh()[penguin_oldposition.getRow()][penguin_oldposition.getCol()]=='R') {
				 count=count+1;
				 //If the penguin is stuck in the ice cell(R) then the loop will stop execution
			 }			 
		 }		 
	return puzzle;
}

//This method will generate the row and column increment or decrement values
public static RowColumn Direction(int move){
	int direction [][] ={{1,-1}, {1,0},{1,1},{0,-1},{0,1},{-1,-1},{-1,0},{-1,1}};
	RowColumn movement=new RowColumn();
	if(move<5) {
		move=move-1; //We are trying to assign the increment or decrement values for move<5
		movement.setRow(direction[move][0]);
		movement.setCol(direction[move][1]);
	}
	else if(move==5) {
		System.out.println("Invalid move");
	}
	else {
		move=move-2;//We are trying to assign the increment or decrement values for move>5
		movement.setRow(direction[move][0]);
		movement.setCol(direction[move][1]);
	}
	return movement;
}

//This findCase method helps us to pick a  case based on next element('#'or'*'or' 'or'hazzard') in the path
// and execute the direction movement statements accordingly
public static int findCase(char ch) {
	int n=0;
	 if (ch=='*') {n=1;}
		else if((ch=='U')||(ch=='S')){n=2;}
		else if (ch=='0'){n=3;}
		else if(ch==' '){n=4;}
		else if(ch=='#') {n=5;}
		else {System.out.println("Invalid Case");}
	return n;
}

//This method helps us to update the position of penguin to next step by considering next element case as input, penguin old and new positions and puzzle as 
//parameters and is going to return updated puzzle
public static Puzzle updatePosition(int n, RowColumn penguin_newposition,RowColumn penguin_oldposition, Puzzle puzzle ){		
	char [][] puz;
	puz=puzzle.getCh();
	char ch[][] = {{' ','0','P'},{' ','0','X'},{' ','0','R'},{' ','0','P'},{'P','R','#'}};
	//This array consists of information about position update
	//The first column of the array gives us value which has to set previous position of penguin if its previous position is in a empty cell
	//The second column of the array gives us value which has to set previous position of penguin if its previous position is in a snow cell
	//The third column of the array gives us value which has to set new position of penguin based on the case
	n=n-1;
	puz[ penguin_newposition.getRow()][ penguin_newposition.getCol()]=ch[n][2];
	if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='P') {
		puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=ch[n][0];}
		else if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='R') {
			puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=ch[n][1];}
	puzzle.setCh(puz);
	return puzzle;
}

//The findFishCount method helps us to count of fish present in the puzzle which is passed as a parameter using Puzzle object.
public static int findFishCount(Puzzle puzzle) {
	int fishcount=0;
	for(int i=0;i<puzzle.getRow();i++) {
		for(int j=0;j<puzzle.getCol();j++) {
			if(puzzle.getCh()[i][j]=='*') {
				fishcount=fishcount+1;
			}
		}
	}
	return fishcount;
}

//This method helps use the write the puzzle, score and the path travelled by the penguin.
public static void writePuzzle(Puzzle puzzle,int sc, String path,String fileName){
	
	try {
		//String fileName = "C:\\Users\\MYPC\\Desktop\\Output.txt";			
      String text= "";
      String line="";
      String score=Integer.toString(sc);// We are tring to convert the int score to string in order write string value into file.
      line=path+"\n"+score+"\n";     
      //
      for(int i=0; i<puzzle.getRow();i++) {
      	for(int j=0; j<puzzle.getCol(); j++) {
      		if(puzzle.getCh()[i][j]=='R') {//Each character in a row we are copying it into a string	
      			puzzle.getCh()[i][j]='P';
      		text=text+puzzle.getCh()[i][j];
      		}
      		else{
      			text=text+puzzle.getCh()[i][j];
      		}
      	}
      	text=text+"\n"; //Each two is concatenated with the other rows
      }          
      line= line+text;    
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,false));// We are trying to write the data into the file
      writer.write(line);
      writer.close();          
  }		
	catch(IOException e) {
		 System.out.print(e.getMessage());
	}
}

//                          ASSIGNMENT 2 CODE START


//This method helps us to generate all the valid moves with respect to penguin's current position
//This method takes the penguin_position and puzzle object as input parameters
public static ArrayList<Long> generatevalidMoves(RowColumn penguin_position, Puzzle puzzle){
	ArrayList<Long> validmoves = new ArrayList<Long>();
	int moves[]= {1,2,3,4,6,7,8,9};//We are storing all the list of possible moves in an array
	RowColumn direct=new RowColumn();
	int move=0;
	for(int i=0;i<moves.length;i++) {
		move=moves[i];
		direct=Direction(move);//This method helps us to get the row and column increment or decrement values based on direction of move.
		if(puzzle.ch[(penguin_position.getRow()+direct.getRow())][(penguin_position.getCol()+direct.getCol())]!='#') {
			validmoves.add((long)move);//If the next position with respect to penguin's current position doesnt have any wall then
			//it will add the move to list of possible moves
		}
		}
	return validmoves;
}

//The generatePushList method helps us to generate the list of possible moves including the current move. Here we are passing only the moves which don't have
//is no shark or bear or wall in the current penguin's position after the move.
public static ArrayList<Long> generatePushList(long move_path, ArrayList<Long> validmoves){
	ArrayList<Long> pushmoves = new ArrayList<Long>();
	long k=0;
	for(int i=0;i<validmoves.size();i++) {
		k=(move_path*10)+validmoves.get(i);//all the possible valid moves are appended to the current move one by one
		pushmoves.add(k);
	}
	return pushmoves;
}

//This function helps me to push the values from pushList at the end of my frontier
public static LinkedList<Long> pushtoFrontier(ArrayList<Long> pushmoves,LinkedList<Long> frontier){
	frontier.addAll(pushmoves);
	return frontier;
}

//This function helps me to pop the first value and remove it from the frontier.
public static long popfromFrontier(LinkedList<Long> frontier){
	long pop=frontier.get(0);
	frontier.remove(0);
	return pop;
}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String int_path="",final_path="";// The int_path and final_path variables are created in order to store the 
		int score=0,count=0;//The score variable is created to store the number of fish consumed by penguin. 
		int fishcount=0,initial_fish=0;//These variables are created in order to store initial and final count of the fishes 
		Puzzle puzzle=new Puzzle(); //Creating a puzzle object to store the puzzle and its dimensions.
		Puzzle temp_puzzle=new Puzzle();//Creating a temporary puzzle object which would be used as intermediate puzzle object in loops or methods
		RowColumn direction=new RowColumn();//This object is created to store the increment or decrement values or row and column when travelled in a particular direcction
		RowColumn penguin_position= new RowColumn();//This object is used to store the location of penguin.
		LinkedList<Long> frontier = new LinkedList<Long>();//This linkedlist is used to store the frontier
		ArrayList<Long> pushmoves = new ArrayList<Long>();//This arraylist is used to store the final push values into frontier in BFS
		ArrayList<Long> validmoves = new ArrayList<Long>();//This arraylist is used to store all the possible moves at a given location of penguin
		long move_path=0;//This variable is used to store the pop value from the frontier.
		
		String inputFilePath = args[0];
		String outputFilePath = args[1];
		puzzle=readPuzzle(inputFilePath); // Reading the puzzle data and dimensions
		
		penguin_position=findPenguin(puzzle);//Finding the initial postion of the penguin.
		initial_fish=findFishCount(puzzle);//Finding the intial fish count
		validmoves= generatevalidMoves(penguin_position,puzzle);//Generating all the possible moves at a initial location of penguin
		frontier=pushtoFrontier(validmoves,frontier);//Pushing all the valid moves into frontier at penguin's intial location
		
		while(count!=1) {
			score=0;//At each loop the score is set to zero
		temp_puzzle=readPuzzle(inputFilePath);//Reading the puzzle
		
		move_path=popfromFrontier(frontier);//Poping the value from frontier and the value can be 1, 34, 4997988 etc
		int_path=String.valueOf(move_path);//As we dont have the direction function to calculate the length of long integer in java, I am converting it to string

		for(int i=0;i<int_path.length();i++) { 
			//The for loop runs until it completes all the directions in the path
			//for example we have 14563 then it will iterate 5 times updating the temp_puzzle after each move 
			int m=Integer.parseInt(String.valueOf(int_path.charAt(i)));//As we have converted the poped value into string, we are trying to convert it back to int by
			//taking each character of string every time.  
			penguin_position=findPenguin(temp_puzzle);//This method helps us to find the penguin location
			temp_puzzle=updatePuzzle(m,penguin_position,temp_puzzle);//We are trying to update the puzzle
		}
		//After all penguin has moved all the directions present in the pop value we are calculating fish count, penguin position, score
		fishcount=findFishCount(temp_puzzle);
		score=initial_fish-fishcount;
		penguin_position=findPenguin(temp_puzzle);
		
		if(score>=8) {
			puzzle=temp_puzzle;//Once the goal state is reached assigning the temp_puzzle object to main puzzle object
			final_path=int_path;//assigning the final path
			count++;//Count is used to break the loop
		}
		else if(temp_puzzle.getCh()[penguin_position.getRow()][penguin_position.getCol()]=='X') {
			//In this case we are not pushing any values into the frontier as it leads to penguin's death state
		}
		else {
			validmoves= generatevalidMoves(penguin_position,temp_puzzle);//We are generating all the possible moves  if the penguin is  alive after penguin has finished 
			//traversal of all the directions from the popup value from the frontier
			pushmoves=generatePushList(move_path, validmoves);//Generating the final push list including the current popup move
			frontier=pushtoFrontier(pushmoves,frontier);//Pushing the final push list to the frontier
		}
		 }

		writePuzzle(puzzle,score,final_path,outputFilePath);//Writing the values to the output text file		
	}

}
