import java.io.FileNotFoundException;
import java.util.Random;

class backpropagation {
    // standardising inputs
    // read from text file
    // make functions to do calculations
    public double sigmoidActivation(double input) {// enter value, returns the sigmoid transfer for it
        return 1 / (1 + Math.exp(-input));
    }

    public double sigmoidActivationDiff(double input) {// enter sigmoid-activated value, returns the differential
        return input * (1 - input);
    }

    public double tanhActivation(double input) {// enter value, returns the tanh transfer for it
        return (Math.exp(input) - Math.exp(-input)) / (Math.exp(input) + Math.exp(-input));
    }

    public double tanhActivationDiff(double input) {// enter tanh-activated value, returns the differential
        return 1 - (input * input);
    }

    // standardise functions
    // finding outliers (excel)

    public double deltaHidden(double weight, double nextdelta, double value, boolean Sigmoid) {// gives the delta value
                                                                                               // of a given hidden node
        if (Sigmoid) {
            return weight * nextdelta * sigmoidActivationDiff(value);
        } else {
            return weight * nextdelta * tanhActivationDiff(value);

        }
    }

    class trainingResults {
        double[][] inputToHiddenWeights;
        double[] hiddenToOutputWeights;
        double[] hiddenLayerBiases;
        double[] outputBiases;

        trainingResults(double[][] v1, double[] v2, double[] v3, double[] v4) {
            inputToHiddenWeights = v1;
            hiddenToOutputWeights = v2;
            hiddenLayerBiases = v3;
            outputBiases = v4;
        }
    }

    // MAIN FUNCTION
    public trainingResults backProp(double[][] inputs, int NumberOfHiddenNodes, int epochs, boolean Sigmoid,
            boolean momentum) {
        // inputs = this.StandardiseInputs(inputs);
        Random rand = new Random(67); // instance of random class
        int epochCounter = 0;// updated each epoch
        double totalSquaredError = 0;
        double meanSquaredError;
        double prevWeight; // for momentum
        double p = 0.1;// learning parameter
        double alpha = 0.9; // for momentum
        // double desiredOutput = inputs[0][inputs.length - 1]; // takes the last value
        // as the output CHANGE?
        int NoOfInputs = inputs[0].length - 1;
        double[][] inputToHiddenWeights = new double[NoOfInputs][NumberOfHiddenNodes]; // each array contains the
                                                                                       // weights from a given input
                                                                                       // node
        double[][] changeInInputToHiddenWeights = new double[NoOfInputs][NumberOfHiddenNodes];
        for (int i = 0; i < NoOfInputs; i++) {
            for (int j = 0; j < NumberOfHiddenNodes; j++) {
                inputToHiddenWeights[i][j] = rand.nextDouble();
            }
        }

        double[] hiddenToOutputWeights = new double[NumberOfHiddenNodes];
        double[] changeInHiddenToOutputWeights = new double[NumberOfHiddenNodes];
        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenToOutputWeights[i] = rand.nextDouble();
        }

        double[] hiddenLayerBiases = new double[NumberOfHiddenNodes];
        double[] changeInHiddenLayerBiases = new double[NumberOfHiddenNodes];
        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenLayerBiases[i] = rand.nextDouble();
        }

        double[] outputBiases = { rand.nextDouble() };
        double[] changeInOutputBiases = { 0 };

        double[] hiddenLayerWeightedSums = new double[NumberOfHiddenNodes];
        double[] hiddenLayerActivation = new double[NumberOfHiddenNodes];
        double[] outputLayerWeightedSums = new double[1];
        double[] outputsActivation = new double[1];
        double[] deltaValuesHidden = new double[NumberOfHiddenNodes];
        double[] deltaValueOutput = new double[1];
        while (epochCounter < epochs) {
            for (int k = 0; k < inputs.length; k++) {
                // forward pass
                for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
                    hiddenLayerWeightedSums[i] = 0;
                    for (int j = 0; j < inputs[0].length - 1; j++) {
                        hiddenLayerWeightedSums[i] += inputs[k][j] * inputToHiddenWeights[j][i];
                    }
                    hiddenLayerWeightedSums[i] += hiddenLayerBiases[i];
                    if (Sigmoid) {
                        hiddenLayerActivation[i] = this.sigmoidActivation(hiddenLayerWeightedSums[i]);
                    } else {
                        hiddenLayerActivation[i] = this.tanhActivation(hiddenLayerWeightedSums[i]);
                    }
                }
                for (int i = 0; i < outputLayerWeightedSums.length; i++) {
                    outputLayerWeightedSums[i] = 0;
                    for (int j = 0; j < hiddenLayerWeightedSums.length; j++) {
                        outputLayerWeightedSums[i] += hiddenLayerActivation[j] * hiddenToOutputWeights[j];
                    }
                    outputLayerWeightedSums[i] += outputBiases[i];
                    if (Sigmoid) {
                        outputsActivation[i] = this.sigmoidActivation(outputLayerWeightedSums[i]);
                    } else {
                        outputsActivation[i] = this.tanhActivation(outputLayerWeightedSums[i]);
                    }
                    System.out.println("output: " + outputsActivation[i]);
                    totalSquaredError += Math.pow(inputs[k][inputs[k].length - 1] - outputsActivation[i], 2);
                    // change to destandardised output
                }
                // backwards pass
                for (int i = 0; i < outputsActivation.length; i++) {
                    deltaValueOutput[i] = (inputs[k][inputs[k].length - 1] - outputsActivation[i])
                            * (outputsActivation[i] * (1 - outputsActivation[i]));
                    prevWeight = outputBiases[i];
                    outputBiases[i] += p * deltaValueOutput[i];
                    changeInOutputBiases[i] = outputBiases[i] - prevWeight;
                    if (momentum) {
                        outputBiases[i] += (alpha * changeInOutputBiases[i]);
                    }

                }
                for (int i = 0; i < hiddenLayerActivation.length; i++) {
                    deltaValuesHidden[i] = this.deltaHidden(hiddenToOutputWeights[i], deltaValueOutput[0],
                            hiddenLayerActivation[i], Sigmoid);
                    prevWeight = hiddenToOutputWeights[i];
                    hiddenToOutputWeights[i] += p * deltaValueOutput[0] * hiddenLayerActivation[i];
                    changeInHiddenToOutputWeights[i] = hiddenToOutputWeights[i] - prevWeight;
                    prevWeight = hiddenLayerBiases[i];
                    hiddenLayerBiases[i] += p * deltaValuesHidden[i];
                    changeInHiddenLayerBiases[i] = hiddenLayerBiases[i] - prevWeight;
                    if (momentum) {
                        hiddenToOutputWeights[i] += (alpha * changeInHiddenToOutputWeights[i]);
                        hiddenLayerBiases[i] += (alpha * changeInHiddenLayerBiases[i]);
                    }
                }
                for (int i = 0; i < inputToHiddenWeights.length; i++) {
                    for (int j = 0; j < NumberOfHiddenNodes; j++) {
                        prevWeight = inputToHiddenWeights[i][j];
                        inputToHiddenWeights[i][j] += p * deltaValuesHidden[j] * inputs[k][i];
                        changeInInputToHiddenWeights[i][j] = inputToHiddenWeights[i][j] - prevWeight;
                        if (momentum) {
                            inputToHiddenWeights[i][j] += (alpha * changeInInputToHiddenWeights[i][j]);
                        }
                    }
                }
            }
            epochCounter++;
        }
        meanSquaredError = totalSquaredError / epochs;
        System.out.println(meanSquaredError);
        return new trainingResults(inputToHiddenWeights, hiddenToOutputWeights, hiddenLayerBiases, outputBiases);
    }

    // next
    // remove the dates from each array
    // standardise all inputs
    // use the next array's output as the desired output for the current set of
    // inputs
    // rearrange the set so that the output is at the end of each array
    public static void main(String[] args) throws FileNotFoundException {
        double[][] testinputs = { { 1, 9, 5, 7, 8, 0, 2, 1 } };
        backpropagation test = new backpropagation();
        test.backProp(testinputs, 4, 1000, true, false);
        // test.backProp(input, number of nodes in hidden layer, number of epochs,
        // sigmoid(true) or tanh(false), use of momentum(true) or not(false)
        // readingfromexternal wd = new readingfromexternal();
        // System.out.println("records: " + wd.getValues());
    }
}