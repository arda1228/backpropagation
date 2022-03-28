import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;//adaptable-length array
import java.util.Arrays;
import java.util.List;

class backpropagationMain {
    // standardising inputs
    // read from text file
    // make functions to do calculations
    public double sigmoidActivation(double input) {// enter value, returns the sigmoid transfer 
        return 1 / (1 + Math.exp(-input));
    }

    public double sigmoidActivationDiff(double input) {// enter sigmoid-activated value, returns the differential
        return input * (1 - input);
    }

    public double tanhActivation(double input) {// enter value, returns the tanh transfer 
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
                    // System.out.println("output: " + outputsActivation[i]);
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

    class testingResults {
        double meanSquaredError;
        double[] destandardisedModelledOutputs;
        double[] destandardisedObservedOutputs;

        testingResults(double meanSquaredError, double[] destandardisedModelledOutputs,
                double[] destandardisedObservedOutputs) {
            this.meanSquaredError = meanSquaredError;
            this.destandardisedModelledOutputs = destandardisedModelledOutputs;
            this.destandardisedObservedOutputs = destandardisedObservedOutputs;
        }
    }

    public testingResults testing(double[][] testSet, trainingResults results, double[] mins, double[] maxes, boolean Sigmoid) {
        dataPreprocessing tester = new dataPreprocessing();
        double[] destandardisedObservedOutputs = new double[testSet.length];
        double[] destandardisedModelledOutputs = new double[testSet.length];
        double totalSquaredError = 0;
        double meanSquaredError;
        // int NoOfInputs = inputs[0].length - 1;
        double[] hiddenLayerWeightedSums = new double[results.numberOfHiddenNodes];
        double[] hiddenLayerActivation = new double[results.numberOfHiddenNodes];
        double[] outputLayerWeightedSums = new double[1];
        double outputsActivation;
        for (int k = 0; k < testSet.length; k++) {
            // forward pass
            for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
                hiddenLayerWeightedSums[i] = 0;
                for (int j = 0; j < testSet[0].length - 1; j++) {
                    hiddenLayerWeightedSums[i] += testSet[k][j] * results.inputToHiddenWeights[j][i];
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
                    outputsActivation = this.sigmoidActivation(outputLayerWeightedSums[i]);
                } else {
                    outputsActivation = this.tanhActivation(outputLayerWeightedSums[i]);
                }
                // System.out.println("modelled(standardised): " + outputsActivation);
                // System.out.println("modelled(destandardised): "
                // + tester.destandardisedValue(outputsActivation, mins[7], maxes[7]));

                destandardisedModelledOutputs[k] = tester.destandardisedValue(outputsActivation, mins[7], maxes[7]);

                // System.out.println("observed(standardised): " + testSet[k][testSet[k].length
                // - 1]);
                // System.out.println("observed(destandardised): "
                // + tester.destandardisedValue(testSet[k][testSet[k].length - 1], mins[7],
                // maxes[7]) + "\n");

                destandardisedObservedOutputs[k] = tester.destandardisedValue(testSet[k][testSet[k].length - 1],
                        mins[7], maxes[7]);

                // totalSquaredError += Math.pow(destandardisedObservedOutputs[k] -
                // destandardisedModelledOutputs[k], 2);
                totalSquaredError += Math.pow(testSet[k][testSet[k].length - 1] - outputsActivation, 2);
                // System.out.println("total squared error at value "+ k + ": " + totalSquaredError + "\n");
            }
        }

        meanSquaredError = totalSquaredError / testSet.length;
        // System.out.println("total squared error: " + totalSquaredError + "\n");

        // double meanError = Math.pow(meanSquaredError, 0.5);
        // System.out.println("mean squared error: " + meanSquaredError);
        // System.out.println("mean error: " + meanError);
        return new testingResults(meanSquaredError, destandardisedModelledOutputs, destandardisedObservedOutputs);
    }

    public static void main(String[] args) throws FileNotFoundException {
        dataPreprocessing dataPrep = new dataPreprocessing();
        // deleteDates will instantiate class dataPreprocessing within itself
        // in order to get the original data from a csv file I created containing the
        // raw values
        // delete the dates at the beginning of each row
        List<List<String>> deleteddates = dataPrep.deleteDates("arda.csv");
        // casts the values from List<List<String>> to double
        double[][] cast = dataPrep.castingToDouble(deleteddates);
        // eliminates outliers, non-numerical, and negative values
        double[][] cleanedData = dataPrep.eliminateOutliers(cast);
        // make the output the last item in each row
        // this made it a lot easier to work with
        double[][] repositionedArray = dataPrep.repositionOutputToEnd(cleanedData, 3);
        // data restructured such that the next day's flow at skelton is the observed
        // answer for each row of data
        double[][] outputRepositionedFromNextDayArray = dataPrep
                .getOneDayAheadOutputInTheRawAsOutput(repositionedArray);
        // shuffle all values so that they can be split properly, without seasonal
        // affections
        double[][] shuffledArray = dataPrep.shuffleArray(outputRepositionedFromNextDayArray);
        // standardise all values in range [0.1,0.9], and return mins and maxes for destandardisation
        dataPreprocessing.standardisedPackager standardizedPack = dataPrep.standardiseInputs(shuffledArray);
        // split into 60/20/20 for training, validation, and testing (attributes of
        // splitData)
        dataPreprocessing.dataSplitter splitData = dataPrep.splitData(standardizedPack.inputsStandardised, 0.6, 0.2,
                0.2);
        // instantiate backpropagation object to begin training and testing
        backpropagationMain test = new backpropagationMain();
        // train the weights using 60% of the shuffled standardised values
        // parameters: training set, hidden nodes, epochs, using Sigmoid transfer
        // function, momentum

        fileOperations fileOps = new fileOperations();
        int epochCounterStart = 1000;
        int epochCounterEnd = 10000;
        int epochCounterStep = 500;
        int arraySize = (int) Math.floor((epochCounterEnd - epochCounterStart) / epochCounterStep) + 1;
        double[] epochCountArrayForGraph = new double[arraySize];
        double[] mseArrayForGraph = new double[arraySize];
        String[] merged = new String[arraySize];
        int indexForGraph = 0;
        // use createUniqueIdentifier to automatically record a unique filename prefix
        String fileName;
        boolean Sigmoid = true;
        for (int i = 0; i < 1; i++) {
            fileName = fileOps.createUniqueIdentifier();
            // Sigmoid = !Sigmoid;
            for (int epochCounter = epochCounterStart; epochCounter < epochCounterEnd; epochCounter += epochCounterStep) {
                trainingResults readyfortesting = test.backpropTraining(splitData.trainingSet, 20, epochCounter, Sigmoid,
                        false);
                // test the weights using the test set and find the mean squared error
                testingResults tested = test.testing(splitData.testSet, readyfortesting, standardizedPack.mins,
                        standardizedPack.maxes, true);
                        // changed sigmoid in 311 from true
                System.out.println("epochCount" + epochCounter + "\nmse: " + tested.meanSquaredError);
                epochCountArrayForGraph[indexForGraph] = Double.valueOf(epochCounter);
                mseArrayForGraph[indexForGraph] = tested.meanSquaredError;
                indexForGraph++;
            }
            indexForGraph = 0;
            // merge the epochCounter and mse values into an array as this is an easier
            merged = fileOps.mergeTwoArraysAsIsAndReturnAsStringArray(epochCountArrayForGraph, mseArrayForGraph);
            // update filename so that file created will have key configuration data in its
            // name
            fileName += "epochStart" + Integer.toString(epochCounterStart) + "epochEnd" + Integer.toString(epochCounterEnd)
                    + "epochStep" + Integer.toString(epochCounterStep) + "sigmoid" + Boolean.toString(Sigmoid).toUpperCase() + ".csv";
            // create a new csv file with the modelled and observed values, so they can be
            // made into a graph in excel
            fileOps.createFile(fileName);
            fileOps.writeArrayToFileAsLines(merged, fileName);
        }
    }
}