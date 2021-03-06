/** *************************************************
 * MainMenuView Class                              *
 *                                                 *
 ************************************************** */
package byui.cit260.fireSwamp.view;

import byui.cit260.fireSwamp.controller.GameControl;
import byui.cit260.fireSwamp.exceptions.GameControlException;
import byui.cit260.fireSwamp.exceptions.MapControlException;
import byui.cit260.fireSwamp.model.*;
import fireswamp.FireSwamp;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Didier Jourdain and Nikkala Thomson
 */
public class MainMenuView extends View {

    //Constructor
    public MainMenuView() {
        super("\n"
            + "\n------------------------------------------------------"
            + "\n| Main Menu                                          |"
            + "\n------------------------------------------------------"
            + "\nN - Start new game"
            + "\nL - Load saved game"
            + "\nS - Save game"
            + "\nX - eXit game"
            + "\n------------------------------------------------------"
            + "\n\nEnter command: ");
    }

    @Override
    public boolean doAction(String value) {

        value = value.toUpperCase();

        switch (value) {
            case "N":  // Start new game
                try {
                    this.startNewGame();
                } catch (MapControlException me) {
                    ErrorView.display(this.getClass().getName(), me.getMessage());
                }
                break;
            case "L": {
                try {
                    // Load saved game
                    this.loadSavedGame();
                } catch (GameControlException ex) {
                    Logger.getLogger(MainMenuView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "S":  // save Game
                this.saveGame();
                break;
            case "X":  // exit Game
                this.exitGame();
                break;
            default:
                ErrorView.display(this.getClass().getName(), "\n*** Invalid selection *** Try again");
                break;
        }

        return false;
    }

    private void loadSavedGame() throws GameControlException {

        // prompt for and get the name of the file to be saved
        LoadGameView loadGameView = new LoadGameView();
        String filePath = loadGameView.getInput();

        Game game = null;

        try (FileInputStream fips = new FileInputStream(filePath)) {
            ObjectInputStream input = new ObjectInputStream(fips);

            game = (Game) input.readObject();
        } catch (Exception e) {
            throw new GameControlException(e.getMessage());
        }

        FireSwamp.setCurrentGame(game);
        FireSwamp.setPlayer(FireSwamp.getCurrentGame().getGamePlayer());
        GameMenuView gameMenu = new GameMenuView();
        try {
            gameMenu.display();
        } catch (GameControlException ex) {
            Logger.getLogger(MainMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void exitGame() {
        try {
            this.console.println("Do you wish to save the game before exiting?  Y/N");
            String answer = null;
            answer = this.keyboard.readLine();
            answer.trim();
            if ((answer.toUpperCase().charAt(0)) == 'Y') {
                this.saveGame();
            }
            
            System.exit(0);
        }
        catch (IOException ex) {
            Logger.getLogger(MainMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startNewGame() throws MapControlException {
        GameControl gc = new GameControl();
        Player player = FireSwamp.getPlayer();
        gc.createNewGame(player);
        
        int startRow = FireSwamp.getCurrentGame().getGameMap().getMapEntranceRow();
        int startColumn = FireSwamp.getCurrentGame().getGameMap().getMapEntranceColumn();
        player.setPlayerPosition(FireSwamp.getCurrentGame().getGameMap().getLocationAt(startRow,startColumn));
        
        GameMenuView gameMenu = new GameMenuView();
        try {
            gameMenu.display();
        } catch (GameControlException ex) {
            Logger.getLogger(MainMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveGame() {
        //prompt for and get the name of the file path for saving
        SaveGameView saveGameView = new SaveGameView();
        String filePath = saveGameView.getInput();

        try {
            //save the game to the specified file
            GameControl.saveGame(FireSwamp.getCurrentGame(), filePath);

        } catch (GameControlException ex) {
            ErrorView.display("MainMenuView", ex.getMessage());
        }
    }

}
