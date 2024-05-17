package ch.lukasweibel.powerlifting;

import ai.djl.*;
import ai.djl.inference.Predictor;
import ai.djl.nn.*;
import ai.djl.nn.core.*;
import ai.djl.training.*;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.metric.Metrics;
import ai.djl.pytorch.engine.PtModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PowerlifterAnalyse {

    Model model;

    private static PowerlifterAnalyse INSTANCE;

    private PowerlifterAnalyse() {
    }

    public void trainModel() {
        System.setProperty("ai.djl.default_engine", "PyTorch");
        System.setProperty("ai.djl.logging.level", "debug");
        Block block = new SequentialBlock()
                .add(Linear.builder().setUnits(128).build())
                .add(Activation.reluBlock())
                .add(Linear.builder().setUnits(64).build())
                .add(Activation.reluBlock())
                .add(Linear.builder().setUnits(1).build());

        model = Model.newInstance("squat-prediction");
        model.setBlock(block);

        DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.l2Loss())
                .addTrainingListeners(TrainingListener.Defaults.logging());

        Trainer trainer = model.newTrainer(config);

        Metrics metrics = new Metrics();
        trainer.setMetrics(metrics);

        int batchSize = 32;
        int numEpochs = 50;

        try (NDManager manager = NDManager.newBaseManager()) {
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get("./data/powerlifting.csv"));

                float[][] features = new float[lines.size() - 1][];
                float[] labels = new float[lines.size() - 1];

                for (int i = 1; i < lines.size(); i++) {
                    String[] tokens = lines.get(i).split(",");
                    float[] row = new float[tokens.length];

                    for (int j = 0; j < tokens.length; j++) {
                        row[j] = Float.parseFloat(tokens[j]);
                    }

                    features[i - 1] = Arrays.copyOf(row, row.length - 1);

                    labels[i - 1] = row[row.length - 1];
                }

                NDArray featureArray = manager.create(features);
                NDArray labelArray = manager.create(labels);
                System.out.println("Features size: " + featureArray.size());
                System.out.println("Labels size: " + labelArray.size());

                ArrayDataset dataset = new ArrayDataset.Builder()
                        .setData(featureArray)
                        .optLabels(labelArray)
                        .optDevice(Device.cpu())
                        .setSampling(batchSize, false)
                        .build();

                RandomAccessDataset[] datasets = dataset.randomSplit(8, 2);

                EasyTrain.fit(trainer, numEpochs, datasets[0], datasets[1]);

                TrainingResult result = trainer.getTrainingResult();
                model.setProperty("Epoch", String.valueOf(numEpochs));
                model.setProperty("MSE", String.format("%.5f", result.getValidateLoss()));

                String epochs = model.getProperty("Epoch");
                String mse = model.getProperty("MSE");

                System.out.println("Training completed:");
                System.out.println("Total Epochs: " + epochs);
                System.out.println("Mean Squared Error on Validation Set: " + mse);

                model.save(Paths.get("model"), "squat-prediction");

                if (model instanceof PtModel) {
                    System.out.println("It is a PT Model");
                } else {
                    System.out.println("It is no PT Model");
                }

                System.out.println("Saved Model!");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (TranslateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public double predict(float[] features) {
        if (model == null) {
            trainModel();
        }
        try (NDManager manager = NDManager.newBaseManager()) {

            NDList input = new NDList(manager.create(features).reshape(new Shape(1, features.length)));

            try (Predictor<NDList, NDList> predictor = model.newPredictor(new NoopTranslator())) {

                NDList output = predictor.predict(input);

                float prediction = output.singletonOrThrow().getFloat();
                return ((double) prediction);
            }
        } catch (Exception e) {
            System.err.println("Error during prediction: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static PowerlifterAnalyse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PowerlifterAnalyse();
        }
        return INSTANCE;
    }

}