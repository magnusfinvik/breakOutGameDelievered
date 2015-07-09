import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Level extends Pane{

	ArrayList<Brick> bricks = new ArrayList<Brick>();
	Racket racket = new Racket();
	Ball ball = new Ball(racket.getPosX(), racket.getPosY());
	TextField timeFrame = new TextField();
	TextField numberOfBallsLeft = new TextField();
	Label intel = new Label("PRESS SPACE TO START");
	Label clearedLevel = new Label("PRESS SPACE ");
	Label lost = new Label("PRESS SPACE LOSER");
	double timeSpent;
	TextField timeScore = new TextField("Time spendt: " + Double.toString(timeSpent));
	private int wishToMoveRacket = 0;
	private int ballsLeft = 3;
	private double time;
	private int levelCounter = 0;
	private AnimationTimer timer;
	

	public Level (){
		showLevel();
		initTimeline();
		spacePressed();
	}

	private void initTimeline(){
		timer = new AnimationTimer(){

			@Override
			public void handle(long now) {
				ball.moveBall();
				keyPressed();
				if(wishToMoveRacket == 1){
					racket.moveRacket(8);
				}
				if(wishToMoveRacket == -1){
					racket.moveRacket(-8);
				}
				checkCollision();
				timeFrame.setText(Double.toString(elapsedTime()));
				numberOfBallsLeft.setText("number of balls left: " + ballsLeft);
			}
		};
	}

	private void showLevel() {
		setLevel();
		showIntel();
		showElapsedTime();
		showBallsLeft();
		getChildren().add(racket);
		getChildren().add(ball);
		getChildren().add(timeFrame);
		getChildren().add(numberOfBallsLeft);
		getChildren().add(intel);
	}

	private void setLevel(){
		if(levelCounter == 0){
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 15; j++){
					bricks.add(new Brick(50 + j * 47, 60 + i * 22));
				}
			}
		}else if(levelCounter == 1){
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 15; j++){
					bricks.add(new BrickLevel2(50 + j * 47, 60 + i * 22));
				}
			}
		}else if(levelCounter == 2){
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 15; j++){
					bricks.add(new BrickLevel3(50 + j * 47, 60 + i * 22));
				}
			}
		}
		for(Brick brick: bricks){
			if(brick.isDestroyed() == false){
				getChildren().add(brick);
			}
		}
	}

	private void keyPressed() {
		this.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.RIGHT) {
				wishToMoveRacket = 1;
			}
			else if(e.getCode() == KeyCode.LEFT){
				wishToMoveRacket = -1;
			}
			this.setOnKeyReleased(k -> {
				wishToMoveRacket = 0;
			});
		});
	}

	private void spacePressed(){
		this.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.SPACE) {
				timer.start();
				startTime();
				getChildren().remove(intel);
			}
		});
	}

	private void nextLevel(){
		this.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.SPACE){
				timer.start();
				getChildren().remove(clearedLevel);
			}
		});
	}

	private void checkCollisionWithRacket() {
		if(ball.getBottom() <= (racket.getPosY()+1) && ball.getBottom() >= (racket.getPosY()-2)  
				&& ball.getX() + 3 >= racket.getPosX() && ball.getX() -3 <= (racket.getPosX()+racket.getwidth())){

			ball.switchDirectionY();
			ball.setSpeed(wishToMoveRacket);

		}else if(ball.getLeft() <= (racket.getPosX() + racket.getwidth() + 2) && ball.getLeft() >= (racket.getPosX() + racket.getwidth() - 2)
				&& (ball.getBottom() >= (racket.getPosY() - 1) && ball.getBottom() <= (racket.getPosY() + racket.getheigth() + 5))){

			ball.setBallDirectionX(3);
			ball.setBallDirectionY(-3);
			
		}else if(ball.getBottom() >= (racket.getPosY() - 1) && ball.getBottom() <= (racket.getPosY() + racket.getheigth() + 5)
				&& ball.getRigth() <= (racket.getPosX() +2)  && ball.getRigth() >= (racket.getPosX() -2)){
			
			ball.setBallDirectionX(-3);
			ball.setBallDirectionY(-3);
			
		}
	}

	private void checkCollisionWithBrick(){
		for(int i = 0; i < bricks.size(); i++){
			if(bricks.get(i).isDestroyed() == true){
				getChildren().remove(bricks.get(i));
				bricks.remove(i);
			}
		}
		for(int i = 0; i < bricks.size(); i++){
			if((ball.getX() + 6) >= bricks.get(i).getTranslateX() && (ball.getX() -6) <= (bricks.get(i).getTranslateX() + bricks.get(i).getWidth())
					&& ball.getTop() <= (bricks.get(i).getTranslateY() + bricks.get(i).getHeight() +1) 
					&& ball.getTop() >= (bricks.get(i).getTranslateY() + bricks.get(i).getHeight() - 3)){

				bricks.get(i).setDestroyed(true);
				ball.setBallDirectionY(3);

			}else if((ball.getX() + 6) >= bricks.get(i).getTranslateX() && (ball.getX() - 6) <= (bricks.get(i).getTranslateX() + bricks.get(i).getWidth())
					&& ball.getBottom() <= (bricks.get(i).getTranslateY() +1) && ball.getBottom() >= (bricks.get(i).getTranslateY() - 3)){

				bricks.get(i).setDestroyed(true);
				ball.setBallDirectionY(-3);

			}else if((ball.getY() + 6) >= bricks.get(i).getTranslateY() && (ball.getY() - 6) <= (bricks.get(i).getTranslateY() + bricks.get(i).getHeight())
					&& ball.getRigth() <= (bricks.get(i).getTranslateX() + 1) && ball.getRigth() >= (bricks.get(i).getTranslateX() - 3)){

				bricks.get(i).setDestroyed(true);
				ball.setBallDirectionX(-3);

			}else if((ball.getY() + 6) >= bricks.get(i).getTranslateY() && (ball.getY() - 6) <= (bricks.get(i).getTranslateY() + bricks.get(i).getHeight())
					&& ball.getLeft() <= (bricks.get(i).getTranslateX() + bricks.get(i).getWidth() + 1)
					&& ball.getLeft() >= (bricks.get(i).getTranslateX() + bricks.get(i).getWidth() - 3)){

				bricks.get(i).setDestroyed(true);
				ball.setBallDirectionX(3);
			}	
		}
	}

	private void checkCollision(){
		if (ball.getY() > 600){
			ballsLeft--;
			if(ballsLeft == 0){
				lost.setLayoutX(240);
				lost.setLayoutY(270);
				lost.setWrapText(true);
				lost.setStyle("-fx-font-size:40;");
				stopTime().setText("Time spendt: " + Double.toString(elapsedTime()));
				getChildren().add(stopTime());
				getChildren().add(lost);
				timer.stop();
				tryAgain();
			}else{
				ball.setX(racket.getPosX() + 45);
				ball.setY(racket.getPosY() - 10);
			}
		}
		if (bricks.size() == 0){
			if(levelCounter++ == 3){
				Label victory = new Label("VICTORY!!!");
				victory.setLayoutX(280);
				victory.setLayoutY(265);
				victory.setWrapText(true);
				victory.setTextFill(Color.DARKORANGE);
				victory.setStyle("-fx-font-size:70;");
				getChildren().add(stopTime());
				getChildren().add(victory);
				timer.stop();
			}else{
				clearedLevel.setLayoutX(250);
				clearedLevel.setLayoutY(500);
				clearedLevel.setStyle("-fx-font-size:20;");
				getChildren().add(clearedLevel);
				setLevel();
				timer.stop();
				nextLevel();
				ballsLeft = 3;
				ball.setX(racket.getPosX() + 45);
				ball.setY(racket.getPosY() -10);
				ball.setCenterX(racket.getPosX() + 45);
				ball.setCenterY(racket.getPosY() - 10);
			}
		}
		if (ball.getY() < 300){
			checkCollisionWithBrick();
		}else if (ball.getY() > 540){
			checkCollisionWithRacket();

		}
	}

	private void tryAgain() {
		this.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.SPACE) {
				resetState();
				levelCounter--;
				spacePressed();
			}
		});
	}

	public void resetState(){
		getChildren().removeAll(bricks);
		getChildren().remove(lost);
		bricks.clear();
		getChildren().remove(timeScore);
		resetTime();
		racket.setPosX(380);
		ball.setBallDirectionX(-3);
		ball.setBallDirectionY(-3);
		ball.setX(racket.getPosX() + 45);
		ball.setY(racket.getPosY() - 7);
		ballsLeft = 3; 
	}

	private  void showElapsedTime() {
		timeFrame.setLayoutY(570);
		timeFrame.setLayoutX(10);
	}

	public void startTime() {
		time = System.currentTimeMillis();
	}

	public TextField stopTime(){
		timeSpent = elapsedTime();
		timeScore.setLayoutX(10);
		timeScore.setLayoutY(570);
		return timeScore;
	}

	public double elapsedTime(){
		long now= System.currentTimeMillis();
		return (now - time) / 1000.0;
	}

	private void resetTime(){
		time = 0;
	}

	private void showBallsLeft() {
		int x = 610;
		int y = 570;
		numberOfBallsLeft.setLayoutX(x);
		numberOfBallsLeft.setLayoutY(y);
	}

	private void showIntel() {
		intel.setLayoutX(320);
		intel.setLayoutY(500);
		intel.setStyle("-fx-font-size:20;");
	}
}

