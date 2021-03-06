package byui.cit260.fireSwamp.view;

import byui.cit260.fireSwamp.model.Item;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Nikkala
 */

public class WriteInventoryView extends View {
    
    public static void writeInventory(ArrayList<Item> itemList, String filepath) throws IOException {
      
       
        try (PrintWriter output = new PrintWriter(filepath))
            //write the inventory to the file
            {   
                //write header
                String header = "Your inventory contains:";
                output.println(header);
                //if itemList is not empty, write each item on a new line
                if (itemList.isEmpty()) {
                    //do nothing
                } else {   //write items
                    for (Item item: itemList) {                        
                        output.println(item.getItemName());
                    }                   
                }   
                output.flush();
            }
        
        catch(Exception e) {
            
            throw new IOException(e.getMessage());
            
        }
        
    }
   
    @Override
    public boolean doAction(String value) {
        return false;
    }
}
