// Name: Haolun Cheng
// USC NetID: haolunch
// CS 455 PA3
// Fall 2021


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield). Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   /**
    * Representation Invariants:
    * -- Covered states above all squares are within the range of the input MineField
    * -- Reset game display should make each covered state back to COVERED
    */

   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
  
   private static final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
   private MineField mineField;
   private int mineGuess;
   private boolean isGameOver;
   private int[][] coverUp;
   

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
      this.mineField = mineField;
      this.isGameOver = false;
      this.mineGuess = 0;
      this.coverUp = new int[mineField.numRows()][mineField.numCols()];
      for(int i = 0; i < this.coverUp.length; i ++){
         for(int j = 0; j< this.coverUp[0].length; j++){
            coverUp[i][j] = COVERED;
         }
      }
   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      this.isGameOver = false;
      this.mineGuess = 0;
      for(int i = 0; i < this.coverUp.length; i ++){
         for(int j = 0; j< this.coverUp[0].length; j++){
            coverUp[i][j] = COVERED;
         }
      }
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return this.mineField;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return this.coverUp[row][col];
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return this.mineField.numMines() - this.mineGuess;
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      if(this.coverUp[row][col] == COVERED) {
         this.coverUp[row][col] = MINE_GUESS;
         this.mineGuess++;
      }
      else if(this.coverUp[row][col] == MINE_GUESS) {
         this.coverUp[row][col] = QUESTION;
         this.mineGuess--;
      }
      else if(this.coverUp[row][col] == QUESTION) this.coverUp[row][col] = COVERED;
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      // If player uncovers a mine, then the player loses the game
      if(this.mineField.hasMine(row, col)) {
         this.loseGame(row, col);
         return false;
      } 
      // If player uncovers all non-mine position, then the player wins the game
      else{
         reveal(row, col);
         if(this.isGameOver()) this.winGame();
      }
      return true;
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
      int count = 0;
      for(int i = 0; i < this.mineField.numRows(); i++){
         for(int j = 0; j < this.mineField.numCols(); j++){
            // Uncover a mine causes the game to end
            if(this.mineField.hasMine(i, j) && this.isUncovered(i, j)){
               this.isGameOver = true;
               break;
            }
            if(this.isUncovered(i, j)) count++;
         }
      }
      // Uncover all non-mine locations causes the game to end
      if(count == this.mineField.numRows() * this.mineField.numCols() - this.mineField.numMines()){
         this.isGameOver = true;
      }
      return this.isGameOver;
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      if(this.coverUp[row][col] == COVERED || this.coverUp[row][col] == MINE_GUESS || this.coverUp[row][col] == QUESTION){
         return false;
      } else return true;
   }

   /**
    * If the square at (row, col) is not a mine or adjacent to any mines, recursively uncover all 
    * adjacent mines that are also not next to any mines.
    * @param row
    * @param col
    */
   private void reveal(int row, int col){
      if(row >= this.coverUp.length || row < 0 || col >= this.coverUp[0].length || col < 0
         || this.mineField.hasMine(row, col) 
         || (this.coverUp[row][col] != COVERED && this.coverUp[row][col] != QUESTION)) return;
      int numAdjacentMines = this.mineField.numAdjacentMines(row, col);
      if(numAdjacentMines > 0) {
         this.coverUp[row][col] = numAdjacentMines;
         return;
      }
      this.coverUp[row][col] = numAdjacentMines;
      for(int[] d : directions)
         reveal(row + d[0], col + d[1]);
   }

   /**
    * Game board display after winning the game.
    */
   private void winGame(){
      for(int i = 0; i < this.mineField.numRows(); i++){
         for(int j = 0; j < this.mineField.numCols(); j++){
            if(this.mineField.hasMine(i, j)) this.coverUp[i][j] = MINE_GUESS;
         }
      }
   }

   /**
    * Game board display after losing the game.
    * @param row
    * @param col
    */
   private void loseGame(int row, int col){
      this.coverUp[row][col] = EXPLODED_MINE;
      for(int i = 0; i < this.coverUp.length; i++){
         for(int j = 0; j < this.coverUp[0].length; j++){
            if(i == row && j == col) continue;
            if(this.coverUp[i][j] == MINE_GUESS && !this.mineField.hasMine(i, j)) this.coverUp[i][j] = INCORRECT_GUESS;
            if(this.coverUp[i][j] == MINE_GUESS && this.mineField.hasMine(i, j)) this.coverUp[i][j] = MINE_GUESS;
            if(this.coverUp[i][j] != MINE_GUESS && this.mineField.hasMine(i, j)) this.coverUp[i][j] = MINE;
         }
      }
   }
}