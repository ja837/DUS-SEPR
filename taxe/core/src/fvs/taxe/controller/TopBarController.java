package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.TaxeGame;
import fvs.taxe.replay.EndTurnAction;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.listeners.GameStateListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TopBarController {
    //This class controls what is displayed in the topBar, the primary method of informing the players of events that occur in game
    //It's very possible to move away from a topBar orientated design and more to dialogs as we have done, but we decided not to entirely due to the work required.
    public final static int CONTROLS_HEIGHT = 40;

    private Context context;
    private Color controlsColor = Color.LIGHT_GRAY;
    private TextButton endTurnButton;
    private TextButton replayButton;
    private TextButton restartReplayButton;
    private TextButton pauseReplayButton;
    private TextButton exitReplayButton;
    private TextButton skipThinkingButton;
    private Label flashMessage;

    public TopBarController(Context context) {
        this.context = context;
        //This creates a listener that changes the bar colour based on the state that the game is in
        context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                switch (state) {
                    case ANIMATING:
                        controlsColor = Color.LIGHT_GRAY;
                        break;

                    default:
                        controlsColor = Color.LIGHT_GRAY;
                        break;
                }
            }
        });

        createFlashActor();
    }

    private void createFlashActor() {
        flashMessage = new Label("", context.getSkin());
        flashMessage.setPosition(400, TaxeGame.HEIGHT - 24);
        context.getStage().addActor(flashMessage);
    }


    public void displayFlashMessage(String message, Color color) {
        //This method displays a message in the topBar for the default 1.75 seconds
        displayFlashMessage(message, color, 1.75f);
    }

    public void displayFlashMessage(String message, Color color, float time) {
        //This method also displays a message in the topBar, but for the amount of time specified in the parameters
        flashMessage.setText(message);
        flashMessage.setColor(color);
        flashMessage.addAction(sequence(delay(time), fadeOut(0.25f)));
    }

    public void displayMessage(String message, Color color){
        //This method sets a permanent message until it is overwritten
        flashMessage.setText(message);
        flashMessage.setColor(color);
    }

    public void clearMessage(){
        //This method clears the current message
        flashMessage.setText("");
        flashMessage.setColor(Color.LIGHT_GRAY);
    }

    public void drawBackground() {
        TaxeGame game = context.getTaxeGame();
        //This method draws the topBar onto the game screen
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(controlsColor);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, TaxeGame.WIDTH, CONTROLS_HEIGHT);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, TaxeGame.WIDTH, 1);
        game.shapeRenderer.end();
    }
    
    public void UpdateContext(Context context){
    	this.context = context;
    }

    public void addEndTurnButton() {
    	
    	if (endTurnButton != null){
    		endTurnButton.remove();
    	}
    	
    	
        //This method adds an endTurn button to the topBar which allows the user to end their turn
        endTurnButton = new TextButton("End Turn", context.getSkin());
        endTurnButton.setPosition(TaxeGame.WIDTH - 100.0f, TaxeGame.HEIGHT - 33.0f);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //This sets the turn to be over in the backend
                context.getMainGame().getPlayerManager().turnOver(context);
                
                //Record this action in the replay manager.
                if (!context.getReplayManager().isReplaying()){               	
                    long timestamp = context.getGameLogic().getReplayManager().getCurrentTimeStamp();
                    EndTurnAction action = new EndTurnAction(context, timestamp);
                    context.getGameLogic().getReplayManager().addAction(action);
                }
               
            }
        });

        context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                //This sets whether or not the endTurn button is displayed based on the state of the game
                //This is important as it prevents players from ending their turn mid placement or mid routing
                if (state == GameState.NORMAL) {
                    endTurnButton.setVisible(true);
                } else {
                    endTurnButton.setVisible(false);
                }
            }
        });

        context.getStage().addActor(endTurnButton);
    }
    
    
    public void addReplayButton(){
    	
    	if (replayButton != null){
    		replayButton.remove();
    	}
    	
    	//This method adds a replay button to the topBar which allows the user to start a replay
        replayButton = new TextButton("Replay", context.getSkin());
        replayButton.setPosition(10.0f, TaxeGame.HEIGHT - 33.0f);
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {          	
                context.enterReplay();
                replayButton.remove();
                endTurnButton.remove();
            }
        });
        
        context.getStage().addActor(replayButton);
    }
    
    public void addExitReplayButton(){
    	//This method adds an exit replay button to the topBar which allows the user stop the replay
    	if (exitReplayButton != null){
    		exitReplayButton.remove();
    	}
        exitReplayButton = new TextButton("Exit Replay", context.getSkin());
        
        exitReplayButton.setPosition(10.0f, TaxeGame.HEIGHT - 33.0f);
        exitReplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {            	
                context.exitReplay();
                exitReplayButton.remove();
                skipThinkingButton.remove();
                restartReplayButton.remove();
                pauseReplayButton.remove();
            }
        });
        
        context.getStage().addActor(exitReplayButton);
        
    }
    
    public void addRestartReplayButton(){
    	//This method adds an exit replay button to the topBar which allows the user stop the replay
    	if (restartReplayButton != null){
    		restartReplayButton.remove();
    	}
    	restartReplayButton = new TextButton("Restart", context.getSkin());
    	float x = 10f + exitReplayButton.getWidth() + 10f;
    	restartReplayButton.setPosition(x, TaxeGame.HEIGHT - 33.0f);
    	restartReplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {            	
                context.restartReplay();
            }
        });
        
        context.getStage().addActor(restartReplayButton);
        
    }
    
    public void addPauseReplayButton(){
    	//This method adds an exit replay button to the topBar which allows the user stop the replay
    	if (pauseReplayButton != null){
    		pauseReplayButton.remove();
    	}
    	pauseReplayButton = new TextButton("Pause", context.getSkin());
    	
    	if (context.getReplayManager().isPaused()){
    		pauseReplayButton.setText("Play");
    	}
    	else{
    		pauseReplayButton.setText("Pause");
    	}
    	
    	float x = 10f + exitReplayButton.getWidth() + 10f + restartReplayButton.getWidth() + 10f;
    	pauseReplayButton.setPosition(x, TaxeGame.HEIGHT - 33.0f);
    	pauseReplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (context.getReplayManager().isPaused()){
            		context.getReplayManager().resumeReplay();
            		pauseReplayButton.setText("Pause");
            	}
            	else{
            		context.getReplayManager().pauseReplay();
            		pauseReplayButton.setText("Play");
            	}
                
            }
        });
        
        context.getStage().addActor(pauseReplayButton);
        
    }
    
    public void addSkipThinkingTimeButton(){
    	//This method adds a skip thinking time button to the topBar which allows the user stop the replay
    	if (skipThinkingButton != null){
    		skipThinkingButton.remove();
    	}
    	skipThinkingButton = new TextButton("Skip Thinking Time", context.getSkin());
    	if (!context.getReplayManager().isSkipThinkingTime( )){
    		skipThinkingButton.setColor(Color.RED);
    	}else{
    		skipThinkingButton.setColor(Color.GREEN);
    	}
    	
    	float x = 10f + exitReplayButton.getWidth() + 10f + restartReplayButton.getWidth() + 10f + pauseReplayButton.getWidth() + 10f;
    	skipThinkingButton.setPosition(x, TaxeGame.HEIGHT - 33.0f);
    	skipThinkingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {    
            	if (context.getReplayManager().isSkipThinkingTime()){
            		context.getReplayManager().setSkipThinkingTime(false);
            		skipThinkingButton.setColor(Color.RED);
            		
            		//Set the time since the replay started to the last action that was played.
            		context.getReplayManager().setTimeSinceReplayStarted(context.getReplayManager().getPreviousAction().getTimeStamp());
            	}
            	else{
            		context.getReplayManager().setSkipThinkingTime(true);
            		skipThinkingButton.setColor(Color.GREEN);
            		
            	}
                
            }
        });
        
        context.getStage().addActor(skipThinkingButton);
        
    }
}
