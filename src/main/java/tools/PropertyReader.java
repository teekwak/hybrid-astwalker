package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

class PropertyReader {

	/**
	 * Reads the config file for configuring the similarity function properties
	 * @param bitVector x
	 * @return x
	 */
	static Map<String, Boolean> createSimilarityFunctionPropertiesMap(String bitVector) {
		Map<String, Boolean> map = new HashMap<>();

		if(bitVector.length() != 17) {
			throw new IllegalArgumentException("[ERROR]: bit vector is NOT 17 digits");
		}

		map.put("importsScore", bitVector.charAt(0) == '1');
		map.put("variableNameScore", bitVector.charAt(1) == '1');
		map.put("classNameScore", bitVector.charAt(2) == '1');
		map.put("authorScore", bitVector.charAt(3) == '1');
		map.put("projectScore", bitVector.charAt(4) == '1');
		map.put("methodCallScore", bitVector.charAt(5) == '1');
		map.put("methodDecScore", bitVector.charAt(6) == '1');
		map.put("sizeScore", bitVector.charAt(7) == '1');
		map.put("importNumScore", bitVector.charAt(8) == '1');
		map.put("complexityScore", bitVector.charAt(9) == '1');
		map.put("extendsScore", bitVector.charAt(10) == '1');
		map.put("packageScore", bitVector.charAt(11) == '1');
		map.put("fieldsScore", bitVector.charAt(12) == '1');
		map.put("isGenericScore", bitVector.charAt(13) == '1');
		map.put("isAbstractScore", bitVector.charAt(14) == '1');
		map.put("isWildCardScore", bitVector.charAt(15) == '1');
		map.put("ownerScore", bitVector.charAt(16) == '1');

		return map;
	}

	/**
	 * Reads the config file for configuring the Index Manager
	 * @param fileLocation location of file
	 * @return x
	 */
	static Map<String, String> createConfigPropertiesMap(String fileLocation) {
		Map<String, String> map = new HashMap<>();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation), "UTF-8"))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(!line.startsWith("#") && line.contains("=")) {
					map.put(line.split("=")[0], line.split("=")[1]);
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("[ERROR]: config file not found!");
		}

		validateConfigProperties(map);

		return map;
	}

	/**
	 * Reads the config file for configuring the AST Walker
	 * @param fileLocation location of file
	 * @return x
	 */
	static Map<String, Boolean> createASTPropertiesMap(String fileLocation) {
		Map<String, Boolean> map = new HashMap<>();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation), "UTF-8"))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(!line.startsWith("#") && line.contains("=")) {
					map.put(line.split("=")[0], Boolean.parseBoolean(line.split("=")[1]));
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("[ERROR]: config file not found!");
		}

		validateASTProperties(map);

		return map;
	}

	/**
	 * Validates the configProperties map
	 */
	private static void validateConfigProperties(Map<String, String> configProperties) {
		// check if map is null
		if(configProperties == null) {
			throw new IllegalArgumentException("[ERROR]: configProperties map is null!");
		}

		// check map size
		int correctSizeOfMap = 13;
		if(configProperties.size() != correctSizeOfMap) {
			throw new IllegalArgumentException("[ERROR]: configProperties map size is incorrect!");
		}
	}

	/**
	 * Validates the astProperties map
	 */
	private static void validateASTProperties(Map<String, Boolean> astProperties) {
		// check if map is null
		if(astProperties == null) {
			throw new IllegalArgumentException("[ERROR]: astProperties map is null!");
		}

		// check map size
		int correctSizeOfMap = 22;
		if(astProperties.size() != correctSizeOfMap) {
			throw new IllegalArgumentException("[ERROR]: astProperties map size is incorrect!");
		}

		// check values of map are set
		for(Map.Entry<String, Boolean> entry : astProperties.entrySet()) {
			if(entry.getValue() == null) {
				throw new IllegalArgumentException("[ERROR]: " + entry.getKey() + " in astProperties map is null!");
			}
		}
	}
}
