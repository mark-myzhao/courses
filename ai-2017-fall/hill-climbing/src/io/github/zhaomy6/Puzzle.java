package io.github.zhaomy6;

import java.util.*;

public class Puzzle {
    //  steepest-ascent
    //  若找到一条路径，返回存有路径的ArrayList
    //  若找不到这样一条路径，返回null
    public static ArrayList<PuzzleState> steepestAscentSolve() {
        ArrayList<PuzzleState> curPath = new ArrayList<>();
        HashSet<Integer> hs = new HashSet<>();

        //  定义初始状态
        PuzzleState curState = new PuzzleState();
        for (int i = 0; i < 100; ++i) {
            curState.rearrange();
        }

        curPath.add(curState);
        hs.add(curState.manhattanDistance());
        while (true) {
            int min = curState.manhattanDistance();
            if (curState.isGoalState()) return curPath;
            ArrayList<PuzzleState> nextStates = curState.getNextStates(hs);

            ArrayList<Integer> ptrs = new ArrayList<>();
            for (int j = 0; j < nextStates.size(); ++j) {
                if (nextStates.get(j).manhattanDistance() < min) {
                    min = nextStates.get(j).manhattanDistance();
                    ptrs.add(j);
                }
            }
            if (ptrs.size() == 0) return null;

            //  从最小冲突的状态中选取一个作为下一个状态
            ArrayList<PuzzleState> choose = new ArrayList<>();
            for (int ele : ptrs) {
                if (nextStates.get(ele).manhattanDistance() == min) {
                    choose.add(nextStates.get(ele));
                }
            }
            if (choose.size() == 0) return null;
            Random rand = new Random();
            int select = Math.abs(rand.nextInt()) % choose.size();
            curState = choose.get(select);
            curPath.add(curState);
            hs.add(curState.manhattanDistance());
        }
    }

    //  first-choice 基本等同于上一个算法
    public static ArrayList<PuzzleState> firstChoiceSolve() {
        ArrayList<PuzzleState> curPath = new ArrayList<>();
        HashSet<Integer> hs = new HashSet<>();

        //  定义初始状态
        PuzzleState curState = new PuzzleState();
        for (int i = 0; i < 100; ++i) {
            curState.rearrange();
        }

        curPath.add(curState);
        hs.add(curState.manhattanDistance());
        while (true) {
            int min = curState.manhattanDistance();
            if (curState.isGoalState()) return curPath;
            ArrayList<PuzzleState> nextStates = curState.getNextStates(hs);

            boolean flag = false;
            for (int j = 0; j < nextStates.size(); ++j) {
                if (nextStates.get(j).manhattanDistance() < min) {
                    min = nextStates.get(j).manhattanDistance();
                    curState = nextStates.get(j);
                    hs.add(curState.manhattanDistance());
                    curPath.add(curState);
                    flag = true;
                    break;
                }
            }
            if (!flag) return null;
        }
    }

    //  Random-Restart
    //  由于初始状态不能更改，并不适合解决此类问题

    //  Simulated-Annealing
    public static ArrayList<PuzzleState> simulatedAnnealingSolve() {
        final double COLD_DOWN_RATE = 0.6;
        final double INIT_TMP = 30.0;
        long start = Calendar.getInstance().getTimeInMillis();
        //  TODO
        ArrayList<PuzzleState> curPath = new ArrayList<>();
        HashSet<Integer> hs = new HashSet<>();

        //  定义初始状态
        PuzzleState curState = new PuzzleState();
        for (int i = 0; i < 100; ++i) {
            curState.rearrange();
        }

        curPath.add(curState);
        hs.add(curState.manhattanDistance());
        while (true) {
            int min = curState.manhattanDistance();
            if (curState.isGoalState()) return curPath;
            ArrayList<PuzzleState> nextStates = curState.getNextStates(hs);
            if (nextStates.isEmpty()) return null;
            long now = Calendar.getInstance().getTimeInMillis();
            double T = INIT_TMP * Math.pow(COLD_DOWN_RATE, (now - start));
            if (T < 0.00001) {
                if (!curState.isGoalState()) {
                    return null;
                }
                return curPath;
            }
            Random rand = new Random();
            int select = Math.abs(rand.nextInt()) % nextStates.size();
            PuzzleState selectedState = nextStates.get(select);
            if (selectedState.manhattanDistance() < curState.manhattanDistance()) {
                hs.add(selectedState.manhattanDistance());
                curState = selectedState;
                curPath.add(curState);
            } else {
                int diff = curState.manhattanDistance() - selectedState.manhattanDistance();
                double prob = Math.exp(diff / T);
                if (Math.abs(rand.nextInt()) % 1000 < prob * 1000) {
                    hs.add(curState.manhattanDistance());
                    curState = selectedState;
                    curPath.add(curState);
                }
            }

        }
    }

    //  调用并测试A*算法
    public static ArrayList<PuzzleState> aStarSolve() {
        PuzzleState curState = new PuzzleState();
        for (int i = 0; i < 100; ++i) {
            curState.rearrange();
        }
        return aStarSearch(curState);
    }

    //  A* 算法，用于与登山法对比
    //  若找不到解，返回null
    public static ArrayList<PuzzleState> aStarSearch(PuzzleState initState) {
        PriorityQueue<ArrayList<PuzzleState> > pq = new PriorityQueue<>(new PuzzleComparator());
        ArrayList<PuzzleState> initPath = new ArrayList<>();
        HashSet<Integer> hs = new HashSet<>();
        //  initialize
        initPath.add(initState);
        pq.add(initPath);
        //  record procedure
        int counter = 0;
        while (!pq.isEmpty()) {
            ArrayList<PuzzleState> curStatePath = pq.poll();
            PuzzleState curState = curStatePath.get(curStatePath.size() - 1);

            //  System.out.println("Mes: Counter = " + counter);
            ++counter;
            //  judge if the search is ended
            if (curState.isGoalState()) {
                return curStatePath;
            }

            hs.add(curState.getHashValue());

            ArrayList<PuzzleState> nextStates = curState.getNextStates();
            for (PuzzleState ele : nextStates) {
                if (hs.contains(ele.getHashValue())) continue;
                ArrayList<PuzzleState> nextStatePath = new ArrayList<>();
                nextStatePath.addAll(curStatePath);
                nextStatePath.add(ele);
                pq.add(nextStatePath);
            }
        }
        return null;
    }
}

class PuzzleState {
    //  state[0] ~ state[8] are content in position i
    //  state[9] is the position of empty block
    private int[] state;

    //  size of the puzzle
    private final int PUZZLESIZE = 3;

    private final int RANDOMTIMES = 400;

    //  Constructor 1: generate Goal State
    public PuzzleState() {
        this.state = new int[PUZZLESIZE * PUZZLESIZE + 1];
        for (int i = 0; i < PUZZLESIZE * PUZZLESIZE; ++i) {
            this.state[i] = i;
        }
        this.state[PUZZLESIZE * PUZZLESIZE] = 0;
    }

    //  Constructor 2
    public PuzzleState(int[] initState) {
        this.state = initState.clone();
    }

    //  获取当前状态的所有下个状态
    public ArrayList<PuzzleState> getNextStates() {
        ArrayList<PuzzleState> nextStates = new ArrayList<>();
        if (state == null) {
            System.out.println("Err: state is not initialized");
            return nextStates;
        }
        int x = this.state[9] / PUZZLESIZE;
        int y = this.state[9] % PUZZLESIZE;
        int[] towardsX = new int[]{1, -1, 0, 0};
        int[] towardsY = new int[]{0, 0, 1, -1};
        for (int i = 0; i < 4; ++i) {
            if (x + towardsX[i] < PUZZLESIZE && x + towardsX[i] >= 0 &&
                    y + towardsY[i] < PUZZLESIZE && y + towardsY[i] >= 0) {
                int[] nextState = this.state.clone();
                int nextPos = (x + towardsX[i]) * PUZZLESIZE + (y + towardsY[i]);
                nextState[this.state[9]] = this.state[nextPos];
                nextState[nextPos] = 0;
                nextState[9] = nextPos;
                nextStates.add(new PuzzleState(nextState));
            }
        }
        return nextStates;
    }

    //  重载该函数，将hs中已经存在的状态从结果中剔除
    public ArrayList<PuzzleState> getNextStates(HashSet<Integer> hs) {
        ArrayList<PuzzleState> tmpList = this.getNextStates();
        ArrayList<PuzzleState> result = new ArrayList<>();
        for (PuzzleState ele : tmpList) {
            if (!hs.contains(ele.getHashValue())) {
                result.add(ele);
            }
        }
        return result;
    }

    //  Get current state
    public int[] getState() {
        return this.state;
    }

    //  Print current state as a puzzle
    public void printPuzzleState() {
        System.out.println(">> Print Current State");
        for (int x = 0; x < PUZZLESIZE; ++x) {
            for (int y = 0; y < PUZZLESIZE; ++y) {
                System.out.print(this.state[x * PUZZLESIZE + y] + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------");
    }

    //  return the manhattan distance between current state and final state
    public int manhattanDistance() {
        int dis = 0;
        for (int i = 0; i < 9; ++i) {
            int curX = getState()[i] / PUZZLESIZE;
            int curY = getState()[i] % PUZZLESIZE;
            int targetX = i / PUZZLESIZE;
            int targetY = i % PUZZLESIZE;
            dis += (Math.abs(curX - targetX) + Math.abs(curY - targetY));
        }
        return dis;
    }

    public boolean isGoalState() {
        int[] gs = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 0};
        for (int i = 0; i < PUZZLESIZE * PUZZLESIZE + 1; ++i) {
            if (this.state[i] != gs[i]) {
                return false;
            }
        }
        return true;
    }

    public void rearrange() {
        Random rand = new Random();
        for (int i = 0; i < RANDOMTIMES; ++i) {
            ArrayList<PuzzleState> arr = this.getNextStates();
            this.state = arr.get(Math.abs(rand.nextInt()) % arr.size()).state.clone();
        }
    }

    public int getHashValue() {
        int hashValue = 0;
        for (int i = 0; i < PUZZLESIZE * PUZZLESIZE; ++i) {
            hashValue *= 10;
            hashValue += this.state[i];
        }
        return hashValue;
    }
}

class PuzzleComparator implements Comparator<ArrayList<PuzzleState> > {
    //  A Star Algorithm
    @Override
    public int compare(ArrayList<PuzzleState> o1, ArrayList<PuzzleState> o2) {
        int m1 = o1.get(o1.size() - 1).manhattanDistance() + o1.size();
        int m2 = o2.get(o2.size() - 1).manhattanDistance() + o2.size();
        if (m1 > m2) {
            return 1;
        } else if (m2 > m1) {
            return -1;
        } else {
            return 0;
        }
    }
}

class PuzzleComparator2 implements Comparator<ArrayList<PuzzleState> > {
    //  Greedy
    @Override
    public int compare(ArrayList<PuzzleState> o1, ArrayList<PuzzleState> o2) {
        int m1 = o1.get(o1.size() - 1).manhattanDistance();
        int m2 = o2.get(o2.size() - 1).manhattanDistance();
        if (m1 > m2) {
            return 1;
        } else if (m2 > m1) {
            return -1;
        } else {
            return 0;
        }
    }
}