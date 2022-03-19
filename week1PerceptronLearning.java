class Solution {
    public void perceptronLearning(double[][] inputs) {
        boolean accurate = false;
        float wZero = 0;
        float wOne = 0;
        float wTwo = 0;
        int epochCounter = 0;
        int accuratecounter = 0;
        while (accurate == false) {
            for (int i = 0; i < inputs.length; i++) {
                double result = inputs[i][0] * wZero + inputs[i][1] * wOne + inputs[i][2] * wTwo;
                int resultabsolute = 0;
                if (result > 0) {
                    resultabsolute = 1;
                } else {
                    resultabsolute = -1;
                }
                if (inputs[i][3] != resultabsolute) {
                    accuratecounter = 0;
                    // change weights
                    wZero += inputs[i][3] * inputs[i][0];
                    wOne += inputs[i][3] * inputs[i][1];
                    wTwo += inputs[i][3] * inputs[i][2];
                } else {
                    accuratecounter++;
                }
            }
            if (accuratecounter == 6){
                accurate = true;
            }
            else {
                accuratecounter = 0;
            }
            epochCounter++;
        }
        System.out.print("system stable. number of epochs: ");
        System.out.println(epochCounter);
        System.out.println("final weights:");
        System.out.println(wZero);
        System.out.println(wOne);
        System.out.println(wTwo);
    }
    public static void main(String[] args) {
        double[][] inputs = { { 1, 1, 4, -1 }, { 1, 2, 9, 1 }, { 1, 5, 6, 1 }, { 1, 4, 5, 1 }, { 1, 6, 0.7, -1 },
                { 1, 1, 1.5, -1 } };
        Solution test = new Solution();
        test.perceptronLearning(inputs);
    }
}