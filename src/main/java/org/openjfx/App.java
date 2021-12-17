package org.openjfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.IOException;
import org.openjfx.AI.*;
/**
 * JavaFX App
 */
public class App extends Application {
    //gui variables
    private static Scene scene;
    private GridPane gridPane = new GridPane();
    private BorderPane borderPane = new BorderPane();
    private Label title = new Label("TicTacToe");
    private Button restartBtn = new Button("Reset");
    Font font = Font.font("Roboto", FontWeight.BOLD,30);
    //Buttons array
    private Button[] buttons = new Button[9];

    //logical variables
    boolean gameOver = false;
    int activePlayer = 0;
    int makeActive = 1;
    int[] gameState = {3,3,3,3,3,3,3,3,3};
    int[][] winningPositions = {
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6}
    };



    @Override
    public void start(Stage stage) throws IOException {
        this.createGUI();
        this.handleEvent();
        
        Scene scene = new Scene( borderPane,550,650);
        stage.setScene(scene);
        stage.show();
    }

    private void createGUI() {
        //Setting Font
        title.setFont(font);
        title.setTextFill(Color.PURPLE);
        restartBtn.setFont(font);
        restartBtn.setTextFill(Color.PURPLE);
        restartBtn.setDisable(true);

        //Setting title at top and restart button at bottom
        borderPane.setTop(title);
        borderPane.setBottom(restartBtn);
        borderPane.setAlignment(title, Pos.CENTER);
        borderPane.setAlignment(restartBtn,Pos.CENTER);
        borderPane.setPadding(new Insets(20,20,20,20));

        //Setting up buttons
        int label = 0;
        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                Button tmpBtn = new Button();
                tmpBtn.setFont(font);
                tmpBtn.setId(label+"");
                tmpBtn.setPrefHeight(150);
                tmpBtn.setPrefWidth(150);
                gridPane.add(tmpBtn,j,i);
                gridPane.setAlignment(Pos.CENTER);
                buttons[label++] = tmpBtn;
            }
        }
        borderPane.setCenter(gridPane);
    }

    private void handleEvent(){
        restartBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(int i=0; i<9; ++i){
                    gameState[i] = 3;
                    buttons[i].setText("");
                    buttons[i].setGraphic(null);
                    buttons[i].setBackground(null);
                    buttons[i].setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths((1)))));
                }
                gameOver = false;
                restartBtn.setDisable(true);
                activePlayer = (makeActive==1)?1:0;
                makeActive = (makeActive==1)?0:1;
                if(activePlayer==1){
                    int bestPos = AI.findBestMove(gameState);
//                    System.out.println(bestPos);
                    buttons[bestPos].setGraphic(new ImageView((new Image("file:src/main/resources/img/cross.jpeg"))));
                    gameState[bestPos] = activePlayer;
                    checkWinner();
                    activePlayer = 0;
                }
            }
        });
        for(Button btn:buttons){
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Button curBtn = (Button) actionEvent.getSource();
                        int btnId = Integer.parseInt(curBtn.getId());
                        if(gameOver){
                            Alert alrt = new Alert(Alert.AlertType.ERROR);
                            alrt.setTitle("alert");
                            alrt.setContentText("Game Over!!!\nClick Restart to Play again.");
                            alrt.show();
                        }
                        else{
                            if(gameState[btnId]==3 && activePlayer==0){
                                curBtn.setGraphic(new ImageView((new Image("file:src/main/resources/img/zero.jpeg"))));
                                gameState[btnId] = activePlayer;
                                checkWinner();
                                activePlayer = 1;
                                int bestPos = AI.findBestMove(gameState);
                                if(bestPos<0){
                                    checkWinner();
                                }
//                                System.out.println(bestPos);
                                if(bestPos>=0)buttons[bestPos].setGraphic(new ImageView((new Image("file:src/main/resources/img/cross.jpeg"))));
                                if(bestPos>=0)gameState[bestPos] = activePlayer;
                                checkWinner();
                                activePlayer = 0;
                            }
                            else{
                                Alert alrt = new Alert(Alert.AlertType.ERROR);
                                alrt.setTitle("alert");
                                alrt.setContentText("Grid is already marked!");
                                alrt.show();
                            }
                        }
                    }
                });
        }
    }
    public void checkWinner(){
        if(!gameOver){
            boolean f = true;
            for(int x:gameState){
                if(x==3){
                    f = false;break;
                }
            }
            if(f){
                restartBtn.setDisable(false);
                gameOver = true;
                Alert alrt = new Alert(Alert.AlertType.ERROR);
                alrt.setTitle("Game Over");
                alrt.setContentText("Match Tied!");
                alrt.show();
            }
            for(int[] pos:winningPositions){
                if((!f) && gameState[pos[1]]!=3 && gameState[pos[0]]==gameState[pos[1]] && gameState[pos[1]]==gameState[pos[2]]){
                    buttons[pos[0]].setBackground((new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY,Insets.EMPTY))));
                    buttons[pos[1]].setBackground((new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY,Insets.EMPTY))));
                    buttons[pos[2]].setBackground((new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY,Insets.EMPTY))));
                    Alert alrt = new Alert(Alert.AlertType.ERROR);
                    alrt.setTitle("Game Over");
                    alrt.setContentText(((activePlayer==1)?'X':'O')+" has won the game!");
                    alrt.show();
                    gameOver=true;
                    restartBtn.setDisable(false);
                    break;
                }
            }
        }
    }
    public static void main(String[] args) {
        launch();
    }

}