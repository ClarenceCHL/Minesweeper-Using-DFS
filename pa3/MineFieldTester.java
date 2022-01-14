public class MineFieldTester {
    public static void main(String[] args){
        boolean[][] mineData = 
        {{true, true, false, false},
        {false, false, true, true},
        {true, false, true, false},
        {false, true, false, true}};

        // Test 1-arg constructor
        MineField MF1 = new MineField(mineData);
        System.out.println("Test for MF1 (Constructor with one argument)");

        // Test numMines method
        System.out.println("Test numMines method");
        System.out.println("[Expected]The number of mines in the mine field MF1 is 8");
        System.out.println("[Actual]The number of mines in the mine field MF1 is " + MF1.numMines());

        // Test hasMine method
        System.out.println("Test hasMine method");
        System.out.println("[Expected]Is there a mine in the square of (2, 3) in MF1? false");
        System.out.println("[Actual]Is there a mine in the square of (2, 3)? " + MF1.hasMine(2, 3));
        System.out.println("[Expected]Is there a mine in the square of (0, 1) in MF1? true");
        System.out.println("[Actual]Is there a mine in the square of (0, 1)? " + MF1.hasMine(0, 1));

        // Test numRows and numCols methods
        System.out.println("Test numRows and numCols methods");
        System.out.println("[Expected]The MF1 mine field has 4 rows");
        System.out.println("[Expected]The MF1 mine field has 4 cols");
        System.out.println("[Actual]The MF1 mine field has " + MF1.numRows() + " rows");
        System.out.println("[Actual]The MF1 mine field has " + MF1.numCols() + " cols");

        // Test inRange method
        System.out.println("Test inRange method");
        System.out.println("[Expected]Is point (3, 3) in range? true");
        System.out.println("[Actual]Is point (3, 3) in range? " + MF1.inRange(3, 3));
        System.out.println("[Expected]Is point (3, 4) in range? false");
        System.out.println("[Actual]Is point (3, 4) in range? " + MF1.inRange(3, 4));

        // Test numAdjacentMines method
        System.out.println("Test numAdjacentMines method");
        System.out.println("[Expected]The number of adjacent mines of position (0, 3) is 2");
        System.out.println("[Actual]The number of adjacent mines of position (0, 3) is " + MF1.numAdjacentMines(0, 3));
        System.out.println("[Expected]The number of adjacent mines of position (2, 2) is 4");
        System.out.println("[Actual]The number of adjacent mines of position (2, 2) is " + MF1.numAdjacentMines(2, 2));

        // Test 3-args constructor
        MineField MF2 = new MineField(6, 6, 10);
        System.out.println("\nTest for MF2 (Constructor with three arguments)");

        // Test populateMineField method
        System.out.println("\nTest populateMineField method for MF2");
        MF2.populateMineField(0, 0);

        // Test numMines method
        System.out.println("Test numMines method on MF2");
        System.out.println("[Expected]The number of mines in the mine field MF2 is 10");
        System.out.println("[Actual]The number of mines in the mine field MF2 is " + MF2.numMines());

        // Test hasMine method
        System.out.println("Test hasMine method on MF2");
        System.out.println("Is there a mine in the square of (2, 2)? " + MF2.hasMine(2, 2));

        // Test numRows and numCols methods
        System.out.println("Test numRows and numCols methods on MF2");
        System.out.println("[Expected]The MF2 mine field has 6 rows");
        System.out.println("[Expected]The MF2 mine field has 6 cols");
        System.out.println("[Actual]The MF2 mine field has " + MF2.numRows() + " rows");
        System.out.println("[Actual]The MF2 mine field has " + MF2.numCols() + " cols");
    }
}
