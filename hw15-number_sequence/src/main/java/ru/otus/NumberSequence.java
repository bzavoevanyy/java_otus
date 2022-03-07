package ru.otus;


public class NumberSequence {
    private final static int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    private int currentThread = 2;

    public static void main(String[] args) {
        final var numberSequence = new NumberSequence();
        new Thread(() -> {
            for (int i = 0; i < arr.length; i++) {
                numberSequence.printInt(1, i);
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < arr.length; i++) {
                numberSequence.printInt(2, i);
            }
        }).start();
    }

    private synchronized void printInt(int inputThread, int arrayIndex) {
        try {
            while (currentThread == inputThread) {
                this.wait();
            }
            System.out.println(Thread.currentThread().getName() + " : " + arr[arrayIndex]);
            currentThread = inputThread;
            sleep();
            notifyAll();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
