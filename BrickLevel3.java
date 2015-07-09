import javafx.scene.paint.Color;


public class BrickLevel3 extends BrickLevel2{
	private boolean takeThreeHits;

	BrickLevel3(int x, int y) {
		super(x, y);
		double random = Math.random();
		if (random <= 0.1){
			takeThreeHits = true;
			takeTwoHits = true;
			this.setFill(Color.BLACK);
		}
	}
	
	@Override
	public void setDestroyed(boolean destroyed) {
		if (takeThreeHits == true){
			switch(y){
			case 60:
			case 82: this.setFill(Color.CRIMSON);
			break;
			case 104:
			case 126:
			case 148: this.setFill(Color.DARKGREEN);
			break;
			case 170:
			case 192: this.setFill(Color.GOLDENROD);
			break;
			case 214:
			case 236:
			case 258: this.setFill(Color.BLUE);
			}
			takeThreeHits = false;
		}else if(takeTwoHits == true){
			switch(y){
			case 60:
			case 82: this.setFill(Color.PINK);
			break;
			case 104:
			case 126:
			case 148: this.setFill(Color.LIGHTGREEN);
			break;
			case 170:
			case 192: this.setFill(Color.YELLOW);
			break;
			case 214:
			case 236:
			case 258: this.setFill(Color.AQUA);
			}
			takeTwoHits = false;
		}else{
			this.destroyed = destroyed;
		}
	}
}
