package ru.gb;

import java.util.Arrays;

public class ArrayTimer {
    private static final int SIZE = 10000000;
    private static final int HALFSIZE = SIZE / 2;

    public float[] calc(float[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = (float) (array[i] * Math.sin(0.2f + array[i] / 5) * Math.cos(0.2f + array[i] / 5) * Math.cos(0.4f + array[i] / 2));
        return array;
    }

    public void oneThread() {
        float[] array = new float[SIZE];
        Arrays.fill(array, 1.0f);
        long startTime = System.currentTimeMillis();
        calc(array);
        System.out.println("Один поток. Время выполнения: " + (System.currentTimeMillis() - startTime));
    }

    public void doubleThread() throws InterruptedException {
        float[] array = new float[SIZE];
        final float[] arrayFirst = new float[HALFSIZE];
        final float[] arraySecond = new float[HALFSIZE];
        Arrays.fill(array, 1.0f); // Idea сама предложила поменять цикл на метод для заполнения массива.
        long startTime = System.currentTimeMillis();
        System.arraycopy(array, 0, arrayFirst, 0, HALFSIZE);
        System.arraycopy(array, HALFSIZE, arraySecond, 0, HALFSIZE);

        Thread thread1 = new Thread() {
            public void run() {
                float[] a1 = calc(arrayFirst);
                System.arraycopy(a1, 0, arrayFirst, 0, arrayFirst.length);
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                float[] a2 = calc(arraySecond);
                System.arraycopy(a2, 0, arraySecond, 0, arraySecond.length);
            }
        };

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.arraycopy(arrayFirst, 0, array, 0, HALFSIZE);
        System.arraycopy(arraySecond, 0, array, HALFSIZE, HALFSIZE);

        System.out.println("Два потока. Время выполнения: " + (System.currentTimeMillis() - startTime));

    }

    public static void main(String[] args) throws InterruptedException {
        ArrayTimer at = new ArrayTimer();
        at.oneThread();
        at.doubleThread();
    }
}
