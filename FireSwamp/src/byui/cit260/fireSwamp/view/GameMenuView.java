/**
 * *************************************************
 * GameMenuView Class                              *
 *                                                 *
 **************************************************
 */
package byui.cit260.fireSwamp.view;

import byui.cit260.fireSwamp.controller.*;
import byui.cit260.fireSwamp.enums.Direction;
import byui.cit260.fireSwamp.enums.ItemType;
import byui.cit260.fireSwamp.exceptions.*;
import byui.cit260.fireSwamp.model.Item;
import byui.cit260.fireSwamp.model.Location;
import byui.cit260.fireSwamp.model.Map;
import byui.cit260.fireSwamp.model.Player;
import fireswamp.FireSwamp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Didier Jourdain and Nikkala Thomson
 */
public class GameMenuView extends View {

    public GameMenuView() {

        super("\n******************************************************"
                + "\n* GAME MENU                                          *"
                + "\n* V - View map                                       *"
                + "\n* O - lOok                                           *"
                + "\n* L - Listen                                         *"
                + "\n* M - sMell                                          *"
                + "\n* T - Take item                                      *"
                + "\n* I - write Inventory to file (Nikkala week 12)      *"
                + "\n* D - write map stats to file (Didier week 12)       *"
                + "\n* N - move North                                     *"
                + "\n* E - move East                                      *"
                + "\n* W - move West                                      *"
                + "\n* S - move South                                     *"
                + "\n* H - Help                                           *"
                + "\n* B - Back                                           *"
                + "\n* P - Win Game (for testing purposes)                *"
                + "\n******************************************************"
                + "\n\n Enter command: ");
    }

    @Override
    public boolean doAction(String choice) {

        choice = choice.toUpperCase();

        switch (choice) {
            case "V":
                this.displayMap();
                break;
            case "O":
                this.look();
                break;
            case "L":
                this.listen();
                break;
            case "M":
                this.smell();
                break;
            case "T":
                this.takeItem();
                break;
            case "I":
                this.writeInventory();
                break;
            case "N": {
                try {
                    this.moveNorth();
                } catch (DangerControlException ex) {
                    Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "E": {
                try {
                    this.moveEast();
                } catch (DangerControlException ex) {
                    Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "W": {
                try {
                    this.moveWest();
                } catch (DangerControlException ex) {
                    Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "S": {
                try {
                    this.moveSouth();
                } catch (DangerControlException ex) {
                    Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "H":
                this.callHelpMenu();
                break;
            case "P":
                this.callWinGameView();
                break;
            case "D":
                this.mapStats();
                break;
            default:
                this.console.println("\n*** Invalid selection *** Try again");
                break;
        }

        return false;
    }

    private void displayMap() {

        Map map = FireSwamp.getCurrentGame().getGameMap();

        // Print map header
        this.console.println();
        for (int col = 0; col < Map.COLUMNS; col++) {
            int colIndex = col + 1;
            if (col == 0) {
                this.console.print("   | " + colIndex + " |");
            } else {
                this.console.printf(" " + colIndex + " |");
            }

        }
        this.console.println();
        this.console.println("   +---+---+---+---+---|");
        try {
            for (int row = 0; row < Map.ROWS; row++) {
                int rowIndex = row + 1;
                for (int col = 0; col < Map.COLUMNS; col++) {
                    char locationType;
                    locationType = map.getLocationAt(row, col).getDanger().getDangerType().toString().charAt(0);
                    if (col == 0) {
                        this.console.print(rowIndex + "  |");
                    }
                    this.console.print(locationType);
                    if (map.getLocationAt(row, col).getItem().getItemType() != ItemType.NONE) {
                        this.console.print(map.getLocationAt(row, col).getItem().getItemName().charAt(0) + " |");
                    } else {
                        this.console.print("  |");
                    }

                }
                this.console.println();
                this.console.println("   +---+---+---+---+---|");

            }
        } catch (Exception e) {
            Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, e);
        }

        if (FireSwamp.getPlayer().getPlayerPosition() != null) {
            Location playerLoc = FireSwamp.getPlayer().getPlayerPosition();
            this.console.println("\n** "
                    + FireSwamp.getPlayer().getPlayerName()
                    + " is at row " + playerLoc.getLocationRowForPeople()
                    + " and at column " + playerLoc.getLocationColumnForPeople());
        }

        //map.mapStatistics();
        //this temporary section is for testing the checkInventory function
        ArrayList<Item> inventory = new ArrayList<>();
        InventoryControl inControl = new InventoryControl();

        Item rope = new Item();
        rope.setItemType(ItemType.ROPE);
        rope.setItemDescription("A sturdy rope");
        rope.setItemName("rope");
        inventory.add(rope);

        Item potion = new Item();
        potion.setItemType(ItemType.POTION);
        potion.setItemDescription("A healing potion");
        potion.setItemName("potion");
        inventory.add(potion);

        FireSwamp.getPlayer().setPlayerInventory(inventory);

        try {
            int ropePositionInList = inControl.checkInventory(inventory, ItemType.ROPE);
            this.console.println("\n inventory position is " + ropePositionInList);
        } catch (InventoryControlException ice) {
            this.console.println(ice.getMessage());
        }

        //end of testing checkInventory function
    }

    //look function checks for the sandy soil that indicates lightning sand
    private void look() {
        try {
            this.console.println(MapControl.checkLook(FireSwamp.getPlayer().getPlayerPosition(),
                    FireSwamp.getCurrentGame().getGameMap()));
        } catch (MapControlException ex) {
            Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //listen functiono checks for popping sound that precedes a flame spurt
    private void listen() {
        try {
            this.console.println(MapControl.checkListen(FireSwamp.getPlayer().getPlayerPosition(),
                    FireSwamp.getCurrentGame().getGameMap()));
        } catch (MapControlException ex) {
            Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //smell function checks for the wet dog smell of a ROUS
    private void smell() {
        try {
            this.console.println(MapControl.checkSmell(FireSwamp.getPlayer().getPlayerPosition(),
                    FireSwamp.getCurrentGame().getGameMap()));
        } catch (MapControlException ex) {
            Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void takeItem() {
        Map map = FireSwamp.getCurrentGame().getGameMap();
        Location location = FireSwamp.getPlayer().getPlayerPosition();
        ArrayList<Item> inventory = FireSwamp.getPlayer().getPlayerInventory();

        boolean isItem;
        isItem = MapControl.checkForItem(location);

        //if there is, add a copy of that item to the inventory, then delete it from the location
        if (isItem) {
            try {
                InventoryControl.addItemToInventory(location.getItem(), inventory);
                this.console.println("\nItem added to inventory.");
            } catch (InventoryControlException ex) {
                this.console.println(ex.getMessage());
            }
            MapControl.deleteItemFromLocation(location);
        } else {
            this.console.println("\nThere is no item here to take.");
        }

    }

    private void moveNorth() throws DangerControlException {
        Map map = FireSwamp.getCurrentGame().getGameMap();
        Player player = FireSwamp.getPlayer();
        try {
            MovementControl.movePlayerDirection(player, Direction.NORTH);
        } catch (MovementControlException me) {
            this.console.println(me.getMessage());
        }

    }

    private void moveEast() throws DangerControlException {
        Map map = FireSwamp.getCurrentGame().getGameMap();
        Player player = FireSwamp.getPlayer();
        try {
            MovementControl.movePlayerDirection(player, Direction.EAST);
        } catch (MovementControlException me) {
            this.console.println(me.getMessage());
        }
    }

    private void moveWest() throws DangerControlException {
        Map map = FireSwamp.getCurrentGame().getGameMap();
        Player player = FireSwamp.getPlayer();
        try {
            MovementControl.movePlayerDirection(player, Direction.WEST);
        } catch (MovementControlException me) {
            this.console.println(me.getMessage());
        }
    }

    private void moveSouth() throws DangerControlException {
        Map map = FireSwamp.getCurrentGame().getGameMap();
        Player player = FireSwamp.getPlayer();
        try {
            MovementControl.movePlayerDirection(player, Direction.SOUTH);
        } catch (MovementControlException me) {
            this.console.println(me.getMessage());
        }
    }

    private void callHelpMenu() {
        HelpMenuView helpMenu = new HelpMenuView();
        try {
            helpMenu.display();
        } catch (GameControlException gce) {
            this.console.println(gce.getMessage());
        }
    }

    private void goBack() {
        this.console.println("*** goBack() function called");
    }

    private void callWinGameView() {
        WinGameView winGame = new WinGameView();
        try {
            winGame.display();
        } catch (GameControlException ex) {
            Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeInventory() {
        String filePath = null;
        boolean valid = false;

        this.console.println("Enter the file path (including filename) for where the inventory list is to be saved:");
        while (!valid) {
            try {
                //prompt for player input

                filePath = this.keyboard.readLine();
                filePath = filePath.trim();

                if (filePath.length() < 1) {
                    this.console.println("\nInvalid value: value can not be blank");
                } else {
                    valid = true;
                }

            } catch (IOException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            //save inventory list to specified file
            WriteInventoryView.writeInventory(FireSwamp.getPlayer().getPlayerInventory(), filePath);
            this.console.println("\nInventory successfully written to file " + filePath + ".");
        } catch (GameControlException gce) {
            ErrorView.display("GameMenuView", gce.getMessage());

        }
    }

    private void mapStats() {
        try {
            String filePath = null;
            MapDetailsView mapDetailsView = new MapDetailsView();
            filePath = mapDetailsView.getInput();

            mapDetailsView.MapDetailsReport(filePath);
        } catch (GameControlException ex) {
            Logger.getLogger(GameMenuView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
