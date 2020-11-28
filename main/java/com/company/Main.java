package com.company;

import java.util.*;

public class Main {

    enum SortTypes {
        INCR(Integer::compareTo),
        DECR((a, b) -> { return b.compareTo(a);}),
        INCR_BY_DIGITS((a, b) -> { return countDigits(a).compareTo(countDigits(b));}),
        DECR_BY_DIGITS((a, b) -> { return countDigits(b).compareTo(countDigits(a));});

        Comparator<Integer> comparator;

        SortTypes(Comparator<Integer> comp) {
            comparator = comp;
        }

        public Comparator<Integer> getComparator() {
            return comparator;
        }

        public static SortTypes fromIntValue(int value) {
            for (SortTypes type : SortTypes.values()) {
                if (type.ordinal() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("no such enum constant with value = " + value);
        }
    }

    public static void main(String[] args) {
        int n = 0;
        SortTypes sortingType = SortTypes.INCR;
        List<Integer> array = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter size of array");
            n = scanner.nextInt();
            System.out.println("Enter type of sort(\n" +
                    "0 - increasing,\n" +
                    "1 - decreasing,\n" +
                    "2 - increasing number of digits\n" +
                    "3 - decreasing number of digits):");
            try {
                sortingType = SortTypes.fromIntValue(scanner.nextInt());
            } catch (IllegalArgumentException ex) {
                System.out.println("wrong type. Default type(increasing) will be used");
                sortingType = SortTypes.INCR;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < n; ++i) {
            array.add((int) (Math.random() * 10000));
        }
        System.out.println("Starting sort:");
	    SortThread th = new SortThread(array, sortingType);
        System.out.print("Sorting: [");
        double loading = 0;
        double delta = 1. / (Math.pow(n, 0.2) * Math.log(Math.pow(n, 0.2)));
        while (th.isAlive()) {
            loading += loading + delta < 1 ? delta : 0;
            System.out.print("*".repeat((int)(loading * 100)) +
                     " ".repeat(100 - (int)(loading * 100)) +
                     "]" + (int)(loading * 100) + "%");
            try {
                Thread.sleep(20);
            } catch(Exception ignored) {}
            System.out.print("\b".repeat(((int) (loading * 100) / 10 > 0 ? 104 : 103)));
        }
        for (int i = 0; i < 100; ++i) {
            System.out.print("*");
        }
        System.out.print("]" + 100 + "%");
        try {
            Thread.sleep(200);
        } catch (Exception ignored) {}

        for (Integer x : array) {
            System.out.print(x + " ");
        }
    }

    static class SortThread extends Thread {
        private final List<Integer> array;
        private final Comparator<Integer> comparator;

        public SortThread(List<Integer> array, SortTypes sortType) {
            super("SortThread");
            this.comparator = sortType.getComparator();
            this.array = array;
            this.start();
        }

        @Override
        public void run() {
            array.sort(comparator);
        }
    }

    private static Integer countDigits(Integer a) {
        if (a.equals(0)) {
            return 1;
        }
        int digits = 0;
        while (a > 0) {
            digits++;
            a /= 10;
        }
        return digits;
    }
}
