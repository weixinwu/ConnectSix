import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by weixinwu on 2016-07-28.
 */
public class Board extends JFrame {


    static boolean computerTurn = false;
    static final int verticleBlock = 1;
    static final int horizontalBlock = 2;
    static final int forwardBlock = 3;
    static final int backwardBlock = 4;
    static int gameLevel = 3;
    static int gameBoard = 3;
    public static int blackWin = 0;
    public static boolean blackturn = true;
    public static final int playerX = 1;
    public static final int playerY = 2;
    public static char [][] globalGameBoard;
    public static ArrayList<Points> points;

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        System.out.println("int the paintcompoment method");
        for (int i = 0 ; i < points.size();i++){
            for (int j=0;j<points.size();j++){
                g.setColor(Color.BLACK);
                g.drawRect(i*300,j*300,300,300);

            }
        }

    }

    public Board(char [][] board){
        int size = board.length;

        JFrame jf = new JFrame();
        JPanel jp = new JPanel();
        jp.setSize(gameBoard*300,gameBoard*300);
        jf.setSize(jp.getSize());
        repaint();
        jf.add(jp);
        jf.setVisible(true);
    }
    public void paintSqure(int i,int j){
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("int the paint method");
        for (int i = 0 ; i < points.size();i++){
            for (int j=0;j<points.size();j++){
                g.setColor(Color.BLACK);
                g.drawRect(i*300,j*300,300,300);

            }
        }
    }

    public static void main(String argv[]) throws Exception {



        String s = "Asd";

        s = s.substring(3,s.length());
        System.out.println(s);





        char[][] board = cleanBoard(gameBoard);



        int row,col;
        row = -1;
        col = -1;
        printBoard(board);
        Scanner sc = new Scanner(System.in);
        int i = 0;
        while (end(board)==0 && !checkBoardFull(board)){


            System.out.print("your input here:");
            row = sc.nextInt();
            col = sc.nextInt();
            while (!isInputvalid(board,row,col)){
                System.out.print("Input invalid, enter the row and col:");
                row = sc.nextInt();
                col = sc.nextInt();
            }
            board[row][col]='O';


            System.out.println("computer turn");
            int score;
            score = setMove(board,playerX);


            printBoard(board);

            if (!(end(board)==0 && !checkBoardFull(board)))
                break;




        }
        System.out.println("end"+end(board) + checkBoardFull(board));




    }

    public static int setMove(char[][]board,int player){
        if (end(board)!=0||checkBoardFull(board)){
            return end(board);
        }
        Point p = null;
        for (int i = 0 ; i < gameBoard;i++){
            for (int j = 0 ; j < gameBoard;j++){
                if (board[i][j]=='U'){
                    if (player ==playerX) {
                        board[i][j] = 'X';
                        player=playerY;
                    }
                    else {
                        board[i][j] = 'O';
                        player = playerX;
                    }

                    char [][]copyBoard = getCopyBoard(board);
                    int result = setMove(copyBoard,player);
                    //System.err.println("i :"+i +" and j :"+j+" result is :"+result);
                    if (result == 0){
                        p = new Point(i,j);
                    }
                    if (player ==playerX && result ==playerX){
                        board[i][j]='U';
                        player=playerY;
                    }else if (player ==playerX && result == playerY){
                        return playerY;
                    }else if (player ==playerY && result ==playerY){
                        board[i][j]='U';
                        player=playerX;
                    }else if (player==playerY && result == playerX){
                        return playerX;
                    }else {

                        board[i][j]='U';
                        if (player==playerY)
                            player=playerX;
                        else player = playerY;
                        //todo handle code here

                    }
                }
            }
        }
        if (p!=null) {
            if (player == playerX)
                board[p.x][p.y] = 'X';
            else board[p.x][p.y] = 'Y';
        }

        return 0;

    }
    public static char[][] getCopyBoard(char [][]board){
        char [][]b = new char[board.length][];
        for (int i = 0 ; i < board.length;i++)
            b[i]= new char[board.length];

        for (int i = 0; i < b.length;i++){

            for (int j = 0 ; j < board.length;j++)
                b[i][j]=board[i][j];
        }
        return  b;
    }

    private static void chooseRandom(char[][] board) {
        for (int i = 0 ; i < gameBoard;i++){
            for (int j = 0 ; j < gameBoard;j++){
                if (board[i][j]=='U'){
                    board[i][j]= 'X';
                    return;
                }
            }
        }
    }




    public static boolean checkBoardFull(char[][] b){
        int size = b.length;
        for (int i = 0 ; i < size;i++){
            for (int j = 0 ; j < size;j++){
                if (b[i][j]=='U')
                    return false;
            }
        }
        return true;
    }
    public static void computerTurn(char [][]board){
        if (!block(board))
            attack(board);
    }
    public static boolean attack(char [][]board){
        int size = board.length;
        for (int i = 0 ; i < size;i++){
            for (int j = 0 ; j < size;j++){
                if (board[i][j]=='U'){
                    board[i][j]='X';
                    System.out.println("computer place at "+i+" and "+j);
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean block(char [][]board){
        int size = board.length;
        boolean ismoved = false;
        for (int i = 0 ; i < size;i++){
            for (int j = 0 ; j < size;j++){
                if (board[i][j]=='O'){
                    if (checkBackwardDiag(board,i,j)>=3) {
                        //block backwordDiag
                        ismoved =  blockMove(board,i,j,backwardBlock);
                    }
                    else if (checkForwardDiag(board,i,j)>=3) {

                        ismoved = blockMove(board,i,j,forwardBlock);

                    }
                    else if (checkHoriz(board,i,j)>=3) {
                        //blockhoriz
                        ismoved = blockMove(board,i,j,horizontalBlock);
                    }
                    else if (checkVer(board,i,j)>=3){
                        //blockver
                        ismoved = blockMove(board,i,j,verticleBlock);
                    }
                    else {

                    }
                }
                if (ismoved == true)
                    return ismoved;
            }
        }
        return false;
    }
    public static boolean isInputvalid(char [][]board,int row,int col){
        if (row >=board.length ||row <0 ||col>board.length||col<0||board[row][col]!='U')
            return false;
        else return  true;
    }

    public static int end(char[][] board){
        int lenght = board.length;
        for (int i = 0 ; i < lenght;i++){
            for (int j=0; j < lenght; j++){
                if (board[i][j]!='U'){
                    if (checkVer(board,i,j)>=gameLevel||checkHoriz(board,i,j)>=gameLevel||checkForwardDiag(board,i,j)>=gameLevel||checkBackwardDiag(board,i,j)>=gameLevel) {
                        if (board[i][j]=='X')
                            return playerX;
                        else if (board[i][j]=='O') return playerY;
                        else return 0;
                    }
                }
            }
        }
        return 0;
    }


    // /
    public static void setGlobalGameBoard(char[][]b ){
        globalGameBoard = b;
    }
    public static int  checkBackwardDiag(char [][]board,int row,int col){
        int score = -1;
        int length = board.length;
        char color = board[row][col];
        int tempRow = row;
        int tempCol = col;
        while (tempRow<length&&tempCol<length){
            if (board[tempRow][tempCol]==color){
                score++;
                tempRow++;
                tempCol++;
            }else {
                tempRow=length;

            }
        }
        while (row>=0&&col>=0){
            if (board[row][col]==color){
                score++;
                row--;
                col--;
            }else {
                row = -1;

            }
        }
        return score;
    }
    public static int checkForwardDiag(char [][]board,int row,int col){
        int score = -1;
        int length = board.length;
        char color = board[row][col];
        int tempRow = row;
        int tempCol = col;
        while (tempRow<length&&tempCol>=0){
            if (board[tempRow][tempCol]==color){
                score++;
                tempRow++;
                tempCol--;
            }else {
                tempRow=length;
            }
        }
        while (row>=0&&col<length){
            if (board[row][col]==color){
                score++;
                row--;
                col++;
            }else
                row = -1;
        }
        return score;
    }
    public static int checkVer(char [][]board,int row,int col){
        int score = 0;
        char color = board[row][col];
        int tempRow = row;
        while (tempRow>=0){
            if (board[tempRow][col]==color) {
                score++;
                tempRow--;
            }
            else
                tempRow = -1;
        }
        tempRow = row;
        score--;
        while (tempRow<board.length){
            if (board[tempRow][col]==color){
                score++;
                tempRow++;
            }else {
                tempRow=board.length;
            }
        }
        return score;
    }

    public static int checkHoriz(char [][]board,int row,int col){
        int score = 0;
        char color = board[row][col];
        int tempcol = col;
        while (tempcol>=0){
            if (board[row][tempcol]==color) {
                score++;
                tempcol--;
            }
            else
                tempcol = -1;
        }
        tempcol = col;
        score--;
        while (tempcol<board.length){
            if (board[row][tempcol]==color){
                score++;
                tempcol++;
            }else {
                tempcol=board.length;
            }
        }
        return score;
    }

    public static void printBoard(char [][]board){

        int length = board.length;
        String b = "";
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0 ; i < length;i++){

            String row ="";
            for (int j = 0 ; j < length;j++){
                row+=board[i][j]+" ";
            }
            row = i+":"+row+"\n";
            b+=row;

        }

        System.out.println(b);
        //System.out.flush();

    }
    public static char[][] cleanBoard(int size){
        char [][]board = new char[size][];
        for (int i = 0 ; i < size;i++)
            board[i]= new char[size];

        for (int i = 0; i < board.length;i++){
            board[i]= new char[gameBoard];
            for (int j = 0 ; j < board.length;j++)
                board[i][j]='U';
        }
        return  board;
    }
    public static boolean blockMove(char[][] board,int row,int col,int code){
        int tempRow = row;
        int tempCol = col;
        int length = board.length;
        if (code ==backwardBlock){
            System.out.println("backward");
            while (board[tempRow][tempCol]=='O'&&tempRow<length&&tempCol<length){
                tempRow++;
                tempCol++;
            }
            if (board[tempRow][tempCol]=='U'){
                board[tempRow][tempCol]='X';
                return  true;
            }else if (board[tempRow][tempCol]=='X')
                return false;
            else if (tempRow==length ||tempCol ==length){
                tempRow = row;
                tempCol = col;

                while (board[tempRow][tempCol]=='O'&&tempRow>=0&&tempCol>=0){
                    tempRow--;
                    tempCol--;
                }
                if (board[tempRow][tempCol]=='U'){
                    board[tempRow][tempCol]='X';
                    return  true;
                }else if (board[tempRow][tempCol]=='X')
                    return false;
                else if (tempRow<0 ||tempCol<0)
                    return false;
            }
        }else if (code ==forwardBlock){
            System.out.println("forward");
            while (board[tempRow][tempCol]=='O'&&tempRow<length&&tempCol<length){
                tempRow++;
                tempCol--;
            }
            if (board[tempRow][tempCol]=='U'){
                board[tempRow][tempCol]='X';
                return  true;
            }else if (board[tempRow][tempCol]=='X')
                return false;
            else if (tempRow==length ||tempCol <0) {
                tempRow = row;
                tempCol = col;
                while (board[tempRow][tempCol]=='O'&&tempRow>=0&&tempCol>=0){
                    tempRow--;
                    tempCol++;
                }
                if (board[tempRow][tempCol]=='U') {
                    board[tempRow][tempCol] = 'X';
                    return true;
                }
                else if (board[tempRow][tempCol]=='X')
                    return false;
                else if (tempRow<0 ||tempCol==length)
                    return false;
            }
        }else if (code == horizontalBlock){
            System.out.println("horizon");
            while (board[tempRow][tempCol]=='O'&&tempCol<length){

                tempCol++;
            }
            if (board[tempRow][tempCol]=='U'){
                board[tempRow][tempCol]='X';
                return  true;
            }else if (board[tempRow][tempCol]=='X')
                return false;
            else if (tempCol==length){
                tempCol = col;
                while (board[tempRow][tempCol]=='O'&&tempCol>=0){
                    tempCol--;
                }
                if (board[tempRow][tempCol]=='U'){
                    board[tempRow][tempCol]='X';
                    return  true;
                }else if (board[tempRow][tempCol]=='X')
                    return false;
                else if (tempRow<0 ||tempCol==length)
                    return false;
            }

        }else {
            System.out.println("vertical");
            while (board[tempRow][tempCol]=='O'&&tempRow<length){

                tempRow++;
            }
            if (board[tempRow][tempCol]=='U'){
                board[tempRow][tempCol]='X';
                return  true;
            }else if (board[tempRow][tempCol]=='X')
                return false;
            else if (tempRow==length){
                tempRow = row;
                while (board[tempRow][tempCol]=='O'&&tempRow>=0){
                    tempRow--;
                }
                if (board[tempRow][tempCol]=='U'){
                    board[tempRow][tempCol]='X';
                    return  true;
                }else if (board[tempRow][tempCol]=='X')
                    return false;
                else if (tempRow<0)
                    return false;
            }
        }
        return false;
    }




}
