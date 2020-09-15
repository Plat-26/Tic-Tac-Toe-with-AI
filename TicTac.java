package tictactoe;
import  java.util.*;

public class Main {
    public static void main(String[] args) {
        new playTicTac().play();
    }
}


class playTicTac {
    static char[][] arry = new char[4][4];
    static GameState gs = GameState.NOT_FINISHED;
    static char win;
    static char player;
    boolean players = true;
    static String player1;
    static String player2;
    static boolean ischecked = false;
    static boolean inRow = false;
    static boolean inColomn = false;
    static boolean inDiagonal = false;
    static boolean notFinished = true;
    static String playing;
    //static GameState playing = GameState.USER;

    void computerGuess() {
        Random random = new Random(4);
        int cord1 = random.nextInt(3 ) + 1;
        int cord2 = random.nextInt(3) + 1;
        checkCoords(cord1 + " " + cord2);
    }

    void quit(){
        System.out.println();
    }

    void setPlayer() {

        if (players) {
            playing = player1;
            player = 'X';
        } else {
            playing = player2;
            player = 'O';
        }
        players = !players;
    }

    void getCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input command: ");

        try {
            String command1 = scanner.next();
            if (command1.equals("start")) {
                player1 = scanner.next();
                player2 = scanner.next();
                gameLoop();
            } else if(command1.equals("exit")) {
                quit();
            }
        } catch (Exception e) {
            System.out.println("Bad parameters!");
            getCommand();
        }
    }

    void gameLoop() {
        field();
        setPlayer();
        if (gs == GameState.NOT_FINISHED) {
            switch (playing) {
                case "user":
                    userPlay();
                    setPlayer();
                    System.out.println(playing);
                    break;
                case "easy":
                    System.out.println("Making move level \"easy\"");
                    levelEasy();
                    setPlayer();
                    break;
                default:
                    getCommand();
            }
        }
        System.out.println(gs.state);
        getCommand();
    }

    void play() {
        getCommand();
    }

    void levelEasy() {
        computerGuess();
        if(!ischecked) computerGuess();
        else printField();
        analyzeField(arry);
    }

    void userPlay() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter the coordinates: ");
            String userCords = scanner.nextLine();
            String result = checkCoords(userCords);
            if (!ischecked) {
                System.out.println(result);
            } else {
                printField();
            }
        } while (!ischecked);
        analyzeField(arry);
    }

    void field() {
        for (char[] row : arry) {
            Arrays.fill(row, ' ');
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
            arry[x][y] = player;
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

     void analyzeField(char[][] arry) {
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
                notFinished = false;
                gs = GameState.X_WINS;
            } else if (win == 'O') {
                notFinished = false;
                gs = GameState.O_WINS;
            }
        } else if (notFinished) {
            gs = GameState.NOT_FINISHED;
        } else {
            gs = GameState.DRAW;
        }
    }
}



enum GameState {
    NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");
    //USER("Newbie"),
    //MACHINE("Easy");

    String state;

    GameState(String state) {
        this.state = state;
    }
}
