import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minimax {
    static final int EMPTY = 0;
    static final int HUMAN =  -1;
    static final int AI = 1;

    static class Play {
        int row, column;
        Play(int row, int column){
            this.row = row;
            this.column = column;
        }
    }


    public static void main(String[] args) {
        int[][] table = new int[3][3];
        startTable(table);

        Random random = new Random();

        table[random.nextInt(3)][random.nextInt(3)] = AI;

        while(freePositions(table) > 0){
            showTable(table);

            Play humanPlay = readPlay(table);
            table[humanPlay.row][humanPlay.column] = HUMAN;
            if(verifyVictory(table) != 0) break;

            Play AIPlay = bestPlay(table);
            table[AIPlay.row][AIPlay.column] = AI;
            if(verifyVictory(table) != 0) break;
        }
        showTable(table);
        stateWin(verifyVictory(table), table);

    }

    static void startTable(int[][] table){
        for(int i = 0; i < 3; i++){
            Arrays.fill(table[i], EMPTY);
        }
    }

    static int verifyVictory(int[][] table) {
        int winner = -1;

        for (int i = 0; i < 3; i++) {
            if (table[i][0] == table[i][1] && table[i][0] == table[i][2] && table[i][0] != 0) {
                return table[i][0];
            }
        }

        for (int i = 0; i < 3; i++) {
            if (table[0][i] == table[1][i] && table[0][i] == table[2][i] && table[0][i] != 0) {
                return table[0][i];
            }
        }

        if (table[0][0] == table[1][1] && table[0][0] == table[2][2] && table[0][0] != 0) {
            return table[0][0];
        }

        if (table[0][2] == table[1][1] && table[0][2] == table[2][0] && table[0][2] != 0) {
            return table[0][2];
        }

        if (freePositions(table) == 0) {
            return 0;
        }

        return -3;
    }


    static int plus(int a, int b, int c) { return a + b + c; }

    static int freePositions(int[][] table) {
        int free = 0;
        for (int[] row : table) {
            for (int cell : row) {
                if (cell == EMPTY) free++;
            }
        }
        return free;
    }

    static Play bestPlay(int[][] table){
        int bestValue = Integer.MIN_VALUE;
        Play bestPlay = new Play(-1, -1);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j< 3; j++){
                if (table[i][j] == EMPTY) {
                    table[i][j] = AI;
                    int valor = minimax(table, 0, false);
                    table[i][j] = EMPTY;

                    if (valor > bestValue) {
                        bestValue = valor;
                        bestPlay = new Play(i, j);
                    }
                }
            }
        }
        return bestPlay;
    }

    static int minimax(int[][] table, int depth, boolean isMaximizing) {
        int winner = verifyVictory(table);
        if (winner != -1) return winner;

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
            case 0 -> System.out.println("\n::: Empate! Boa tentativa! :::\n");
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
        int linha, coluna;

        do {
            System.out.print("Informe a LINHA [1, 2 ou 3]: ");
            linha = scanner.nextInt() - 1;
        } while (linha < 0 || linha > 2);

        do {
            System.out.print("Informe a COLUNA [1, 2 ou 3]: ");
            coluna = scanner.nextInt() - 1;
        } while (coluna < 0 || coluna > 2);

        return new Play(linha, coluna);
    }

}