# PersistentBlockStorage
A simple tool for storing persistent data in blocks

This was a simple class I wrote to use in various projects that can store any object implementing Serializable and persist it through restarts. Currently the only file format is flatfile yml, however I most likely will add other formats such as MongoDB. The class was written in Kotlin mostly because of me gaining an interest in learning more about the language and its more conveinient and less verbose style.

# Usage
Using the PersistentBlockStorage class is rather simple: all you have to do is instantiate the class and use the given methods. Below are examples for both Kotlin and Java.
## Kotlin
```kotlin
class TestCase : JavaPlugin() {

    private lateinit var blockStorage : PersistentBlockStorage

    override fun onEnable() {
        //Initializes the PersistentBlockStorage, providing the plugin instance as well as the data file name
        blockStorage = PersistentBlockStorage(this, "blockData.yml")

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
```
## Java
```java
public class Test extends JavaPlugin {

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
```

