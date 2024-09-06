/**
 * 
 */
package TestRunPack;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
/**
/**
 * @author Chandrashekar Akkenapally
 *
 */

// The RowColumn class is created in order store the row column dimensions of penguin positions and puzzle size. It is also used to pass 
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
// as a method variable under different circumstances.
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



public class Assignment1 {
	
	/**
	 * @param args
	 */

	//This method is used to create puzzle by providing the row and column size dynamically.
	public static char[][] createPuzzle(RowColumn puzzlesize){
		char[][] puz= new char[puzzlesize.getRow()][puzzlesize.getCol()];
		return puz;
	}
	
	//This function helps us to read the puzzle data from the input text file.
	public static Puzzle readPuzzle(){
		
		int count=1;
		Puzzle puzzle=new Puzzle();
		RowColumn puzzlesize= new RowColumn();
		Scanner sc = null;
        try {
        	
            File file = new File("C:\\Users\\MYPC\\Desktop\\Input.txt"); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner 
            String line;
            line=sc.nextLine();  //We are trying to fetch and read the first line from the input file
            String split[] = line.split(" ");//We are splitting the first line into two parts and converting
         // them to integers to know the row column dimensions of puzzle
            puzzlesize.setRow(Integer.parseInt(split[0]));
            puzzlesize.setCol(Integer.parseInt(split[1])); 
            puzzle.setRow(puzzlesize.getRow());
            puzzle.setCol(puzzlesize.getCol());
              char[][] temp_puz=createPuzzle(puzzlesize);
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
           puzzle.setCh(temp_puz);
          }
          catch(FileNotFoundException e)
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
	//is pass as function return.
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
					else {
						
					}
				}
			}	
			return penguin_position;
		}
		
		
		//The updatePuzzle method helps us to move the penguin in the required direction by providing direction of move, penguin current position
		// and the puzzle object as paramters and the return type of the method is a Puzzle object which consists of updated puzzle and size of puzzle.
		public static Puzzle updatePuzzle(int move, RowColumn penguin_position,Puzzle puzzle)
		{
			RowColumn penguin_newposition=new RowColumn();
			RowColumn penguin_oldposition=new RowColumn();
			int i=penguin_position.getRow(),j=penguin_position.getCol();
			 int n=0;
			 penguin_oldposition.setRow(penguin_position.getRow());
			 penguin_oldposition.setCol(penguin_position.getCol());
			 char[][] ch= new char[puzzle.getRow()][puzzle.getCol()];
			 ch=puzzle.getCh();
			if(move==1) {
				 //Move SouthWest
				// while moving SouthWest we are decreasing the column and increasing the row.
				 for (i=penguin_position.getRow()+1, j=penguin_position.getCol()-1;i<puzzle.getRow()&&j>=0;i++,j--) {
					 n=findCase(puzzle.getCh()[i][j]);
					 //This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					 //The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						 //If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						 //penguin is stuck in the ice cell(R) then the loop will stop execution
						 j=-1;
					 }	 
				 }
			}
			 else if(move==2) {
				 //Move South
				 //While moving south we are increasing the row by keeping column constant
				 for(i=((penguin_position.getRow())+1); i<puzzle.getRow();i++){
					 n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						 //penguin is stuck in the ice cell(R) then the loop will stop execution
						 i=puzzle.getRow()+1;
					 }	 
				 }
				 
			 }
			
			 else if(move==3) {
				 //Move SouthEast
				 //While moving south east we are increasing the row and column values
				 for ( i=penguin_position.getRow()+1, j=penguin_position.getCol()+1;i<puzzle.getRow()&&j<puzzle.getCol();i++,j++){
					 n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
					//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
				    //penguin is stuck in the ice cell(R) then the loop will stop execution
						 i=puzzle.getRow()+1;
					 }	 
					 
					
				 }}
			 else if(move==4) {
				 //Move West
				 //While moving west we are decreasing the column value by keeping the row constant
				 for(j=((penguin_position.getCol())-1); j>=0;j--){
					 n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);
					//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						    //penguin is stuck in the ice cell(R) then the loop will stop execution
						 j=-1;
					 }	 
				 }}
			 else if(move==6) {
				 //Move East
				 //While moving east we are increasing the column value by keeping the row constant
				 for( j=((penguin_position.getCol())+1); j<puzzle.getCol();j++){
					 n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						    //penguin is stuck in the ice cell(R) then the loop will stop execution
						 j=puzzle.getCol()+1;
					 }	 
				 }}
			 else if(move==7) {
					//Move NorthWest
				 //While moving north west we are decreasing both row and column values
				 for ( i=penguin_position.getRow()-1, j=penguin_position.getCol()-1;i>=0&&j>=0;i--,j--){
					 n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						    //penguin is stuck in the ice cell(R) then the loop will stop execution
						 j=-1;
					 }	 
				 }}
			 else if(move==8) {
				 //Move North
				 //While moving north we are decreasing the row value by keeping the column constant.
				 for( i=((penguin_position.getRow())-1); i>=0;i--){
					 n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						    //penguin is stuck in the ice cell(R) then the loop will stop execution
						 i=-1;
					 }	 
				 }}
			 else if(move==9)
			 {
				 //Move NorthEast
				 // While moving north east we are decreasing the row value by increasing the column value
				 for (i=penguin_position.getRow()-1, j=penguin_position.getCol()+1;i>=0&&j<puzzle.getCol();i--,j++) {
					n=findCase(puzzle.getCh()[i][j]);
					//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
					 // and execute the direction movement statements accordingly
					 penguin_position=findPenguin(puzzle);//This method helps to find the location of penguin
					 penguin_newposition.setRow(i);
					 penguin_newposition.setCol(j);
					 puzzle=executeSwitch(n,penguin_newposition,penguin_oldposition,puzzle);
					//The execuiteSwitch method returns the update position of the penguin.
					 penguin_oldposition.setRow(i);
					 penguin_oldposition.setCol(j);
					 if((puzzle.getCh()[i][j]=='X')||(puzzle.getCh()[i][j]=='#')||(puzzle.getCh()[i][j]=='R')) {
						//If the penguin status is dead(X) or if the penguin's position is not updated due to wall(#) or if the
						    //penguin is stuck in the ice cell(R) then the loop will stop execution
						 i=-1;
					 }	 
				 }}
		  
			return puzzle;
		}
		
		//This findCase method helps us to pick a switch case based on next element(#or*or orhazzard) in the path
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
		
		//The execute Switch method helps to update the next location in which the penguin is going to be and the current location 
		//from which the penguin is moved.
		public static Puzzle executeSwitch(int n, RowColumn penguin_newposition,RowColumn penguin_oldposition, Puzzle puzzle ){		
			char [][] puz;
			puz=puzzle.getCh();
			switch (n)
			{
			case 1:
			{
				puz[ penguin_newposition.getRow()][ penguin_newposition.getCol()]='P';
				if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='P') {
					puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=' ';}
					else if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='R') {
						puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]='0';
					}
				break;
			}
			case 2:
			{
				puz[ penguin_newposition.getRow()][ penguin_newposition.getCol()]='X';
				if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='P') {
				puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=' ';}
				else if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='R') {
					puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]='0';
				}
				
				break;
			}
			case 3:
			{
				
				puz[ penguin_newposition.getRow()][ penguin_newposition.getCol()]='R';
			     if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='P') {
						puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=' ';}
						else if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='R') {
							puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]='0';
						}
			    
			     break;
			}
			case 4:
			{
				puz[ penguin_newposition.getRow()][ penguin_newposition.getCol()]='P';
				if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='P') {
					puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=' ';}
					else if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='R') {
						puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]='0';
					} 
				break;
			}
			case 5:
			{    puz[ penguin_newposition.getRow()][ penguin_newposition.getCol()]='#';
				if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='P') {
					puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]='P';}
					else if(puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]=='R') {
						puz[ penguin_oldposition.getRow()][ penguin_oldposition.getCol()]='R';
					}
					else {
						System.out.println("Invalid previous position");
					}
				
				break;
			}
			default:
			{
				System.out.println("Invalid switch");
			}
			}
			puzzle.setCh(puz);
			return puzzle;
		}
		//The generateRandomDirection() method helps to pick a random number and return that random number
		//If we get 0 or 5 or greater than 9 then the function is called recursively.
		public static int generateRandomDirection() {
			int int_random=0, upperbound=9;
			Random rand= new Random();
			int_random= rand.nextInt(upperbound)+1;
			if ((int_random==0)||(int_random==5)||(int_random>9)) {
				//
				
				generateRandomDirection();
			}
			return int_random;
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
		public static void writePuzzle(Puzzle puzzle,int sc, String path){
			
			try {
				String fileName = "C:\\Users\\MYPC\\Desktop\\Output.txt";			
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
	           // writer.write("");
	           // writer.flush();
	            writer.write(line);
	            writer.close();          
	        }		
			catch(IOException e) {
				 System.out.print(e.getMessage());
			}
		}
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String int_path="",final_path="";// The int_path and final_path variables are created in order to store the 
		// intermediate path and final path including all intermediate paths travelled by penguin.
		int score=0,int_random=0;//The score variable is created to store the number of fish consumed by penguin. 
		//The random variable is created to store the random value generated by the generateRandomDirection.
		int fishcount=0,initial_fish=0;//These variables are created in order to store initial and final count of the fishes 
		//after the penguins movement in the puzzle.
		Puzzle puzzle=new Puzzle(); //Creating a puzzle object to store the puzzle and its dimensions.
		puzzle=readPuzzle(); // Reading the puzzle data and dimensions
		RowColumn penguin_position= new RowColumn();
		char[][] ch= new char[puzzle.getRow()][puzzle.getCol()];	
		ch=puzzle.getCh();
		penguin_position=findPenguin(puzzle);//Finding the initial postion of the penguin.
		initial_fish=findFishCount(puzzle);//Finding the intial fish count
		
		//Here were are executing a loop which generates random directions for 6 times and after the penguin has travelled in that direction
		// a condition is validated based on status of the penguin and fishcount in the puzzle.
		for( int i=1;i<=6;i++) {
			penguin_position=findPenguin(puzzle);
			fishcount=findFishCount(puzzle);
			int_random=generateRandomDirection();
						
			 if((ch[penguin_position.getRow()][penguin_position.getCol()])=='X') {
				//"Game Over");
				i=7;
			}
			else if((fishcount==0)&&((ch[penguin_position.getRow()][penguin_position.getCol()])!='X')) {
				//"Penguin Won the Game");
				i=7;
			}
			else if((fishcount==0)&&((ch[penguin_position.getRow()][penguin_position.getCol()])=='X')) {
				//"Highes Score Obtained but penguin is dead");
				i=7;
			}
			else {
				puzzle=updatePuzzle(int_random,penguin_position,puzzle);
				int_path=Integer.toString(int_random);
				 final_path=final_path+int_path;
			}
			 
           }
			 fishcount=findFishCount(puzzle);//Calculation the fishcount at the end of the loop
		score=initial_fish-fishcount;//Calculating the score

		writePuzzle(puzzle,score,final_path);//Writing the values to the output text file
		}

	}


