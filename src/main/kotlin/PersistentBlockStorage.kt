import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.*
import java.util.*


class PersistentBlockStorage : JavaPlugin() {



    companion object {
        private val data = mutableMapOf<Location, MutableMap<String, java.io.Serializable>>()

        fun storeValue(location : Location, key : String, value : java.io.Serializable) {
            data.compute(location) {_, v -> v ?: mutableMapOf()}
            data.getValue(location)[key] = value
        }

        fun getValue(location : Location, key : String): Any {
            return data.getValue(location).getValue(key)
        }
    }

    override fun onEnable() {

        config.getConfigurationSection("data").getKeys(false).forEach {loc ->
            val deserializedLoc = deserializeLocation(loc)
            val mapToAdd = mutableMapOf<String, Serializable>()

            config.getConfigurationSection("data.$loc").getKeys(false).forEach {flag ->
                mapToAdd["data.$loc.$flag"] = deserializeObject(config.getString("data.$loc.$flag")) as Serializable
            }

            data[deserializedLoc] = mapToAdd
        }


        PersistentBlockStorage.storeValue(Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "someKey", BlockData())

        var e : Int = PersistentBlockStorage.getValue(Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "someKey") as Int
    }

    override fun onDisable() {
        data.forEach { block ->
            val serializedLoc = serializeLocation(block.key)
            
            block.value.forEach {flag ->
                config.set("data.$serializedLoc.${flag.key}", serializeObject(flag.value))
            }

            saveConfig()
        }
    }

    private fun serializeObject(o : Serializable) : String {
        val baos = ByteArrayOutputStream();
        val oos = ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }

    private fun deserializeObject(s : String) : Any {
        val data = Base64.getDecoder().decode(s);
        val ois = ObjectInputStream(ByteArrayInputStream(data))
        val o = ois.readObject();
        ois.close()
        return o
    }

    private fun serializeLocation(l : Location) : String {

        return "${l.world.name}${l.x}:${l.y}:${l.z}"

    }

    private fun deserializeLocation(s : String) : Location {
        val arr = s.split(":")
        return Location(Bukkit.getWorld(arr[0]), arr[1].toDouble(), arr[2].toDouble(), arr[3].toDouble())
    }


}