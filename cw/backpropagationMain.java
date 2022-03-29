import java.io.FileNotFoundException;// throws error if file isn't found
import java.util.Random; // allows selection of random numbers to find global minima in weight space 
import java.util.List; // data structure representing ordered sequence of objects

class backpropagationMain {

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

    public double deltaHidden(double weight, double nextdelta, double value, boolean Sigmoid) {
        // gives the delta value of a given hidden node
        // needed for backwards passing to allow changing of weights
        // gets a different differential based on the activation function
        if (Sigmoid) {
            return weight * nextdelta * sigmoidActivationDiff(value);
        } else {
            return weight * nextdelta * tanhActivationDiff(value);

        }
    }

    class trainingResults {// used as the return type for the training function
        // weights from input nodes to hidden layer
        double[][] inputToHiddenWeights; // [i][j], where i = input value and j = hidden layer node

        // weights from hidden layer nodes to the output node
        double[] hiddenToOutputWeights; // [i], where i = hidden node value

        // biases on hidden layer
        double[] hiddenLayerBiases;// [i], where i = index of the hidden node within the layer

        // bias on output layer
        double[] outputBiases; // [i], where i = index of the output node within the layer

        // number of hidden nodes
        int numberOfHiddenNodes;

        trainingResults(double[][] inputToHiddenWeights, double[] hiddenToOutputWeights, double[] hiddenLayerBiases,
                double[] outputBiases, int numberOfHiddenNodes) {// constructor
            this.inputToHiddenWeights = inputToHiddenWeights;
            this.hiddenToOutputWeights = hiddenToOutputWeights;
            this.hiddenLayerBiases = hiddenLayerBiases;
            this.outputBiases = outputBiases;
            this.numberOfHiddenNodes = numberOfHiddenNodes;
        }
    }

    // MAIN TRAINING FUNCTION
    public trainingResults backpropTraining(double[][] inputs, double learningParameter, int NumberOfHiddenNodes,
            int epochs, boolean Sigmoid, boolean momentum, double Alpha) {
        Random rand = new Random(67); // instance of random class
        int epochCounter = 0;// updated each epoch
        double p = learningParameter;// learning parameter
        double prevWeight; // for momentum
        double alpha = Alpha; // for momentum
        int NoOfInputs = inputs[0].length - 1;// records number of inputs

        // each array contains the weights from a given input node
        double[][] inputToHiddenWeights = new double[NoOfInputs][NumberOfHiddenNodes];

        // each array contains the last change in weights from a given input node
        double[][] changeInInputToHiddenWeights = new double[NoOfInputs][NumberOfHiddenNodes];

        for (int i = 0; i < NoOfInputs; i++) {// iterating through inputs
            for (int j = 0; j < NumberOfHiddenNodes; j++) {// randomly assigning initial weights
                inputToHiddenWeights[i][j] = rand.nextDouble();
            }
        }

        // each index contains the weight from a given hidden layer node
        double[] hiddenToOutputWeights = new double[NumberOfHiddenNodes];

        double[] changeInHiddenToOutputWeights = new double[NumberOfHiddenNodes]; // initialising array
        for (int i = 0; i < NumberOfHiddenNodes; i++) { // randomly assigning initial weights
            hiddenToOutputWeights[i] = rand.nextDouble();
        }

        double[] hiddenLayerBiases = new double[NumberOfHiddenNodes];// initialising array
        double[] changeInHiddenLayerBiases = new double[NumberOfHiddenNodes];// initialising array

        for (int i = 0; i < NumberOfHiddenNodes; i++) {
            hiddenLayerBiases[i] = rand.nextDouble(); // randomly assigning initial weights
        }

        double[] outputBiases = { rand.nextDouble() }; // randomly assigning initial weights
        double[] changeInOutputBiases = { 0 }; // assigning initial weights

        // initialising array sizes by the amount of nodes in their respective layers
        double[] hiddenLayerWeightedSums = new double[NumberOfHiddenNodes];
        double[] hiddenLayerActivation = new double[NumberOfHiddenNodes];
        double[] outputLayerWeightedSums = new double[1];
        double[] outputsActivation = new double[1];
        double[] deltaValuesHidden = new double[NumberOfHiddenNodes];
        double[] deltaValueOutput = new double[1];

        // forward pass through all data by the inputted number of epochs
        while (epochCounter < epochs) {
            // iterates through all rows from inputted data
            for (int k = 0; k < inputs.length; k++) {
                // iterating through all hidden layer nodes and declaring their initial values
                for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
                    hiddenLayerWeightedSums[i] = 0;
                    for (int j = 0; j < inputs[0].length - 1; j++) {
                        // iterating through all input values and creating weighted sums for the hidden
                        // layer nodes
                        hiddenLayerWeightedSums[i] += inputs[k][j] * inputToHiddenWeights[j][i];
                    }
                    // adding biases to hidden layer weighted sums
                    hiddenLayerWeightedSums[i] += hiddenLayerBiases[i];
                    // depending on the selection in the method's arguments, an activation function
                    // is selected
                    if (Sigmoid) {
                        hiddenLayerActivation[i] = this.sigmoidActivation(hiddenLayerWeightedSums[i]);
                    } else {
                        hiddenLayerActivation[i] = this.tanhActivation(hiddenLayerWeightedSums[i]);
                    }
                }
                // finding the output's weighted sum
                for (int i = 0; i < outputLayerWeightedSums.length; i++) {
                    outputLayerWeightedSums[i] = 0;
                    for (int j = 0; j < hiddenLayerWeightedSums.length; j++) {
                        // adds the weights and values from the hidden layer
                        outputLayerWeightedSums[i] += hiddenLayerActivation[j] * hiddenToOutputWeights[j];
                    }
                    // adding the bias
                    outputLayerWeightedSums[i] += outputBiases[i];
                    // depending on the selection in the method's arguments, an activation function
                    // is selected

                    if (Sigmoid) {
                        outputsActivation[i] = this.sigmoidActivation(outputLayerWeightedSums[i]);
                    } else {
                        outputsActivation[i] = this.tanhActivation(outputLayerWeightedSums[i]);
                    }
                }
                // backwards pass, changing weights
                // starting with output node
                for (int i = 0; i < outputsActivation.length; i++) {
                    // finding delta value of output node
                    deltaValueOutput[i] = (inputs[k][inputs[k].length - 1] - outputsActivation[i])
                            * (outputsActivation[i] * (1 - outputsActivation[i]));
                    prevWeight = outputBiases[i];
                    outputBiases[i] += p * deltaValueOutput[i];
                    changeInOutputBiases[i] = outputBiases[i] - prevWeight;
                    // saving previous weight and change in weights for momentum if selected
                    if (momentum) {
                        outputBiases[i] += (alpha * changeInOutputBiases[i]);
                    }
                }
                // changing path weights from hidden nodes
                for (int i = 0; i < hiddenLayerActivation.length; i++) {
                    // calling function to find delta of a given hidden node, saving to array
                    deltaValuesHidden[i] = this.deltaHidden(hiddenToOutputWeights[i], deltaValueOutput[0],
                            hiddenLayerActivation[i], Sigmoid);

                    // saving previous weight and change in weights for momentum if selected
                    prevWeight = hiddenToOutputWeights[i];

                    // changing weights
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
                // changing path weights from input nodes
                for (int i = 0; i < inputToHiddenWeights.length; i++) {// iterate through inputs
                    for (int j = 0; j < NumberOfHiddenNodes; j++) {// iterate through hidden nodes
                        // saving previous weight and change in weights for momentum if selected
                        prevWeight = inputToHiddenWeights[i][j];
                        // change weight
                        inputToHiddenWeights[i][j] += p * deltaValuesHidden[j] * inputs[k][i];
                        changeInInputToHiddenWeights[i][j] = inputToHiddenWeights[i][j] - prevWeight;
                        if (momentum) {
                            inputToHiddenWeights[i][j] += (alpha * changeInInputToHiddenWeights[i][j]);
                        }
                    }
                }
            }
            epochCounter++;// one pass through the data has been completed
        }
        // final weights have been found, so a new trainingResults object is constructed
        // using them
        // ready for testing
        return new trainingResults(inputToHiddenWeights, hiddenToOutputWeights, hiddenLayerBiases, outputBiases,
                NumberOfHiddenNodes);
    }

    // class that is returned from testing function, allows to use results for
    // analysis
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

    // testing function
    // takes test set and weights as inputs
    public testingResults testing(double[][] testSet, trainingResults results, double[] mins, double[] maxes, boolean Sigmoid) {
        dataPreprocessing tester = new dataPreprocessing();
        // declaring array lengths and variables
        double[] destandardisedObservedOutputs = new double[testSet.length];
        double[] destandardisedModelledOutputs = new double[testSet.length];
        double totalSquaredError = 0;// needed for mse later
        double meanSquaredError;
        double[] hiddenLayerWeightedSums = new double[results.numberOfHiddenNodes];
        double[] hiddenLayerActivation = new double[results.numberOfHiddenNodes];
        double[] outputLayerWeightedSums = new double[1];
        double outputsActivation;
        for (int k = 0; k < testSet.length; k++) {// iterate through every row in test set
            // same as training but forward pass only
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
                System.out.println("modelled(standardised): " + outputsActivation);
                System.out.println("modelled(destandardised): " + tester.destandardisedValue(outputsActivation, mins[7], maxes[7]));
                destandardisedModelledOutputs[k] = tester.destandardisedValue(outputsActivation, mins[7], maxes[7]);

                System.out.println("observed(standardised): " + testSet[k][testSet[k].length - 1]);
                System.out.println("observed(destandardised): " + tester.destandardisedValue(testSet[k][testSet[k].length - 1], mins[7], maxes[7]));
                destandardisedObservedOutputs[k] = tester.destandardisedValue(testSet[k][testSet[k].length - 1],
                        mins[7], maxes[7]);
                // incrementing total squared error between activated modelled and observed
                // values
                totalSquaredError += Math.pow(testSet[k][testSet[k].length - 1] - outputsActivation, 2);
            }
        }

        meanSquaredError = totalSquaredError / testSet.length;// final mse calculation
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
        // standardise all values in range [0.1,0.9], and return mins and maxes for
        // destandardisation
        dataPreprocessing.standardisedPackager standardizedPack = dataPrep.standardiseInputs(shuffledArray);
        // split into 60/20/20 for training, validation, and testing (attributes of
        // splitData)
        dataPreprocessing.dataSplitter splitData = dataPrep.splitData(standardizedPack.inputsStandardised, 0.6, 0.2,
                0.2);
        // instantiate backpropagation object to begin training and testing
        backpropagationMain test = new backpropagationMain();

        // creating .csv files to make analysis graphs
        // declare new fileOperations object to make new files with
        fileOperations fileOps = new fileOperations();
        // independent counter stands for the independent variable that will be changed
        // to form the x-axis of the graph, in the case where y is mean squared error
        // in this example, i did not use it as i had to find observed vs modelled values
        // but it can be used to replace any continuous value in the arguments for backpropTraining (line 332)
        int IndependentCounterStart = 0;
        int IndependentCounterEnd = 0;
        int IndependentCounterStep = 1;
        // declaring size of result arrays - must be the difference between start and end
        // divided by step size
        int arraySize = (int) Math.floor((IndependentCounterEnd - IndependentCounterStart) / IndependentCounterStep)
                + 1;
        double[] IndependentCountArrayForGraph = new double[splitData.testSet.length];
        double[] mseArrayForGraph = new double[splitData.testSet.length];
        String[] merged = new String[splitData.testSet.length];
        int indexForGraph = 0;
        // use createUniqueIdentifier to automatically record a unique filename prefix
        String fileName;
        fileName = fileOps.createUniqueIdentifier();
        for (int IndependentCounter = IndependentCounterStart; IndependentCounter <= IndependentCounterEnd; IndependentCounter += IndependentCounterStep) {
            // train the weights using 60% of the shuffled standardised values
            trainingResults readyfortesting = test.backpropTraining(splitData.trainingSet, 0.1, 11, 4000,
                    true, true, 0.05);
            // (double[][] inputs, double learningParameter, int NumberOfHiddenNodes,
            // int epochs, boolean Sigmoid, boolean momentum, double Alpha)

            // test the weights using the test set and find the mean squared error
            testingResults tested = test.testing(splitData.testSet, readyfortesting, standardizedPack.mins, standardizedPack.maxes, true);
            System.out.println("modelled at " + IndependentCounter + ":\n" + tested.destandardisedModelledOutputs[23]);
            IndependentCountArrayForGraph = tested.destandardisedObservedOutputs;
            mseArrayForGraph = tested.destandardisedModelledOutputs;
            indexForGraph++;
        }
        indexForGraph = 0;
        // merge the epochCounter and mse values into an array as this is an easier
        merged = fileOps.mergeTwoArraysAsIsAndReturnAsStringArray(IndependentCountArrayForGraph, mseArrayForGraph);
        // update filename so that file created will have key configuration data in its
        // name
        fileName += ".csv";
        // create a new csv file with the modelled and observed values, so they can be
        // made into a graph in excel
        fileOps.createFile(fileName);
        fileOps.writeArrayToFileAsLines(merged, fileName);
    }
}
