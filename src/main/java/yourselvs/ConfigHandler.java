package yourselvs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.mythicacraft.voteroulette.utils.ConfigAccessor;

public class ConfigHandler {
	private DungeonTracker plugin;
	private File dataFolder;
	private File defaults;
	
	private String defaultsPath = "default.yml";
	private FileWriter fileStream;
	
	private Yaml yaml = new Yaml();
	
	public ConfigHandler(DungeonTracker plugin) {
		this.plugin = plugin;
		
		dataFolder = plugin.getDataFolder();
		if(!dataFolder.exists())
			dataFolder.mkdir();
		
		defaults = new File(plugin.getDataFolder(), defaultsPath);
		if(!defaults.exists())
			try {defaults.createNewFile(); writeDefaultsFile();} catch (IOException e) {plugin.getLogger().info(e.getMessage());}
	}
	
	public void writeDefaultsFile() {
		try {
			fileStream = new FileWriter(defaults);
			plugin.getLogger().info("No defaults file detected. Writing new one.");
			
			ConfigAccessor defaultFile = new ConfigAccessor("defaults.yml");
			yaml = new Yaml();
			yaml.dump(defaultFile, fileStream);
			
			fileStream.close();
			
			plugin.getLogger().info("New defaults file created.");
		} catch (IOException e) {plugin.getLogger().info(e.getMessage());}
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Boolean> getParams(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			InputStream reader = new FileInputStream(defaults);
			
			yaml = new Yaml();
			result = (Map<String, Object>) yaml.load(reader);
		} catch (FileNotFoundException e) {e.printStackTrace();}
		Map<String, Boolean> defaults = new HashMap<String, Boolean>();
		for(String param : result.keySet())
			defaults.put(param, (Boolean) result.get(param));
		return defaults;
	}
}
