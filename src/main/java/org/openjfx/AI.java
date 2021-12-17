package org.openjfx;

public class AI {
    static class Move{
        int row,col;
        Move(int row, int col){
            this.row = row;
            this.col = col;
        }
        void setMove(int row,int col){
            this.row = row;
            this.col = col;
        }
    };
    static char player = 'x', opponent = 'o';

     static int findBestMove(int []gameState){
         char[][] board=new char[3][3];
         for(int i=0; i<3; ++i){
             for(int j=0;j<3;++j){
                 int pos = i*3+j;
                 if(gameState[pos]==3)board[i][j]='_';
                 else if(gameState[pos]==1)board[i][j]='x';
                 else board[i][j]='o';
             }
         }
         int bestVal = -9999;
         Move bestMove = new Move(-1,-1);
         for(int i=0; i<3; ++i){
             for(int j=0; j<3; ++j){
                 if(board[i][j]=='_'){
                     board[i][j] = player;
                     int moveVal = miniMax(board,0,false);
                     board[i][j] = '_';
                     if(moveVal>bestVal){
                         bestVal = moveVal;
                         bestMove.setMove(i,j);
                     }
                 }
             }
         }
//         System.out.println(bestMove.row+" "+bestMove.col);
       return bestMove.row*3+bestMove.col;
     }

    private static int miniMax(char[][] board, int depth, boolean isMax) {
        int score = checkWinner(board);
        if(score==99) return score;
        if(score==-99) return score;
        if(!isMoveBaki(board)) return 0;
        if(isMax){
            int best = -9999;
            for(int i=0; i<3; ++i){
                for(int j=0; j<3; ++j){
                    if(board[i][j]=='_'){
                        board[i][j] = player;
                        best = Math.max(best,miniMax(board,depth+1,!isMax));
                        board[i][j] = '_';
                    }
                }
            }
            return best;
        }
        else{
            int best = 9999;
            for(int i=0; i<3; ++i){
                for(int j=0; j<3; ++j){
                    if(board[i][j]=='_'){
                        board[i][j] = opponent;
                        best = Math.min(best,miniMax(board,depth+1,!isMax));
                        board[i][j] = '_';
                    }
                }
            }
            return best;
        }
    }

    private static boolean isMoveBaki(char[][] board) {
         for(int i=0; i<3; ++i){
             for(int j=0; j<3; ++j){
                 if(board[i][j]=='_') return true;
             }
         }
         return false;
    }

    private static int checkWinner(char[][] board){
         for(int r=0; r<3; ++r){
             if(board[r][0]!='_' && board[r][0]==board[r][1] && board[r][0]==board[r][2]){
                 if(board[r][0]==player) return 99;
                 else return -99;
             }
         }
        for(int c=0; c<3; ++c){
            if(board[0][c]!='_' && board[0][c]==board[1][c] && board[0][c]==board[2][c]){
                if(board[0][c]==player) return 99;
                else return -99;
            }
        }
        if(board[0][0]!='_'&&board[0][0]==board[1][1]&&board[1][1]==board[2][2]){
            if(board[0][0]==player) return 99;
            else return -99;
        }
        if(board[0][2]!='_'&&board[0][2]==board[1][1]&&board[1][1]==board[2][0]){
            if(board[0][2]==player) return 99;
            else return -99;
        }
        return 0;
    }

//    public static void main(String[] args) {
//         int[]arr={
//                 1,1,0,
//                 0,0,1,
//                 1,0,1};
//        System.out.println(findBestMove(arr));
//    }

}
