class backpropexample {
    public void backProp(double[] inputs) {
        int epochCounter = 0;
        double p = 0.1;
        double[][] inputToHiddenWeights = { { 3, 6 }, { 4, 5}};  
        //double[][] inputToHiddenWeights = { { 3, 6 }, { 4, 5} };
        double[] hiddenToOutputWeights = {2, 4};
        double[] hiddenLayerBiases = { 1, -6 };
        double[] outputBiases = {-3.92};
        double[] hiddenLayerWeightedSums = { 0, 0 };
        double[] hiddenLayerActivation = { 0, 0 };
        double[] outputLayerWeightedSums = {0};
        double[] outputsActivation = {0};
        double[] greekValuesHidden = {0, 0};
        double[] greekValueOutput = {0};
        while (epochCounter < 20000){
        //forward pass
        for (int i = 0; i < hiddenLayerWeightedSums.length; i++) {
            hiddenLayerWeightedSums[i] = 0;
            for (int j = 0; j < inputs.length - 1; j++){
                hiddenLayerWeightedSums[i] += inputs[j] * inputToHiddenWeights[j][i];
            }
            hiddenLayerWeightedSums[i] += hiddenLayerBiases[i];
            hiddenLayerActivation[i] = 1 / (1 + Math.exp(-hiddenLayerWeightedSums[i]));
            //System.out.println(hiddenLayerActivation[i]);
        }
        for (int i = 0; i < outputLayerWeightedSums.length; i++){
            outputLayerWeightedSums[i] = 0;
            for (int j = 0; j < hiddenLayerWeightedSums.length; j++){
                outputLayerWeightedSums[i] += hiddenLayerActivation[j] * hiddenToOutputWeights[j];
            }
            outputLayerWeightedSums[i] += outputBiases[i];
            outputsActivation[i] = 1 / (1 + Math.exp(-outputLayerWeightedSums[i]));
            System.out.println("output: "+outputsActivation[i]);
        }
        //backwards pass
        for (int i = 0; i < outputsActivation.length; i++){
            greekValueOutput[i] = (inputs[inputs.length-1]-outputsActivation[i])*(outputsActivation[i] * (1-outputsActivation[i]));
            //System.out.println(greekValueOutput[i]);
            outputBiases[i] += p * greekValueOutput[i];
            //System.out.println("new output bias: "+outputBiases[i]);

        }    
        for (int i = 0; i < hiddenLayerActivation.length; i++){
            greekValuesHidden[i] = (hiddenToOutputWeights[i]* greekValueOutput[0]) *
            (hiddenLayerActivation[i] * (1-hiddenLayerActivation[i]));
            //System.out.println(greekValuesHidden[i]);
            //CHANGE HIDDEN TO OUTPUT WEIGHTS
            // System.out.println("old weight: "+hiddenToOutputWeights[i]);
            hiddenToOutputWeights[i] += p * greekValueOutput[0] * hiddenLayerActivation[i];
            // System.out.println("new weight: "+hiddenToOutputWeights[i]);
            hiddenLayerBiases[i] += p * greekValuesHidden[i];
            //System.out.println("new hidden layer bias: "+hiddenLayerBiases[i]);
        }    
        for (int i = 0; i < inputToHiddenWeights.length; i++){
            for (int j = 0; j < inputToHiddenWeights.length; j++){
                //System.out.println("previous weight:");
                //System.out.println(inputToHiddenWeights[i][j]);
                inputToHiddenWeights[i][j] += p * greekValuesHidden[j] * inputs[i];
               //System.out.println("new weight:");
                //System.out.println(inputToHiddenWeights[i][j]);
            }
        }
        epochCounter++;
    }

    }
    public static void main(String[] args) {
        double[] testinputs = { 1, 0, 1 };
        backpropexample test = new backpropexample();
        test.backProp(testinputs);
    }
}