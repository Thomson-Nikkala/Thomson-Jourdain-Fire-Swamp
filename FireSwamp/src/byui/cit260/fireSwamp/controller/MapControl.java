/** *************************************************
 * MapControl Class                                *
 *                                                 *
 ************************************************** */
package byui.cit260.fireSwamp.controller;

import byui.cit260.fireSwamp.enums.DangerType;
import byui.cit260.fireSwamp.enums.ItemType;
import byui.cit260.fireSwamp.exceptions.MapControlException;
import byui.cit260.fireSwamp.model.Item;
import byui.cit260.fireSwamp.model.Location;
import byui.cit260.fireSwamp.model.Map;

/**
 *
 * @authors Didier Jourdain and Nikkala Thomson
 */
public class MapControl {

    /**
     *
     * @param location
     */
    
    public static void deleteItemFromLocation(Location location) {
        Item item = new Item();
        item.setItemType(ItemType.NONE);
        location.setItem(item);
    }

    public static String checkLook(Location playerLocation, Map map) throws MapControlException {
        String strDanger = "";
        String strItem = "";

        
        int row = playerLocation.getLocationRow();
        int col = playerLocation.getLocationColumn();
        
        
        //check for all nearby LightningSand
        //Look north
        if (row > 0) {
            Location northLocation = map.getLocationAt(row - 1, col);
            if (northLocation.getDanger().getDangerType() == DangerType.LIGHTNINGSAND) {
                strDanger += "\nYou see a strange patch of sand to the north.";
            }
        }
        //Look south
        if (row < (Map.ROWS - 1)) {
            Location southLocation = map.getLocationAt(row + 1, col);
            if (southLocation.getDanger().getDangerType() == DangerType.LIGHTNINGSAND) {
                strDanger += "\nYou see a strange patch of sand to the south.";
            }
        }
        //Look west
        if (col > 0) {
            Location westLocation = map.getLocationAt(row, col - 1);
            if (westLocation.getDanger().getDangerType() == DangerType.LIGHTNINGSAND) {
                strDanger += "\nYou see a strange patch of sand to the west.";
            }
        }
        //Look east
        if (col < (Map.COLUMNS - 1)) {
            Location eastLocation = map.getLocationAt(row, col + 1);
            if (eastLocation.getDanger().getDangerType() == DangerType.LIGHTNINGSAND) {
                strDanger += "\nYou see a strange patch of sand to the east.";
            }
        }
        
        //check for item at location and print it out if it exists
        Item itemAtPlayerPosition = map.getLocationAt(row, col).getItem();        
        ItemType foundItemType = itemAtPlayerPosition.getItemType();
        if (foundItemType == ItemType.NONE) {
            strItem += "\nThere are no items here.";
        } else {
            strItem += "\nAlso, on the ground, you see ";
            
            switch (foundItemType) {
                case BUCKET:
                    strItem += "a wooden bucket full of water.";
                    break;
                case POTION:
                    strItem += "a shimmery purple healing potion.";
                    break;
                case ROPE:
                    strItem += "a long section of sturdy rope.";
                    break;
                default:
                    break;
            }
        }
        if (strDanger == "") {
            return strItem;
        }
        else {
            return strDanger + strItem;
        }
    }

    public static String checkSmell(Location testLocation, Map map) throws MapControlException {
        String strDanger = "";
        //check for nearby ROUS
        int row = testLocation.getLocationRow();
        int col = testLocation.getLocationColumn();

        //check north
        if (row > 0) {
            if (map.getLocationAt(row - 1, col).getDanger().getDangerType() == DangerType.ROUS) {
                strDanger = "\nYou smell an odor reminiscent of wet dog and rotting garbage wafting from the north.";
            }
        }
        //check south
        if (row < (Map.ROWS - 1)) {
            if (map.getLocationAt(row + 1, col).getDanger().getDangerType() == DangerType.ROUS) {
                strDanger += "\nYou smell an odor reminiscent of wet dog and rotting garbage wafting from the south.";
            }
        }
        //check west
        if (col > 0) {
            if (map.getLocationAt(row, col - 1).getDanger().getDangerType() == DangerType.ROUS) {
                strDanger += "\nYou smell an odor reminiscent of wet dog and rotting garbage wafting from the west.";
            }
        }
        //check east
        if (col < (Map.COLUMNS - 1)) {
            if (map.getLocationAt(row, col + 1).getDanger().getDangerType() == DangerType.ROUS) {
                strDanger += "\nYou smell an odor reminiscent of wet dog and rotting garbage wafting from the east.";
            }
        }
        
        if (strDanger == "") {
            strDanger = "\nYou smell no particular odor.";
        }
        
        return strDanger;

    }

    public static String checkListen(Location testLocation, Map map) throws MapControlException {
        String strDanger = "";
        //check for nearby Fire Spurt
        //check for nearby LightningSand
        int row = testLocation.getLocationRow();
        int col = testLocation.getLocationColumn();

        //check north
        if (row > 0) {
            if (map.getLocationAt(row - 1, col).getDanger().getDangerType() == DangerType.FLAMESPURT) {
                strDanger += "\nYou hear a popping noise to the north.";
            }
        }
        //check south
        if (row < (Map.ROWS - 1)) {
            if (map.getLocationAt(row + 1, col).getDanger().getDangerType() == DangerType.FLAMESPURT) {
                strDanger += "\nYou hear a popping noise to the south.";
            }
        }
        //check west
        if (col > 0) {
            if (map.getLocationAt(row, col - 1).getDanger().getDangerType() == DangerType.FLAMESPURT) {
                strDanger += "\nYou hear a popping noise to the west.";
            }
        }
        //check east
        if (col < (Map.COLUMNS - 1)) {
            if (map.getLocationAt(row, col + 1).getDanger().getDangerType() == DangerType.FLAMESPURT) {
                strDanger += "\nYou hear a popping noise to the east.";
            }
        }
        
        if (strDanger == "") {
            strDanger = "\nYou just hear your own breathing. Calm down.";
        }
        
        return strDanger;
    }

}
