public class VisibleFieldTester {
    public static void main(String[] args){
        boolean[][] mineData = 
        {{true, true, false, false},
        {false, false, true, true},
        {true, false, true, false},
        {false, true, false, true}};

        // Test Visible Field Constructor
        MineField MF = new MineField(mineData);
        VisibleField VF = new VisibleField(MF);

        // Test getStatus method
        System.out.println("The status of (2, 2) is " + VF.getStatus(2, 2));
        System.out.println("The status of (2, 1) is " + VF.getStatus(2, 1));

        // Test numMinesLeft method
        System.out.println("Number of mines left is " + VF.numMinesLeft());

        // Test getMineField method
        System.out.println("The Mine Field is " + VF.getMineField());
    }
}
