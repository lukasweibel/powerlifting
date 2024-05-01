package ch.lukasweibel.powerlifting;

import ai.djl.translate.Translator;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslatorContext;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;

public class ArrayTranslator implements Translator<NDArray, NDArray> {

    @Override
    public NDList processInput(TranslatorContext ctx, NDArray input) {
        // Input is already an NDArray, so just return it
        return (NDList) input;
    }

    @Override
    public NDArray processOutput(TranslatorContext ctx, NDList list) {
        // Assume the output is also an NDArray and directly extract it
        return list.singletonOrThrow();
    }

    @Override
    public Batchifier getBatchifier() {
        // Use the default batchifier that DJL provides for NDArray processing
        return Batchifier.STACK;
    }
}
