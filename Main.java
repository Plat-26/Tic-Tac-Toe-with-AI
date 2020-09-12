package tictactoe;
import  java.util.*;

public class Main {
    public static void main(String[] args) {
        new playTicTac().play();
    }
}

class playTicTac {
    static char[][] arry = new char[4][4];
    static String cells;
    static GameState gs;
    static char win;
    static boolean ischecked = false;
    static boolean useX = false;
    static boolean inRow = false;
    static boolean inColomn = false;
    static boolean inDiagonal = false;
    static boolean notFinished = false;

    void play() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter cells: ");
        cells = scanner.nextLine();
        field(cells);
        do {
            System.out.println("Enter the coordinates: ");
            String cords = scanner.nextLine();
            String result = checkCoords(cords);
            if (!ischecked) {
                System.out.println(result);
            } else {
                printField();
            }
        } while (!ischecked);
        gs = analyzeField(arry);
        System.out.println(gs.state);
    }

    //Puts the input cells into 3 x 3 array
    void field(String cells) {
        int k = 0;
        String s = cells.replace('_', ' ');
        for (int j = 3; j > 0; j--) {
            for (int i = 1; i < 4; i++) {
                arry[i][j] = s.charAt(k);
                k++;
            }
        }
        this.printField();
    }

    //Method to switch between the char X and O while playing
    char player(String cells) {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < cells.length(); i++) {
            if (cells.charAt(i) == 'X') countX++;
            else if (cells.charAt(i) == 'O') countO++;
        }

        if (countX > countO) return 'O';
        else if (countX < countO) return 'X';
        else {
            useX = !useX;
            return (useX) ? 'X' : 'O';
        }
    }

    void printField() {
        System.out.println("---------");
        System.out.printf("| %c %c %c |\n", arry[1][3], arry[2][3], arry[3][3]);
        System.out.printf("| %c %c %c |\n", arry[1][2], arry[2][2], arry[3][2]);
        System.out.printf("| %c %c %c |\n", arry[1][1], arry[2][1], arry[3][1]);
        System.out.println("---------");
    }

    String checkCoords(String coords) {
        String[] check = coords.split(" ");
        try {
            int x = Integer.parseInt(check[0]);
            int y = Integer.parseInt(check[1]);

            if (x < 1 || x > 3) {
                return "Coordinates should be from 1 to 3!";
            } else if (y < 1 || y > 3) {
                return "Coordinates should be from 1 to 3!";
            } else {

                return this.updateField(x, y);
            }
        } catch (NumberFormatException e) {
            return "You should enter numbers!";
        }
    }

    String updateField(int x, int y) {
        if (arry[x][y] == ' ') {
            arry[x][y] = this.player(cells);
            ischecked = true;
        } else {
            return "This cell is occupied! Choose another one!";
        }
        return null;
    }

    void checkRows(char[][] arry) {
        for (int i = 3; i > 0; i--) {
            int j = 1;
            if ((arry[j][i] == arry[j + 1][i]) && (arry[j + 1][i] == arry[j + 2][i])) {
                if (arry[j][i] != ' ') {
                    inRow = true;
                    win = arry[j][i];
                }
            }
        }
    }

     void checkColomns(char[][] arry) {
        for (int i = 1; i < 4; i++) {
            int j = 3;
            if ((arry[i][j] == arry[i][j - 1]) && (arry[i][j - 1] == arry[i][j - 2])) {
                if (arry[i][j] != ' ') {
                    inColomn = true;
                    win = arry[i][j];
                }
            }
        }
    }

     void checkDiagonals(char[][] arry) {
        for (int i = 1, j = 3; i < 2 && j > 2; i++, j--) { //forward diagonal\
            if (arry[i][j] == arry[i + 1][j - 1] && arry[i + 1][j - 1] == arry[i + 2][j - 2]) {
                inDiagonal = true;
                win = arry[i][j];

            }//for backward diagonal/
            if (arry[j][j] == arry[j - 1][j - 1] && arry[j - 1][j - 1] == arry[j - 2][j - 2]) {
                inDiagonal = true;
                win = arry[j][j];

            }
        }
     }


     GameState analyzeField(char[][] arry) {
        this.checkColomns(arry);
        this.checkDiagonals(arry);
        this.checkRows(arry);

        //Check if game is not finished
        for (char[] ch : arry) {
            for (char c : ch) {
                if (c == ' ') {
                    notFinished = true;
                    break;
                }
            }
        }

        //get the game state
        if (inRow || inDiagonal || inColomn) {
            if (win == 'X') {
                return GameState.X_WINS;
            } else if (win == 'O') {
                return GameState.O_WINS;
            }
        } else if (notFinished) {
            return GameState.NOT_FINISHED;
        } else {
            return GameState.DRAW;
        }
        return null;
    }
}



enum GameState {
    NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");

    String state;

    GameState(String state) {
        this.state = state;
    }
}
