package io.github.zhaomy6;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        PuzzleState initState = new PuzzleState();
        initState.rearrange();
        System.out.println(">> Initial State:");
        initState.printPuzzleState();

        ArrayList<PuzzleState> path = PuzzleState.aStarSearch(initState);
        if (path == null) {
            System.out.println(">> No Path Found");
            return;
        }
        System.out.println(">> Find Path:");
        for (PuzzleState ele : path) {
            ele.printPuzzleState();
        }
        System.out.println("Path Length =" + path.size());
    }
}
