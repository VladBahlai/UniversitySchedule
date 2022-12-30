package com.github.vladbahlai.university.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.github.vladbahlai.university.utils.Values.FIRST_NAMES;
import static com.github.vladbahlai.university.utils.Values.LAST_NAMES;

public class RandomGenerator {

    public static Set<String> generateFullName(int count) {
        Set<String> names = new HashSet<>();
        while (names.size() != count) {
            names.add(getRandomArrayValue(FIRST_NAMES) +
                    " " + getRandomArrayValue(LAST_NAMES));
        }
        return names;
    }

    public static Set<String> generateName(int count, String[] firstArray, String[] secondArray) {
        Set<String> names = new HashSet<>();
        while (names.size() != count) {
            if (getRandomBoolean()) {
                names.add(getRandomArrayValue(firstArray));
            } else {
                names.add(getRandomArrayValue(secondArray));
            }
        }
        return names;
    }

    public static Set<String> generateName(int count, String[] firstArray) {
        Set<String> names = new HashSet<>();
        while (names.size() != count) {
            names.add(getRandomArrayValue(firstArray));
        }
        return names;
    }

    public static Set<String> generateName(int count, int start, int end) {
        Set<String> names = new HashSet<>();
        while (names.size() != count) {
            names.add(String.valueOf(RandomGenerator.getRandomInt(start, end)));
        }
        return names;
    }

    public static String generatePassword(int length) {
        return ThreadLocalRandom.current()
                .ints(length, 33, 122)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static int getRandomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    public static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static double getRandomDouble(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end) / 10.0;
    }

    public static char[] randomChar(char[] array) {
        return new char[]{array[ThreadLocalRandom.current().nextInt(array.length)], array[ThreadLocalRandom.current().nextInt(array.length)]};
    }

    public static <T> T getRandomListValue(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static String getRandomArrayValue(String[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

}
