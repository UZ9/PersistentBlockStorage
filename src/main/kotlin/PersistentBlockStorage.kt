import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import java.io.*
import java.util.*


/**
 * Kotlin utility class used for persistent storing of [Serializable] objects inside blocks
 */
class PersistentBlockStorage(private var config: FileConfiguration, private var file: File) {


    //Stores all of the data within a mutable map
    private val data = mutableMapOf<Location, MutableMap<String, Serializable>>()

    /**
     * Stores a value into a location using a key-value based system
     *
     * @param location The block the location is at
     * @param key The key for the item to be stored under
     * @param value The object that is going to be stored/serialized
     */
    fun storeValue(location: Location, key: String, value: Serializable) {
        data.compute(location) { _, v -> v ?: mutableMapOf() }
        data.getValue(location)[key] = value
    }

    /**
     * Retrieves a value from location using a key-value based system
     *
     * @param location The block the location is at
     * @param key The key the data is being stored under
     * @return The value stored in that position
     */
    fun getValue(location: Location, key: String): Any {
        return data.getValue(location).getValue(key)
    }


    /**
     * Loads all of the block data
     */
    fun load() {
        config.getConfigurationSection("data").getKeys(false).forEach { loc ->
            val deserializedLoc = deserializeLocation(loc)
            val mapToAdd = mutableMapOf<String, Serializable>()

            config.getConfigurationSection("data.$loc").getKeys(false).forEach { flag ->
                mapToAdd["data.$loc.$flag"] = deserializeObject(config.getString("data.$loc.$flag")) as Serializable
            }

            data[deserializedLoc] = mapToAdd
        }

    }

    fun save() {
        data.forEach { block ->
            val serializedLoc = serializeLocation(block.key)

            block.value.forEach { flag ->
                config.set("data.$serializedLoc.${flag.key}", serializeObject(flag.value))
            }


        }

        config.save(file)
    }

    private fun serializeObject(o: Serializable): String {
        val baos = ByteArrayOutputStream();
        val oos = ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }

    private fun deserializeObject(s: String): Any {
        val data = Base64.getDecoder().decode(s);
        val ois = ObjectInputStream(ByteArrayInputStream(data))
        val o = ois.readObject();
        ois.close()
        return o
    }

    private fun serializeLocation(l: Location): String {
        return "${l.world.name}${l.x}:${l.y}:${l.z}"
    }

    private fun deserializeLocation(s: String): Location {
        val arr = s.split(":")
        return Location(Bukkit.getWorld(arr[0]), arr[1].toDouble(), arr[2].toDouble(), arr[3].toDouble())
    }

}