package byui.cit260.fireSwamp.view;

import byui.cit260.fireSwamp.controller.DangerControl;
import byui.cit260.fireSwamp.controller.InventoryControl;
import byui.cit260.fireSwamp.enums.ItemType;
import byui.cit260.fireSwamp.exceptions.DangerControlException;
import byui.cit260.fireSwamp.exceptions.GameControlException;
import byui.cit260.fireSwamp.exceptions.InventoryControlException;
import byui.cit260.fireSwamp.model.Item;
import fireswamp.FireSwamp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Didier Jourdain and Nikkala Thomson
 */
public class FlameSpurtView extends View {

    private double pyramidLength;
    private double pyramidWidth;
    private double pyramidHeight;
    private double volumeGuessed;
    private double volumeComputed;

    public FlameSpurtView(double length, double width, double height) throws DangerControlException {
        
        super("\n************************************************************"
            + "\n**  You stand before a potential fire spurt. If you don't go"
            + "\n**  around it very carefully you will be toast unless you have"
            + "\n**  a bucket of water to throw at it.");
        
        DangerControl dc = new DangerControl();

        this.pyramidLength = length;
        this.pyramidWidth = width;
        this.pyramidHeight = height;
        this.volumeComputed = dc.calcFlameSpurtAnswer(length, width, height);
    }
    
    @Override
    public void display() {
        //Display the problem to be solved
        this.console.println(this.message + "\n** To avoid your demise, answer this math problem:"
                           + "\n****************************************************"
                           + "\n>>   What is the volume of a Pyramid of"
                           + "\n>>   " + pyramidLength + " feet long,"
                           + "\n>>   " + pyramidWidth + " feet wide, and"
                           + "\n>>   " + pyramidHeight + " feet tall ?");
        
        //Collect input
        String input = getInput();
        
        //Validate input
        boolean isCorrect = doAction(input);
        
        //Display results
        if (isCorrect) {
            this.console.println("You made it across");
        }
        else {
            //Check for bucket of water (itemType 2) 
            InventoryControl inControl = new InventoryControl();
            try {
                ArrayList<Item> inventory = FireSwamp.getPlayer().getPlayerInventory();                
                inControl.checkInventory(inventory, ItemType.BUCKET);
            } catch (InventoryControlException ice) {
                this.console.println(ice.getMessage());
                this.console.println("Alas, that's incorrect, and you have no bucket "
                             + " of water to repel the flames which roast you."
                             + "\nThe correct answer was: "
                             + volumeComputed + "\n");
                LoseMenuView loseView = new LoseMenuView();
                try {
                    loseView.display();
                } catch (GameControlException ex) {
                    Logger.getLogger(FlameSpurtView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.console.println("\nLucky you! You have a bucket of water to repel the flames.\n");
            // need to remove the item once used
        }
    }

    @Override
    public String getInput() {
                
        String input = null;  //value to be returned
        boolean valid = false; //initialize to not valid
        
        while (!valid) {
            try {
                input = this.keyboard.readLine();
                input = input.trim();
                input = input.toUpperCase();
            
            } catch (IOException ex) {
                Logger.getLogger(FlameSpurtView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                Double.parseDouble(input);
            } catch (NumberFormatException nf) {
                ErrorView.display(this.getClass().getName(), "\nPlease enter a valid number.");
            }
            
            valid = true;
        }
        
        return input;
           
    }
    
    @Override
    public boolean doAction(String value) {
        double userAnswer = Double.parseDouble(value);
        DangerControl newDangerControl = new DangerControl();
       try {
           double correctAnswer = newDangerControl.calcFlameSpurtAnswer(pyramidLength, pyramidWidth, pyramidHeight); 
           return (Math.abs(userAnswer - correctAnswer) < 1.0);
       } catch (DangerControlException de) {
          ErrorView.display(this.getClass().getName(),
                            de.getMessage());
          return false;
       }    
    }
    
    public double getPyramidLength() {
        return pyramidLength;
    }

    public void setPyramidLength(double pyramidLength) {
        this.pyramidLength = pyramidLength;
    }

    public double getPyramidWidth() {
        return pyramidWidth;
    }

    public void setPyramidWidth(double pyramidWidth) {
        this.pyramidWidth = pyramidWidth;
    }

    public double getPyramidHeight() {
        return pyramidHeight;
    }

    public void setPyramidHeight(double pyramidHeight) {
        this.pyramidHeight = pyramidHeight;
    }

    public double getVolumeGuessed() {
        return volumeGuessed;
    }

    public void setVolumeGuessed(double volumeGuessed) {
        this.volumeGuessed = volumeGuessed;
    }
    
}
