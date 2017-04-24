package io.github.zhaomy6;

import java.util.ArrayList;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
//        testEightQueensProblem();
        testEightPuzzleProblem();
    }

    public static void testEightPuzzleProblem() {
        final int CASES_NUM = 2000;
        //  Steepest-Ascent
        int counter = 0;
        long start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            ArrayList<PuzzleState> path = Puzzle.steepestAscentSolve();
            if (path != null) {
                ++counter;
            }
        }
        long last = Calendar.getInstance().getTimeInMillis();
        double timeCost = (last - start) / 1000.0;
        System.out.println(">> 8-Puzzle: Steepest-Ascent <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");


        //  First-Choice
        counter = 0;
        start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            ArrayList<PuzzleState> path = Puzzle.firstChoiceSolve();
            if (path != null) {
                ++counter;
            }
        }
        last = Calendar.getInstance().getTimeInMillis();
        timeCost = (last - start) / 1000.0;
        System.out.println(">> 8-Puzzle: First-Choice <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");

        //  Simulated-Annealing
        counter = 0;
        start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            ArrayList<PuzzleState> path = Puzzle.simulatedAnnealingSolve();
            if (path != null) {
                ++counter;
            }
        }
        last = Calendar.getInstance().getTimeInMillis();
        timeCost = (last - start) / 1000.0;
        System.out.println(">> 8-Puzzle: Simulated-Annealing <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");

        //  A*算法
        counter = 0;
        start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            ArrayList<PuzzleState> path = Puzzle.aStarSolve();
            if (path != null) {
                ++counter;
            }
        }
        last = Calendar.getInstance().getTimeInMillis();
        timeCost = (last - start) / 1000.0;
        System.out.println(">> 8-Puzzle: A-Star <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");
    }

    public static void testEightQueensProblem() {
        final int CASES_NUM = 2000;
        //  steepest-ascent
        int counter = 0;
        long start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            QueensState result = EightQueens.steepestAscentSolve();
            if (result != null) {
                ++counter;
            }
        }
        long last = Calendar.getInstance().getTimeInMillis();
        double timeCost = (last - start) / 1000.0;
        System.out.println(">> Steepest-Ascent <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");

        //  first-choice
        counter = 0;
        start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            QueensState result = EightQueens.firstChoiceSolve();
            if (result != null) {
                ++counter;
            }
        }
        last = Calendar.getInstance().getTimeInMillis();
        timeCost = (last - start) / 1000.0;
        System.out.println(">> First-Choice <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");

        //  random-restart
        counter = 0;
        start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            QueensState result = EightQueens.randomRestartSolve();
            if (result != null) {
                ++counter;
            }
        }
        last = Calendar.getInstance().getTimeInMillis();
        timeCost = (last - start) / 1000.0;
        System.out.println(">> Random-Restart <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");

        //  simulated annealing
        counter = 0;
        start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < CASES_NUM; ++i) {
            QueensState result = EightQueens.simulateAnnealingSolve();
            if (result != null) {
                ++counter;
            }
        }
        last = Calendar.getInstance().getTimeInMillis();
        timeCost = (last - start) / 1000.0;
        System.out.println(">> Simulated Annealing <<");
        System.out.println("Test Cases: " + CASES_NUM);
        System.out.printf("Solve Rate: %.3f\n", counter / (double) CASES_NUM);
        System.out.printf("Time Cost: %.3fs\n", timeCost);
        System.out.println(">> --------------- <<");
    }
}
