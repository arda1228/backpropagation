import java.io.FileNotFoundException;
import java.util.Random;

class backpropexample {
    // standardising inputs
    // read from text file
    // make functions to do calculations
    public double sigmoidActivation(double input) {
        return 1 / (1 + Math.exp(-input));
    }

    public double sigmoidActivationDiff(double input) {
        return input * (1 - input);
    }

    public double tanhActivation(double input) {
        return (Math.exp(input) - Math.exp(-input)) / (Math.exp(input) + Math.exp(-input));
    }

    public double tanhActivationDiff(double input) {
        return 1 - (input * input);
    }

    // standardise functions
    // finding outliers (excel)

    public double deltaHidden(double weight, double nextdelta, double value, boolean Sigmoid) {
        if (Sigmoid) {
            return weight * nextdelta * sigmoidActivationDiff(value);
        } else {
            return weight * nextdelta * tanhActivationDiff(value);

        }
    }

    // MAIN FUNCTION
    public void backProp(double[] inputs, int NumberOfHiddenNodes, int epochs, boolean Sigmoid) {
        // inputs = this.StandardiseInputs(inputs);
        Random rand = new Random(67); // instance of random class
        int epochCounter = 0;
        double p = 0.1;
        int NoOfInputs = inputs.length - 1;
        double[][] inputToHiddenWeights = new double[NoOfInputs][NumberOfHiddenNodes];
        for (int i = 0; i < NoOfInputs; i++) {
            for (int j = 0; j < NumberOfHiddenNodes; j++) {
                inputToHiddenWeights[i][j] = rand.nextDouble();
            }
        }
        double[] hiddenToOutputWeights = new double[NumberOfHiddenNodes];
        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenToOutputWeights[i] = rand.nextDouble();
        }
        double[] hiddenLayerBiases = new double[NumberOfHiddenNodes];
        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenLayerBiases[i] = rand.nextDouble();
        }
        double[] outputBiases = { rand.nextDouble() };
        double[] hiddenLayerWeightedSums = new double[NumberOfHiddenNodes];
        double[] hiddenLayerActivation = new double[NumberOfHiddenNodes];
        double[] outputLayerWeightedSums = new double[1];
        double[] outputsActivation = new double[1];
        double[] deltaValuesHidden = new double[NumberOfHiddenNodes];
        double[] deltaValueOutput = new double[1];
        while (epochCounter < epochs) {
            // forward pass
            for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
                hiddenLayerWeightedSums[i] = 0;
                for (int j = 0; j < inputs.length - 1; j++) {
                    hiddenLayerWeightedSums[i] += inputs[j] * inputToHiddenWeights[j][i];
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
            }
            // backwards pass
            for (int i = 0; i < outputsActivation.length; i++) {
                deltaValueOutput[i] = (inputs[inputs.length - 1] - outputsActivation[i])
                        * (outputsActivation[i] * (1 - outputsActivation[i]));
                outputBiases[i] += p * deltaValueOutput[i];

            }
            for (int i = 0; i < hiddenLayerActivation.length; i++) {
                deltaValuesHidden[i] = this.deltaHidden(hiddenToOutputWeights[i], deltaValueOutput[0],
                        hiddenLayerActivation[i], Sigmoid);
                hiddenToOutputWeights[i] += p * deltaValueOutput[0] * hiddenLayerActivation[i];
                hiddenLayerBiases[i] += p * deltaValuesHidden[i];
            }
            for (int i = 0; i < inputToHiddenWeights.length; i++) {
                for (int j = 0; j < NumberOfHiddenNodes; j++) {
                    inputToHiddenWeights[i][j] += p * deltaValuesHidden[j] * inputs[i];
                }
            }
            epochCounter++;
        }

    }
//next
//remove the dates from each array
//standardise all inputs
//use the next array's output as the desired output for the current set of inputs
//rearrange the set so that the output is at the end of each array
    public static void main(String[] args) throws FileNotFoundException {
        double[] testinputs = { 1, 9, 5, 7, 8, 0, 2, 1 };
        backpropexample test = new backpropexample();
        test.backProp(testinputs, 4, 13, false);
        readingfromexternal wd = new readingfromexternal();
        System.out.println("records: " + wd.getValues());
    }
}