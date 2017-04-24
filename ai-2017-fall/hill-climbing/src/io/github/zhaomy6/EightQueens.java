package io.github.zhaomy6;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class EightQueens {
    //  debug only
    public static void main(String[] args) {
        QueensState q = new QueensState();
        q.showState();
        System.out.println(QueensState.getNextStates(q, 0).size());
    }

    //  Steepest-Ascent
    public static QueensState steepestAscentSolve() {
        HashMap<Integer, Boolean> hm = new HashMap<>();
        QueensState curState = new QueensState();
        //  curState.showState();
        for (int i = 0; i < QueensState.PROBLEM_SIZE * 30; ++i) {
            if (curState.getConflict() == 0){
                return curState;
            }
            ArrayList<QueensState> nextStates = QueensState.getNextStates(curState,
                    i % QueensState.PROBLEM_SIZE, hm);
            int min = 100;
            ArrayList<Integer> ptrs = new ArrayList<>();
            for (int j = 0; j < nextStates.size(); ++j) {
                if (nextStates.get(j).getConflict() <= min) {
                    min = nextStates.get(j).getConflict();
                    ptrs.add(j);
                }
            }

            //  从最小冲突的状态中选取一个作为下一个状态
            ArrayList<QueensState> choose = new ArrayList<>();
            for (int ele : ptrs) {
                if (nextStates.get(ele).getConflict() == min) {
                    choose.add(nextStates.get(ele));
                }
            }
            Random rand = new Random();
            int select = Math.abs(rand.nextInt()) % choose.size();
            curState = choose.get(select);
            hm.put(curState.getHashCode(), true);
        }
        return null;
    }

    //  First-Choice
    public static QueensState firstChoiceSolve() {
        HashMap<Integer, Boolean> hm = new HashMap<>();
        QueensState curState = new QueensState();
        for (int i = 0; i < QueensState.PROBLEM_SIZE * 30; ++i) {
            if (curState.getConflict() == 0){
                return curState;
            }
            ArrayList<QueensState> nextStates = QueensState.getNextStates(curState,
                    i % QueensState.PROBLEM_SIZE, hm);
            int min = curState.getConflict();
            //  选取最先出现比当前冲突数少的作为下一个状态
            for (int j = 0; j < nextStates.size(); ++j) {
                if (nextStates.get(j).getConflict() < min) {
                    curState = nextStates.get(j);
                    hm.put(curState.getHashCode(), true);
                    break;
                }
            }
        }
        return null;
    }

    //  Random-Restart
    public static QueensState randomRestartSolve() {
        HashMap<Integer, Boolean> hm = new HashMap<>();
        QueensState curState = new QueensState();
        while (true) {
            if (curState.getConflict() == 0){
                return curState;
            }
            Random rand = new Random();
            int chooseCol = Math.abs(rand.nextInt()) % QueensState.PROBLEM_SIZE;
            ArrayList<QueensState> nextStates = QueensState.getNextStates(curState,
                    chooseCol, hm);

            int min = 100;
            ArrayList<Integer> ptrs = new ArrayList<>();
            for (int j = 0; j < nextStates.size(); ++j) {
                if (nextStates.get(j).getConflict() <= min) {
                    min = nextStates.get(j).getConflict();
                    ptrs.add(j);
                }
            }
            //  从最小冲突的状态中选取一个作为下一个状态
            ArrayList<QueensState> choose = new ArrayList<>();
            for (int ele : ptrs) {
                if (nextStates.get(ele).getConflict() == min) {
                    choose.add(nextStates.get(ele));
                }
            }
            if (choose.size() == 0) continue;
            int select = Math.abs(rand.nextInt()) % choose.size();
            curState = choose.get(select);
            hm.put(curState.getHashCode(), true);
        }
    }

    //  Simulated-Annealing
    public static QueensState simulateAnnealingSolve() {
        final double COLD_DOWN_RATE = 0.6;
        final double INIT_TMP = 50.0;
        long start = Calendar.getInstance().getTimeInMillis();
        HashMap<Integer, Boolean> hm = new HashMap<>();
        QueensState curState = new QueensState();
        Random rand = new Random();
        while (true) {
            if (curState.getConflict() == 0) return curState;
            double T = INIT_TMP * Math.pow(COLD_DOWN_RATE, (Calendar.getInstance().getTimeInMillis() - start) * 10);
            if (T < 0.00001) {
                if (curState.getConflict() > 0) {
                    return null;
                }
                return curState;
            }
            int selectedCol = Math.abs(rand.nextInt()) % QueensState.PROBLEM_SIZE;
            ArrayList<QueensState> list = QueensState.getNextStates(curState, selectedCol, hm);
            if (list.size() == 0) continue;
            int selectedState = Math.abs((rand.nextInt())) % list.size();
            QueensState nextState = list.get(selectedState);
            if (nextState.getConflict() < curState.getConflict()) {
                curState = nextState;
                hm.put(curState.getHashCode(), true);
            } else {
                int diff = curState.getConflict() - nextState.getConflict();
                double prob = Math.exp(diff / T);
                if (Math.abs(rand.nextInt()) % 1000 < prob * 1000) {
                    curState = nextState;
                    hm.put(curState.getHashCode(), true);
                }
            }
        }
    }
}

class QueensState {
    public static final int PROBLEM_SIZE = 8;
    private int[] state;

    //  无参数 生成随机状态
    public QueensState() {
        Random rand = new Random();
        state = new int[PROBLEM_SIZE];
        for (int i = 0; i < PROBLEM_SIZE; ++i) {
            state[i] = Math.abs(rand.nextInt()) % PROBLEM_SIZE;
        }
    }

    public QueensState(int[] s) {
        state = s.clone();
    }

    public QueensState(QueensState qs) {
        this.state = qs.getState().clone();
    }

    public int[] getState() {
        return state;
    }

    //  返回冲突皇后组数
    public int getConflict() {
        int counter = 0;
        for (int i = 0; i < PROBLEM_SIZE; ++i) {
            for (int j = i + 1; j < PROBLEM_SIZE; ++j) {
                if (state[i] == state[j]) {
                    ++counter;
                } else if (j - i == Math.abs(state[j] - state[i])) {
                    ++counter;
                }
            }
        }
        return counter;

    }

    public int getHashCode() {
        int res = 0;
        for (int i = 0; i < PROBLEM_SIZE; ++i) {
            res *= 10;
            res += state[i];
        }
        return res;
    }

    public void showState() {
        System.out.println("----State " + this.getHashCode() + ": ----");
        for (int i = 0; i < PROBLEM_SIZE; ++i) {
            for (int j = 0; j < PROBLEM_SIZE; ++j) {
                if (state[j] == i) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
        System.out.println("Conflict: " + getConflict());
        System.out.println("-------------------");
    }

    //  获取下一个状态
    //  仅移动第col列上第王后
    public static ArrayList<QueensState> getNextStates(QueensState qs, int col) {
        ArrayList<QueensState> list = new ArrayList<>();
        for (int i = 0; i < PROBLEM_SIZE; ++i) {
            if (qs.getState()[col] != i) {
                int[] tmp = qs.getState().clone();
                tmp[col] = i;
                list.add(new QueensState(tmp));
            }
        }
        return list;
    }

    //  重载函数，过滤掉出现过的状态
    public static ArrayList<QueensState> getNextStates(QueensState qs, int col,
                                                       HashMap<Integer, Boolean> hm) {
        ArrayList<QueensState> list = new ArrayList<>();
        for (int i = 0; i < PROBLEM_SIZE; ++i) {
            if (qs.getState()[col] != i) {
                int[] tmp = qs.getState().clone();
                tmp[col] = i;
                QueensState tmpQS = new QueensState(tmp);
                if (hm.get(tmpQS.getHashCode()) == null) {
                    list.add(tmpQS);
//                    hm.put(tmpQS.getHashCode(), true);
                }
            }
        }
        return list;
    }
}