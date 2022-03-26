import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;//adaptable-length array
import java.util.Arrays;
import java.util.List;

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
        int numberOfHiddenNodes;

        trainingResults(double[][] inputToHiddenWeights, double[] hiddenToOutputWeights, double[] hiddenLayerBiases,
                double[] outputBiases, int numberOfHiddenNodes) {
            this.inputToHiddenWeights = inputToHiddenWeights;
            this.hiddenToOutputWeights = hiddenToOutputWeights;
            this.hiddenLayerBiases = hiddenLayerBiases;
            this.outputBiases = outputBiases;
            this.numberOfHiddenNodes = numberOfHiddenNodes;
        }
    }

    public double destandardisedValue(double raw, double Min, double Max) {
        return ((((raw - 0.1) / 0.8) * (Max - Min)) + Min);
    }

    // MAIN FUNCTION
    public trainingResults backpropTraining(double[][] inputs, int NumberOfHiddenNodes, int epochs, boolean Sigmoid,
            boolean momentum) {
        // inputs = this.StandardiseInputs(inputs);
        Random rand = new Random(67); // instance of random class
        int epochCounter = 0;// updated each epoch
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
                    // totalSquaredError += Math.pow(inputs[k][inputs[k].length - 1] -
                    // outputsActivation[i], 2);
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
        return new trainingResults(inputToHiddenWeights, hiddenToOutputWeights, hiddenLayerBiases, outputBiases,
                NumberOfHiddenNodes);
    }

    public double testing(double[][] inputs, trainingResults results, boolean Sigmoid) {
        double totalSquaredError = 0;
        double meanSquaredError;
        // int NoOfInputs = inputs[0].length - 1;
        double[] hiddenLayerWeightedSums = new double[results.numberOfHiddenNodes];
        double[] hiddenLayerActivation = new double[results.numberOfHiddenNodes];
        double[] outputLayerWeightedSums = new double[1];
        double[] outputsActivation = new double[1];
        for (int k = 0; k < inputs.length; k++) {
            // forward pass
            for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
                hiddenLayerWeightedSums[i] = 0;
                for (int j = 0; j < inputs[0].length - 1; j++) {
                    hiddenLayerWeightedSums[i] += inputs[k][j] * results.inputToHiddenWeights[j][i];
                }
                hiddenLayerWeightedSums[i] += results.hiddenLayerBiases[i];
                if (Sigmoid) {
                    hiddenLayerActivation[i] = this.sigmoidActivation(hiddenLayerWeightedSums[i]);
                } else {
                    hiddenLayerActivation[i] = this.tanhActivation(hiddenLayerWeightedSums[i]);
                }
            }
            for (int i = 0; i < outputLayerWeightedSums.length; i++) {
                outputLayerWeightedSums[i] = 0;
                for (int j = 0; j < hiddenLayerWeightedSums.length; j++) {
                    outputLayerWeightedSums[i] += hiddenLayerActivation[j] * results.hiddenToOutputWeights[j];
                }
                outputLayerWeightedSums[i] += results.outputBiases[i];
                if (Sigmoid) {
                    outputsActivation[i] = this.sigmoidActivation(outputLayerWeightedSums[i]);
                } else {
                    outputsActivation[i] = this.tanhActivation(outputLayerWeightedSums[i]);
                }
                System.out.println("modelled: " + outputsActivation[i]);
                totalSquaredError += Math.pow(inputs[k][inputs[k].length - 1] - outputsActivation[i], 2);
                // change to destandardised output
            }
        }
        meanSquaredError = totalSquaredError / inputs.length;
        // System.out.println("mean squared error: " + meanSquaredError);
        return meanSquaredError;
    }

    // class standardisedPackager {//contains all data needed to (de)standardise a value
    //     double[][] inputsStandardised;
    //     double[] mins;
    //     double[] maxes;

    //     standardisedPackager(double[][] inputsStandardised, double[] mins, double[] maxes) {
    //         this.inputsStandardised = inputsStandardised;
    //         this.mins = mins;
    //         this.maxes = maxes;
    //     }
    // }

    public static void main(String[] args) throws FileNotFoundException {
        // instantiate readingfromexternal to get data
        readingfromexternal externalData = new readingfromexternal();
        // instantiate rearranging
        rearranging dataPrep = new rearranging();
        // delete the dates at the beginning of each row
        List<List<String>> deleteddates = dataPrep.deleteDates();
        // casts the values from List<List<String>> to double
        double[][] cast = dataPrep.castingToDouble(deleteddates);
        // eliminates outliers, non-numerical, and negative values
        double[][] cleanedData = dataPrep.eliminateOutliers(cast);
        // make the output the last item in each row
        // this made it a lot easier to work with
        double[][] repositionedArray = dataPrep.repositionOutputToEnd(cleanedData, 3); 
        // data restructured such that the next day's flow at skelton is the observed answer for each row of data
        double[][] outputRepositionedFromNextDayArray = dataPrep
                .getOneDayAheadOutputInTheRawAsOutput(repositionedArray);
        // shuffle all values so that they can be split properly, without seasonal affections
        double[][] shuffledArray = dataPrep.shuffleArray(outputRepositionedFromNextDayArray);
        // 
       // standardisedPackager standardizedPack = new standardisedPackager(shuffledArray, {9}, {8});
        // standardisedPackager standardizedPack = standardiseInputs(shuffledArray);
        // double[][] testinputs = { { 1, 9, 5, 7, 8, 0, 2, 1 } };
        // backpropagation test = new backpropagation();
        // trainingResults readyfortesting = test.backpropTraining(testinputs, 4, 1000,
        // true, false);
        // test.testing(testinputs, readyfortesting, true);
    }
}