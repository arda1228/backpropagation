import java.util.Random;

class test {
    public void backProp(double[][] inputs) {// 2 inputs, 1 hidden layer with 2 nodes, 1 output
        Random rd = new Random();
        System.out.println("starting weights:");
        int epochCounter = 0;
        double stepParameter = 0.01;
        // first array is all of the path weights from the inputs to the first hidden
        // node
        // second array is all of the path weights from the inputs to the second hidden
        // node
        double[] nodevalues = {0, 0};
        double[] actnodevalues = {0, 0};
        double[] actnodevaluesdiff = {0, 0};
        double[][] inputtohiddenweights = { { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 },
                { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 } };
        // path weights from hidden layer nodes to output
        double[] hiddentooutputweights = { 0.5, 0.5 };
        double[] hiddenbiases = { 0.5, 0.5 };
        double outputbias = 0.5; 
        double output = 0;
        double actoutput = 0;
        double outputdiff = 0;
        while (epochCounter < 500) {
            for (int i = 0; i < inputs.length; i++) {// forward pass
                // double weightedSum = inputs[i][0] * wZero + inputs[i][1] * wOne + inputs[i][2] * wTwo;
                // double activation = 1 / (1 + Math.exp(weightedSum));
                // double activationdiff = activation * (1 - activation);
                // double greekoutput = (inputs[i][3] - activation) * activationdiff;
                nodevalues[0] 
                = inputs[i][0] * inputtohiddenweights[0][0] 
                + inputs[i][1] * inputtohiddenweights[0][1] 
                + inputs[i][2] * inputtohiddenweights[0][2]
                + inputs[i][3] * inputtohiddenweights[0][3]
                + inputs[i][4] * inputtohiddenweights[0][4]
                + inputs[i][5] * inputtohiddenweights[0][5]
                + inputs[i][6] * inputtohiddenweights[0][6]
                + hiddenbiases[0];
                double firsthiddenactivation = 1 / (1 + Math.exp(nodevalues[0]));
                nodevalues[1] 
                = inputs[i][0] * inputtohiddenweights[1][0] 
                + inputs[i][1] * inputtohiddenweights[1][1] 
                + inputs[i][2] * inputtohiddenweights[1][2]
                + inputs[i][3] * inputtohiddenweights[1][3]
                + inputs[i][4] * inputtohiddenweights[1][4]
                + inputs[i][5] * inputtohiddenweights[1][5]
                + inputs[i][6] * inputtohiddenweights[1][6]
                + hiddenbiases[1];
                double secondhiddenactivation = 1 / (1 + Math.exp(nodevalues[1]));
                output = nodevalues[0] * hiddentooutputweights[0]
                + nodevalues[1] * hiddentooutputweights[1] + outputbias;
                //MAKE FUNCTIONS FOR APPLYING FUNCTIONS 
                actnodevalues[0] = 1 / (1 + Math.exp(actnodevalues[0]));
                actnodevalues[1] = 1 / (1 + Math.exp(actnodevalues[1]));
                actoutput = 1 / (1 + Math.exp(output));

                // backward pass
                //differential of sigmoid

                outputdiff = output * (1 - output);
                actnodevaluesdiff[0] = actnodevaluesdiff[0] * (1 - actnodevaluesdiff[0]);
                actnodevaluesdiff[1] = actnodevaluesdiff[1] * (1 - actnodevaluesdiff[1]);
                // WEIRD SIGMA VALUE AND PART 4 BELOW
            }
            epochCounter++;
        }
        System.out.print("training complete. number of epochs: ");
        System.out.println(epochCounter);
        System.out.println("final weights:");
    }

    public static void main(String[] args) {
        double[][] testinputs = { { 10.4, 4.393, 9.291, 0, 0, 0, 4, 24.86 },
                { 9.95, 4.239, 8.622, 0, 0, 0.8, 0, 23.6 } };
        // double[][] inputs = { { 1, 1, 4, -1 }, { 1, 2, 9, 1 }, { 1, 5, 6, 1 }, { 1,
        // 4, 5, 1 }, { 1, 6, 0.7, -1 },
        // { 1, 1, 1.5, -1 } };
        test test = new test();
        test.backProp(testinputs);
    }
}

// Take next training example E with correct output C
// 2. Make a forward pass through the network computing
// weighted sums, Sj
// , and activations uj
// =f(Sj
// ) for every node
// 3. Backward pass computing, for each node j:
// f’(Sj) = uj(1-uj)
// 4. Update the weights: 𝑤𝑖,𝑗∗ = 𝑤𝑖,𝑗+ ρ𝛿𝑗ui

// mse =
// (∑(𝑂 − 𝑀)2)/n

// differential of sigmoid function
// f '(x) = f (x)(1− f (x))

// slide 22
// 𝑤𝑖,𝑗
// ∗ = 𝑤𝑖,𝑗+ ρ𝛿𝑗ui

//FUNCTION FOR CREATING ARRAYS OF INPUTS AND HIDDEN LAYERS AND OUTPUTS