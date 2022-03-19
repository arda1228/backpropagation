class newclass {
    public void perceptronLearning(double[][] inputs) {
        float wZero = 0;
        float wOne = 0;
        float wTwo = 0;
        int epochCounter = 0;
        double learningParameter = 0.01;
        while (epochCounter < 500) {
            for (int i = 0; i < inputs.length; i++) {
                double result = inputs[i][0] * wZero + inputs[i][1] * wOne + inputs[i][2] * wTwo;
                int resultabsolute = 0;
                if (result > 0) {
                    resultabsolute = 1;
                } else {
                    resultabsolute = -1;
                }
                wZero += learningParameter * (inputs[i][3] - result);
                //* inputs[i][0];
                wOne += learningParameter * (inputs[i][3] - result) * inputs[i][1];
                wTwo += learningParameter * (inputs[i][3] - result) * inputs[i][2];
            }
            epochCounter++;
        }
        System.out.print("training complete. number of epochs: ");
        System.out.println(epochCounter);
        System.out.println("final weights:");
        System.out.println(wZero);
        System.out.println(wOne);
        System.out.println(wTwo);
    }

    public static void main(String[] args) {
        double[][] inputs = { { 1, 1, 4, -1 }, { 1, 2, 9, 1 }, { 1, 5, 6, 1 }, { 1, 4, 5, 1 }, { 1, 6, 0.7, -1 },
                { 1, 1, 1.5, -1 } };
        newclass test = new newclass();
        test.perceptronLearning(inputs);
    }
}
// Set W to a random start point
// 2. Select a training example k (cycle through)
// 3. Update each weight n: Wn
// *=Wn + 0.01(C - S)En
// 4. Return to step 2 (repeat many times)