// Name: Haolun Cheng
// USC NetID: haolunch
// CS 455 PA3
// Fall 2021

import java.util.Random;

/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
   /**
    * Representation Invariants:
    * -- Number of mines on the MineField cannot exceed one third of the total square amount
    * -- All squares should be within the range of the input mineData
    * -- resetEmpty() method does not change the 1-arg MineField (Fixed MineField)
    */
   
   private static final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
   private boolean[][] mineData;
   private int numRows;
   private int numCols;
   private int numMines;
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
      this.mineData = new boolean[mineData.length][mineData[0].length];
      for(int i = 0; i < mineData.length; i++){
         for(int j = 0; j < mineData[0].length; j++){
            this.mineData[i][j] = mineData[i][j];
         }
      }
      this.numRows = mineData.length;
      this.numCols = mineData[0].length;
      this.numMines = countNumMines(mineData);
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      this.mineData = new boolean[numRows][numCols];
      this.numRows = numRows;
      this.numCols = numCols;
      this.numMines = numMines;
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
      int x = 0;
      int y = 0;
      Random randomPos = new Random();
      this.resetEmpty();
      for(int i = 0; i < this.numMines(); i++){
         // If x equals to row and y equals to col or position (x, y) already has a mine, we choose another position
         // to place the mine.
         do{
            x = randomPos.nextInt(this.numRows);
            y = randomPos.nextInt(this.numCols);
         }while((x == row && y == col) || this.mineData[x][y]);
         this.mineData[x][y] = true;
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in 
         at the beginning of a game.
    */
   public void resetEmpty() {
      for(int i = 0; i < this.numRows; i++){
         for(int j = 0; j < this.numCols; j++){
            mineData[i][j] = false;
         }
      }
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      int numOfMines = 0;
      for(int[] dir : directions){
         numOfMines += hasMine(row + dir[0], col + dir[1], mineData);
      }
      return numOfMines;
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      return row >= 0 && row < this.numRows && col >=0 && col < this.numCols;
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return this.numRows;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return this.numCols;
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      return mineData[row][col] == true;
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return this.numMines;
   }

   /**
    * Returns the number of mines in the minefield.
    * @param mineData
    * @return number of mines
    */
   private int countNumMines(boolean[][] mineData){
      int count = 0;
      for(int i = 0; i < mineData.length; i++){
         for(int j = 0; j < mineData[0].length; j++){
            if(mineData[i][j]) count++; 
         }
      }
      return count;
   }

   /**
    * Returns whether position (row, col) has a mine.
    * @param row
    * @param col
    * @param mineData
    * @return 0 if there is no mine, and 1 if there is a mine.
    */
   private int hasMine(int row, int col, boolean[][] mineData){
      if(row >= mineData.length || row < 0 || col >= mineData[0].length || col < 0 || !mineData[row][col])
            return 0;
      return 1;
   }
}