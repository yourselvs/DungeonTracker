package yourselvs.dungeontracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.yaml.snakeyaml.Yaml;

import com.mythicacraft.voteroulette.utils.ConfigAccessor;

public class ConfigHandler {
	private DungeonTracker plugin;
	private File dataFolder;
	private File defaults;
	
	private String defaultsPath = "default.yml";
	private String permissionsPath = "permissions.txt";
	private FileWriter fileStream;
	
	private Yaml yaml = new Yaml();
	
	public ConfigHandler(DungeonTracker plugin) {
		this.plugin = plugin;
		
		dataFolder = plugin.getDataFolder();
		if(!dataFolder.exists())
			dataFolder.mkdir();
		
		defaults = new File(plugin.getDataFolder(), defaultsPath);
		if(!defaults.exists())
			try {defaults.createNewFile(); writeDefaultsFile();} catch (IOException e) {e.printStackTrace();}
	}
	
	public void writeDefaultsFile() {
		try {
			fileStream = new FileWriter(defaults);
			plugin.getLogger().info("No defaults file detected. Writing new one.");
			
			ConfigAccessor defaultFile = new ConfigAccessor(defaultsPath, plugin);
			yaml = new Yaml();
			yaml.dump(defaultFile, fileStream);
			
			fileStream.close();
			
			plugin.getLogger().info("New defaults file created.");
		} catch (IOException e) {plugin.getLogger().info(e.getMessage());}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPermissions() {
		ConfigAccessor defaultFile = new ConfigAccessor(permissionsPath, plugin);
			
		List<String> list = (List<String>) defaultFile.getConfig().getList("permissions");
		
		return list;
	}
}
