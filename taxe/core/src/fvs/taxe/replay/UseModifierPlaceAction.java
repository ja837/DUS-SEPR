package fvs.taxe.replay;

import java.util.List;

import com.badlogic.gdx.Gdx;

import fvs.taxe.actor.TrainActor;
import fvs.taxe.controller.Context;
import fvs.taxe.controller.StationController;
import fvs.taxe.controller.TrainController;
import fvs.taxe.controller.TrainMoveController;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.map.Connection;
import gameLogic.map.Station;
import gameLogic.player.Player;
import gameLogic.resource.Modifier;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

public class UseModifierPlaceAction extends Action {
	
	

	Modifier modifier;

	public UseModifierPlaceAction(Context context, long timestamp, Modifier m) {
		super(context, timestamp);

		modifier = m;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void play() {
		System.out.println("Replaying an modifier use action.");

		context.getGameLogic().getMap().addConnection(modifier.getStation1(), modifier.getStation2());
		
		//The modifiers is removed from the player's inventory as it has been used
		Player currentPlayer = context.getGameLogic().getPlayerManager().getCurrentPlayer();
		
		Modifier m = null;
		
		for (Resource r : currentPlayer.getResources()){
			if (r instanceof Modifier){
				m = (Modifier) r;
				
			}
		}
		
		currentPlayer.removeResource(m);
	}

	@Override
	public String toString() {
		
		return "Modifier use (add) Action, adding a connction between " + modifier.getStation1() + " and " + modifier.getStation2() + super.toString();
	}

}
