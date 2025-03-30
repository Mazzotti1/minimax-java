import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minimax {
    static final int EMPTY = 0;
    static final int HUMAN = -1;
    static final int AI = 1;
    static final int NOBODY = 15;
    static final int DRAW = 8;

    static class Play {
        int row, column;
        Play(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public static void main(String[] args) {
        int[][] table = new int[3][3];
        startTable(table);

        Random random = new Random();
        table[random.nextInt(3)][random.nextInt(3)] = AI;

        while (freePositions(table) > 0) {
            showTable(table);

            Play humanPlay = readPlay(table);
            table[humanPlay.row][humanPlay.column] = HUMAN;

            int winner = verifyVictory(table);
            if (winner != NOBODY) {
                stateWin(winner, table);
                break;
            }

            Play AIPlay = bestPlay(table);
            table[AIPlay.row][AIPlay.column] = AI;

            winner = verifyVictory(table);
            if (winner != NOBODY) {
                stateWin(winner, table);
                break;
            }
        }

        showTable(table);
    }

    static void startTable(int[][] table) {
        for (int i = 0; i < 3; i++) {
            Arrays.fill(table[i], EMPTY);
        }
    }

    static int verifyVictory(int[][] table) {
        for (int i = 0; i < 3; i++) {
            if (table[i][0] == table[i][1] && table[i][0] == table[i][2] && table[i][0] != EMPTY) {
                return table[i][0];
            }
        }

        for (int j = 0; j < 3; j++) {
            if (table[0][j] == table[1][j] && table[0][j] == table[2][j] && table[0][j] != EMPTY) {
                return table[0][j];
            }
        }

        if (table[0][0] == table[1][1] && table[0][0] == table[2][2] && table[0][0] != EMPTY) {
            return table[0][0];
        }

        if (table[0][2] == table[1][1] && table[0][2] == table[2][0] && table[0][2] != EMPTY) {
            return table[0][2];
        }

        if (freePositions(table) == 0) {
            return DRAW;
        }

        return NOBODY;
    }

    static int freePositions(int[][] table) {
        int free = 0;
        for (int[] row : table) {
            for (int cell : row) {
                if (cell == EMPTY) free++;
            }
        }
        return free;
    }

    static Play bestPlay(int[][] table) {
        int bestValue = Integer.MIN_VALUE;
        Play bestPlay = new Play(-1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (table[i][j] == EMPTY) {
                    table[i][j] = AI;
                    int score = minimax(table, 0, false);
                    table[i][j] = EMPTY;

                    if (score > bestValue) {
                        bestValue = score;
                        bestPlay = new Play(i, j);
                    }
                }
            }
        }

        return bestPlay;
    }

    static int minimax(int[][] table, int depth, boolean isMaximizing) {
        int winner = verifyVictory(table);
        if (winner != NOBODY) {
            if (winner == AI) return 10 - depth;
            if (winner == HUMAN) return depth - 10;
            return 0;
        }

        if (isMaximizing) {
            int bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (table[i][j] == EMPTY) {
                        table[i][j] = AI;
                        bestValue = Math.max(bestValue, minimax(table, depth + 1, false));
                        table[i][j] = EMPTY;
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (table[i][j] == EMPTY) {
                        table[i][j] = HUMAN;
                        bestValue = Math.min(bestValue, minimax(table, depth + 1, true));
                        table[i][j] = EMPTY;
                    }
                }
            }
            return bestValue;
        }
    }

    static void stateWin(int winnerPlayer, int[][] table) {
        switch (winnerPlayer) {
            case HUMAN -> System.out.println("\n::: Parabéns, você ganhou! :::\n");
            case AI -> System.out.println("\n::: A IA venceu, tente novamente! :::\n");
            case DRAW -> System.out.println("\n::: Empatou! :::\n");
            default -> System.out.println("\n::: Jogo em andamento :::\n");
        }
        showTable(table);
        System.exit(0);
    }

    static void showTable(int[][] table) {
        System.out.println("    1   2   3");
        System.out.println("  -------------");
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < 3; j++) {
                char position = switch (table[i][j]) {
                    case AI -> 'X';
                    case HUMAN -> 'O';
                    default -> '.';
                };
                System.out.print(" " + position + " |");
            }
            System.out.println("\n  -------------");
        }
    }

    static Play readPlay(int[][] table) {
        Scanner scanner = new Scanner(System.in);
        int row, column;

        do {
            System.out.print("Informe a LINHA [1, 2 ou 3]: ");
            row = scanner.nextInt() - 1;
        } while (row < 0 || row > 2);

        do {
            System.out.print("Informe a COLUNA [1, 2 ou 3]: ");
            column = scanner.nextInt() - 1;
        } while (column < 0 || column > 2);

        while (table[row][column] != EMPTY) {
            System.out.println("Essa posição já está ocupada! Escolha outra.");
            do {
                System.out.print("Informe a LINHA [1, 2 ou 3]: ");
                row = scanner.nextInt() - 1;
            } while (row < 0 || row > 2);

            do {
                System.out.print("Informe a COLUNA [1, 2 ou 3]: ");
                column = scanner.nextInt() - 1;
            } while (column < 0 || column > 2);
        }

        return new Play(row, column);
    }
}
