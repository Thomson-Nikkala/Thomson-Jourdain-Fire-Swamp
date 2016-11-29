/** *************************************************
 * LightningSandView Class                         *
 *                                                 *
 ************************************************** */
package byui.cit260.fireSwamp.view;

import byui.cit260.fireSwamp.controller.DangerControl;
import byui.cit260.fireSwamp.controller.InventoryControl;
import byui.cit260.fireSwamp.exceptions.DangerControlException;
import byui.cit260.fireSwamp.exceptions.InventoryControlException;
import byui.cit260.fireSwamp.model.Item;
import fireswamp.FireSwamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Nikkala Thomson
 */
public class LightningSandView extends View {

    private double diameter;

    @Override
    public void display() {

        //Randomize diameter
        double diameter = Math.random() * 29 + 1;  //diameter will be [1..30)

        //Display problem
        System.out.println(" You see a pit of lightning sand in front of you."
                + "\n To avoid being swallowed up by the sand, you must calculate the"
                + "\n distance you will need to walk around one side of the pit.  "
                + "\n The distance across the pit is ");
        System.out.printf("%.2f", diameter);
        System.out.printf(".");

        //Collect input
        String input = getInput();

        //Validate input
        boolean isCorrect = doAction(input);

        //Display results
        if (isCorrect) {
            System.out.println("You made it across");
        } else {
            //Check for rope (itemType 1) 
            InventoryControl inControl = new InventoryControl();
            try {
                ArrayList<Item> inventory = FireSwamp.getPlayer().getPlayerInventory();
                inControl.checkInventory(inventory, 1);
            } catch (InventoryControlException ice) {
                System.out.println(ice.getMessage());
                System.out.println("Alas, that's incorrect, and you have no rope "
                             + " to help pull you out.");
                LoseMenuView loseView = new LoseMenuView();
                loseView.display();
            }
        }
    }

    @Override
    public String getInput() {
        Scanner in = new Scanner(System.in);  //get infile for keyboard
        String input = " ";  //value to be returned
        boolean validInput = false; //initialize to not valid

        while (!validInput) {
            input = in.nextLine();
            input = input.trim();
            input = input.toUpperCase();

            try {
                Double.parseDouble(input);
                validInput = true;

            } catch (NumberFormatException nfe) {
                System.out.println("\nPlease enter a valid number.");
            }

        }

        return input;

    }

    @Override
    public boolean doAction(String value) {
        double userAnswer = Double.parseDouble(value);
        DangerControl newDanger = new DangerControl();
        try {
            double correctAnswer = newDanger.calcLightningSandAnswer(diameter);
            return (Math.abs(userAnswer - correctAnswer) < 0.01);
        } catch (DangerControlException de) {
            System.out.println(de.getMessage());
            return false;
        }
    }

}
