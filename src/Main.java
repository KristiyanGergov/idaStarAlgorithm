import javafx.util.Pair;

import java.util.*;

enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN;

    @Override
    public String toString() {
        if (this == Direction.LEFT) {
            return "left";
        } else if (this == Direction.RIGHT) {
            return "right";
        } else if (this == Direction.UP) {
            return "up";
        } else {
            return "down";
        }
    }
}

public class Main {
    private static Scanner input = new Scanner(System.in);

    private final int[][] initialTable;
    private final Map<Integer, Pair<Integer, Integer>> goalMap;
    private final int startRow;
    private final int startCol;

    private int[][] currentTable;
    private int currentRow;
    private int currentCol;

    private Main(int[][] initialTable, Map<Integer, Pair<Integer, Integer>> goalMap, int startRow, int startCol) {
        this.initialTable = initialTable;
        currentTable = new int[initialTable.length][initialTable.length];
        this.goalMap = goalMap;
        this.startRow = startRow;
        this.startCol = startCol;
    }

    private static Map<Integer, Pair<Integer, Integer>> initializeGoalState(int tableWidth, int goalZeroIndex) {
        Map<Integer, Pair<Integer, Integer>> map = new HashMap<>();
        int value = 1;
        int index = 0;
        for (int row = 0; row < tableWidth; ++row) {
            for (int col = 0; col < tableWidth; col++) {
                if (index++ != goalZeroIndex) {
                    Pair<Integer, Integer> pair = new Pair<>(row, col);
                    map.put(value++, pair);
                }
            }
        }

        return map;
    }

    private static void printInput(int numbersCount, int zeroIndex, int[][] arr, int tableWidth) {
        System.out.println(numbersCount);
        System.out.println(zeroIndex);
        for (int i = 0; i < tableWidth; ++i) {
            for (int j = 0; j < tableWidth; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int heuristic() {
        int score = 0;

        for (int row = 0; row < currentTable.length; ++row) {
            for (int col = 0; col < currentTable.length; ++col) {
                int value = currentTable[row][col];
                if (value != 0) {
                    Pair<Integer, Integer> pair = goalMap.get(value);
                    score += Math.abs(row - pair.getKey()) + Math.abs(col - pair.getValue());
                }
            }
        }

        return score;
    }

    private boolean calculateCurrentTable(List<Direction> path) {
        for (int i = 0; i < currentTable.length; i++) {
            System.arraycopy(initialTable[i], 0, currentTable[i], 0, currentTable.length);
        }
        currentCol = startCol;
        currentRow = startRow;


        for (Direction dir : path) {
            if (dir == Direction.LEFT) {
                --currentCol;
                if (currentCol < 0) {
                    return false;
                }
                currentTable[currentRow][currentCol + 1] = currentTable[currentRow][currentCol];
            } else if (dir == Direction.RIGHT) {
                ++currentCol;
                if (currentCol >= currentTable.length) {
                    return false;
                }
                currentTable[currentRow][currentCol - 1] = currentTable[currentRow][currentCol];
            } else if (dir == Direction.UP) {
                --currentRow;
                if (currentRow < 0) {
                    return false;
                }
                currentTable[currentRow + 1][currentCol] = currentTable[currentRow][currentCol];
            } else if (dir == Direction.DOWN) {
                ++currentRow;
                if (currentRow >= currentTable.length) {
                    return false;
                }
                currentTable[currentRow - 1][currentCol] = currentTable[currentRow][currentCol];
            }
            currentTable[currentRow][currentCol] = 0;
        }

        return true;
    }

    private List<Direction> idaStarSearch() {
        for (int maxDepth = 0; ; ++maxDepth) {
            List<Direction> path = depthSearch(0, maxDepth, new ArrayList<Direction>());
            if (path != null) {
                return path;
            }
        }
    }

    private List<Direction> depthSearch(int currentDepth, int maxDepth, List<Direction> path) {
        if (currentDepth > maxDepth) {
            return null;
        }

        if (!calculateCurrentTable(path)) {
            return null;
        }

        int heuristicValue = heuristic();

        if (heuristicValue == 0) {
            return path;
        }

        if (heuristicValue > (maxDepth - currentDepth)) {
            return null;
        } else {
            ++currentDepth;
            List<Direction> newPath;
            List<Direction> result;

            newPath = new ArrayList<>(path);
            newPath.add(Direction.LEFT);
            result = depthSearch(currentDepth, maxDepth, newPath);
            if (result != null) {
                return result;
            }

            newPath = new ArrayList<>(path);
            newPath.add(Direction.RIGHT);
            result = depthSearch(currentDepth, maxDepth, newPath);
            if (result != null) {
                return result;
            }

            newPath = new ArrayList<>(path);
            newPath.add(Direction.UP);
            result = depthSearch(currentDepth, maxDepth, newPath);
            if (result != null) {
                return result;
            }

            newPath = new ArrayList<>(path);
            newPath.add(Direction.DOWN);
            result = depthSearch(currentDepth, maxDepth, newPath);
            return result;
        }
    }

    public static void main(String[] args) {

        int numbersCount = input.nextInt();
        int goalZeroIndex = input.nextInt();
        if (goalZeroIndex == -1) {
            goalZeroIndex = numbersCount;
        }

        ++numbersCount;
        int tableWidth = (int) Math.sqrt(numbersCount);

        int startRow = 0;
        int startCol = 0;

        int[][] arr = new int[tableWidth][tableWidth];
        for (int i = 0; i < tableWidth; ++i) {
            for (int j = 0; j < tableWidth; j++) {
                arr[i][j] = input.nextInt();
                if (arr[i][j] == 0) {
                    startRow = i;
                    startCol = j;
                }
            }
        }

        Map<Integer, Pair<Integer, Integer>> goalMap = initializeGoalState(tableWidth, goalZeroIndex);


        Main table = new Main(arr, goalMap, startRow, startCol);

        printInput(numbersCount, goalZeroIndex, arr, tableWidth);
        System.out.println(table.goalMap);

//        System.out.println(table.heuristic(arr));
        List<Direction> path = table.idaStarSearch();
        System.out.println(path.size());
        for (Direction dir : path) {
            System.out.println(dir);
        }
//        System.out.println(Direction.DOWN.toString());
    }
}
