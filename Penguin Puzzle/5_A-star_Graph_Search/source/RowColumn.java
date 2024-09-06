/**
 * 
 */
package assignment5;

/**
 * @author ChandraSekhar Akkenapally
 *
 */
//The RowColumn class is created in order store the row column dimensions of penguin positions or puzzle size. It is also used to pass 
//the row and column values to different functions.
public class RowColumn {
	public int row=0;
	public int col=0;
	   String line;	
	public int getRow() {//This method is getter which helps me to return the Row value
		 
		return row;
	}
	public void setRow(int row) {//This method is setter which helps me to set the Row value
		this.row = row;
	}
	public int getCol() {//This method is getter which helps me to return the Column value
		
		return col;
	}
	public void setCol(int col) {//This method is setter which helps me to set the Column value
		this.col = col;
	}	
	public String toString() {//This method helps is used to return the row and column values as string but separated by _.
		return String.format("%s_%s",this.row,this.col);
	}
}
