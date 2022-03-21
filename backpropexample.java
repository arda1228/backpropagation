import java.util.Random;

class backpropexample {
    // standardising inputs
    // make code adaptable
    // read from text file
    // make functions to do calculations
    public double sigmoidActivation(double input) {
        return 1 / (1 + Math.exp(-input));
    }

    public double sigmoidActivationDiff(double input) {
        return input * (1 - input);
    }

    public double greekHidden(double weight, double nextGreek, double value) {
        return weight * nextGreek * sigmoidActivationDiff(value);
    }

    public void backProp(double[] inputs, int NumberOfHiddenNodes, int epochs) {
        Random rand = new Random(); // instance of random class
        int epochCounter = 0;
        double p = 0.1;
        int NoOfInputs = inputs.length - 1;
        // double[][] inputToHiddenWeights = { { 3, 6 }, { 4, 5 } };
        double[][] inputToHiddenWeights = new double[NoOfInputs][NumberOfHiddenNodes];
        for (int i = 0; i < NoOfInputs; i++) {
            for (int j = 0; j < NumberOfHiddenNodes; j++) {
                inputToHiddenWeights[i][j] = rand.nextDouble();
                // System.out.println(inputToHiddenWeights[i][j]);
            }
        }
        // double[] hiddenToOutputWeights = { 2, 4 };
        double[] hiddenToOutputWeights = new double[NumberOfHiddenNodes];
        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenToOutputWeights[i] = rand.nextDouble();
        }
        // double[] hiddenLayerBiases = { 1, -6 };
        double[] hiddenLayerBiases = new double[NumberOfHiddenNodes];
        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenLayerBiases[i] = rand.nextDouble();
        }
        // double[] outputBiases = { -3.92 };
        double[] outputBiases = { rand.nextDouble() };
        // double[] hiddenLayerWeightedSums = { 0, 0 };
        double[] hiddenLayerWeightedSums = new double[NumberOfHiddenNodes];
        // double[] hiddenLayerActivation = { 0, 0 };
        double[] hiddenLayerActivation = new double[NumberOfHiddenNodes];
        // double[] outputLayerWeightedSums = { 0 };
        double[] outputLayerWeightedSums = new double[1];
        // double[] outputsActivation = { 0 };
        double[] outputsActivation = new double[1];
        // double[] greekValuesHidden = { 0, 0 };
        double[] greekValuesHidden = new double[NumberOfHiddenNodes];
        // double[] greekValueOutput = { 0 };
        double[] greekValueOutput = new double[1];
        while (epochCounter < epochs) {
            // forward pass
            for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
                hiddenLayerWeightedSums[i] = 0;
                for (int j = 0; j < inputs.length - 1; j++) {
                    hiddenLayerWeightedSums[i] += inputs[j] * inputToHiddenWeights[j][i];
                }
                hiddenLayerWeightedSums[i] += hiddenLayerBiases[i];
                hiddenLayerActivation[i] = this.sigmoidActivation(hiddenLayerWeightedSums[i]);
            }
            for (int i = 0; i < outputLayerWeightedSums.length; i++) {
                outputLayerWeightedSums[i] = 0;
                for (int j = 0; j < hiddenLayerWeightedSums.length; j++) {
                    outputLayerWeightedSums[i] += hiddenLayerActivation[j] * hiddenToOutputWeights[j];
                }
                outputLayerWeightedSums[i] += outputBiases[i];
                outputsActivation[i] = this.sigmoidActivation(outputLayerWeightedSums[i]);
                System.out.println("output: " + outputsActivation[i]);
            }
            // backwards pass
            for (int i = 0; i < outputsActivation.length; i++) {
                greekValueOutput[i] = (inputs[inputs.length - 1] - outputsActivation[i])
                        * (outputsActivation[i] * (1 - outputsActivation[i]));
                // System.out.println(greekValueOutput[i]);
                outputBiases[i] += p * greekValueOutput[i];
                // System.out.println("new output bias: "+outputBiases[i]);

            }
            for (int i = 0; i < hiddenLayerActivation.length; i++) {
                greekValuesHidden[i] = this.greekHidden(hiddenToOutputWeights[i], greekValueOutput[0],
                        hiddenLayerActivation[i]);
                hiddenToOutputWeights[i] += p * greekValueOutput[0] * hiddenLayerActivation[i];
                // System.out.println("new weight: "+hiddenToOutputWeights[i]);
                hiddenLayerBiases[i] += p * greekValuesHidden[i];
                // System.out.println("new hidden layer bias: "+hiddenLayerBiases[i]);
            }
            for (int i = 0; i < inputToHiddenWeights.length; i++) {
                for (int j = 0; j < NumberOfHiddenNodes; j++) {
                    // System.out.println("previous weight:");
                    // System.out.println(inputToHiddenWeights[i][j]);
                    inputToHiddenWeights[i][j] += p * greekValuesHidden[j] * inputs[i];
                    // System.out.println("new weight:");
                    // System.out.println(inputToHiddenWeights[i][j]);
                }
            }
            epochCounter++;
        }

    }

    public static void main(String[] args) {
        double[] testinputs = { 1, 9, 5, 7, 8, 0, 0.3 };
        backpropexample test = new backpropexample();
        test.backProp(testinputs, 10, 4);
    }
}