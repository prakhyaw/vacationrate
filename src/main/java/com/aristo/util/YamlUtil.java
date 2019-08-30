package com.aristo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.nytimes.testng.Logger;

public class YamlUtil {
	private static Map<String, String> testData = new HashMap<String, String>();
	
	/*This method is used to create a yaml with the response details*/
	public void writeToYamlFile(Map<String, String> resInfo,String fileName) {
			String filePath = System.getProperty("user.dir")+"//testdata//"+fileName+".yaml";
			Logger.info("fileName=="+filePath);
			DumperOptions options = new DumperOptions();
	        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml yaml = new Yaml(options);
			File f = new File(filePath);
			Logger.info("File =="+f.toString());
			if(f.exists() && !f.isDirectory()) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
				String fileBackUpPath = System.getProperty("user.dir")+"//testdata//"+fileName+ sdf.format(f.lastModified())+".yaml";
				File f2 = new File(fileBackUpPath);
				Logger.info("Renamed File =="+f2.toString());
				f.renameTo(f2);
			}
			FileWriter writer;
			try {
				writer = new FileWriter(filePath);
				yaml.dump(resInfo, writer);
				writer.close();
			} catch (IOException e) {
				Logger.info("Error writing to Yaml file with response details");
			}
			
	}
	
	/*This method is used to read a yaml*/
	@SuppressWarnings("unchecked")
	public Map< String, String> readFromYamlFile(String fileName) {
		String filePath = System.getProperty("user.dir")+"//testdata//"+fileName+".yaml";
		
		InputStream ios = null;
		try {
			ios = new FileInputStream(new File(filePath));
		} catch (FileNotFoundException e) {
			Logger.info("Error Yaml file not found at the path ->"+filePath);
		}
		Yaml yaml = new Yaml();
		// Parse the YAML file and return the output as a series of Maps and Lists
        Map< String, String> response = (Map< String, String>) yaml.load(ios);
		return response;
	}

	
	/*This method is used to read a yaml based on the key passed as a parameter*/
	@SuppressWarnings("unchecked")
	public static Map<String, String> getTestData(String fileName, String forTestCase) {
		
		Yaml yaml = new Yaml();
		try {
			String filePath = System.getProperty("user.dir")+"//testdata//"+fileName+".yaml";
			InputStream input = new FileInputStream(new File(filePath));
			Map<String, Map<String, String>> YFile = (Map<String, Map<String, String>>) yaml.load(input);
			addToTestData(YFile.get(forTestCase));
			return testData;
		}catch(org.yaml.snakeyaml.error.YAMLException ye){
			Logger.info("Yaml error when trying to get test data keys");
			return testData;
		} catch (FileNotFoundException e) {
			Logger.info("Error Yaml file not found at the path");
			return testData;
		}
		
	}

	private static void addToTestData(Map<String, String> m) {
		Iterator<String> iterator = m.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			testData.put(key, m.get(key));			
		}
	}
}
