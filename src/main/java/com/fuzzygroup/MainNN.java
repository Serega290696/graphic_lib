package com.fuzzygroup;

import com.googlecode.fannj.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialArray;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.HopfieldLearning;
import org.neuroph.util.TransferFunctionType;

public class MainNN {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        NeuralNetwork<HopfieldLearning> nNetwork = new Perceptron(1, 1);

        DataSet trainingSet =
                new DataSet(1, 1);

        for (int i = 0; i < 2; i++) {
            double x = Math.random() * 10;
            double y = Math.random() * 10;
            double z = Math.random() * 10;
            trainingSet.addRow(new double[]{y}, new double[]{y * y});
        }
//        trainingSet.addRow(new DataSetRow(new double[]{0, 0},
//                new double[]{0}));
//        trainingSet.addRow(new DataSetRow(new double[]{0, 1},
//                new double[]{1}));
//        trainingSet.addRow(new DataSetRow(new double[]{1, 0},
//                new double[]{1}));
//        trainingSet.addRow(new DataSetRow(new double[]{1, 1},
//                new double[]{1}));
        // learn the training set
        nNetwork.learn(trainingSet);
        // save the trained network into file
        nNetwork.save("or_perceptron.nnet");
        System.out.println("end");

        // set network input
        nNetwork.setInput(2, 4);
        // calculate network
        nNetwork.calculate();
        // get network output
        double[] networkOutput = nNetwork.getOutput();

        for (double i : networkOutput)
            System.out.println(i);

    }

    private static void learn() {
        //Для сборки новой ИНС необходимо создасть список слоев
        List<Layer> layerList = new ArrayList<Layer>();
        layerList.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(16, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(4, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        Fann fann = new Fann(layerList);
        //Создаем тренера и определяем алгоритм обучения
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
        /* Проведем обучение взяв уроки из файла, с максимальным колличеством
           циклов 100000, показывая отчет каждую 100ю итерацию и добиваемся
        ошибки меньше 0.0001 */
        trainer.train(new File("train.data").getAbsolutePath(), 100000, 100, 0.0001f);
        fann.save("ann");
    }

//    public static void main(String[] args) {
//        learn();
//        test();
//    }

    private static void test() {
        Fann fann = new Fann("ann");
        float[][] tests = {
                {1.0f, 0, 1},
                {0.9f, 1, 3},
                {0.3f, 0, 8},
                {1, 1, 8},
                {0.1f, 0, 0},
        };
        for (float[] test : tests) {
            System.out.println(getAction(fann.run(test)));
        }
    }

    private static String getAction(float[] out) {
        int i = 0;
        for (int j = 1; j < 4; j++) {
            if (out[i] < out[j]) {
                i = j;
            }
        }
        switch (i) {
            case 0:
                return "атаковать";
            case 1:
                return "прятаться";
            case 2:
                return "бежать";
            case 3:
                return "ничего не делать";
        }
        return "";
    }
}
