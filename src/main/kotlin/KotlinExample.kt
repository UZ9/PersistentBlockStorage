import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class KotlinExample : JavaPlugin() {

    private lateinit var blockStorage : PersistentBlockStorage

    override fun onEnable() {
        //Initializes the PersistentBlockStorage, providing the plugin instance as well as the data file name
        blockStorage = PersistentBlockStorage(this, "config.yml")

        //Stores the value 3 in key "exampleKey" at Location("world", 0.0, 0.0, 0.0)
        blockStorage.storeValue(Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "exampleKey", 3)

        //Retrieves an integer from key "exampleKey" at Location("world", 0.0, 0.0, 0.0)
        val v : Int = blockStorage.getValue(Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "exampleKey") as Int
    }

    override fun onDisable() {
        //Call the blockStorage.save() method to save all data, currently don't have a better solution other than manually calling this
        blockStorage.save()
    }

}