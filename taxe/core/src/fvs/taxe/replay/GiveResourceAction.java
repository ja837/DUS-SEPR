package fvs.taxe.replay;

import fvs.taxe.controller.Context;
import gameLogic.player.Player;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

public class GiveResourceAction extends Action {
	
	
	int playerToGiveResourceTo;
	Resource resource;

	public GiveResourceAction(Context context, long timestamp, int p, Resource r) {
		super(context, timestamp);
		playerToGiveResourceTo = p;
		
		if (r instanceof Train){
			resource = new Train((Train) r);
		}else{
			resource = r;
		}
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void play() {
		System.out.println("Replaying an resource allocation action.");
		
		Player player = context.getGameLogic().getPlayerManager().getAllPlayers().get(playerToGiveResourceTo - 1);
		context.getGameLogic().getResourceManager().addResourceToPlayer(player, resource);
		
	}

	@Override
	public String toString() {
		
		return "Resource Allocation Action, giving " + resource.toString() + " to Player " + playerToGiveResourceTo + super.toString();
	}

}
