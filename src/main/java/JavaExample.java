import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaExample extends JavaPlugin {

    private PersistentBlockStorage blockStorage;

    @Override
    public void onEnable() {
        blockStorage = new PersistentBlockStorage(this, "blockData.yml");

        //Stores the value 3 in key "exampleKey" at Location("world", 0.0, 0.0, 0.0)
        blockStorage.storeValue(new Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "exampleKey", 3);

        //Retrieves an integer from key "exampleKey" at Location("world", 0.0, 0.0, 0.0)
        int v = (int) blockStorage.getValue(new Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "exampleKey");
    }

    @Override
    public void onDisable() {
        //Call the blockStorage.save() method to save all data, currently don't have a better solution other than manually calling this
        blockStorage.save();
    }
}
