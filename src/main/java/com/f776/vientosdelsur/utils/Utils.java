package com.f776.vientosdelsur.utils;

import java.util.List;
import java.util.Random;

public class Utils {

    private static final Random SEED = new Random();

    public static <T> T pickRandom(List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be null or empty");
        }
        return collection.get(SEED.nextInt(collection.size()));
    }

    public static <T> List<T> pickRandomRange(List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be null or empty");
        }

        int fromIndex = SEED.nextInt(collection.size());
        int toIndex = fromIndex + SEED.nextInt(collection.size() - fromIndex);
        return collection.subList(fromIndex, toIndex);
    }
}
