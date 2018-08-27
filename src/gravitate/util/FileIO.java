package gravitate.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FileIO {
	private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	
	/**
	 * Loads a level from a file
	 * @param fileName - The name of the .level or .zip file without the extension
	 * @return A JSONData object containing all level data
	 */
	public static JSONData extractLevel(String fileName) throws ZipException, IOException {
			JSONData levelData = new JSONData();
			InputStream in;
			Scanner s;
			//.level files are just renamed .zip files, so this works.
			ZipFile levelZip = null;
			try {
				InputStream input = classLoader.getResourceAsStream("levels/" + fileName + ".level");
				File file = File.createTempFile("foo", "bar");
			    file.deleteOnExit();
				OutputStream out = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = input.read(buffer);
				while (len != -1) {
				    out.write(buffer, 0, len);
				    len = input.read(buffer);
				}
				out.close();
				levelZip = new ZipFile(file);
				System.out.println("[FileLoader] Found file \"" + fileName + ".level\"");
			} catch(Exception e) {
				//Accept ZIP files, but prefer LEVEL files
				InputStream input = classLoader.getResourceAsStream("levels/" + fileName + ".zip");
				File file = File.createTempFile("foo", "bar");
			    file.deleteOnExit();
				OutputStream out = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = input.read(buffer);
				while (len != -1) {
				    out.write(buffer, 0, len);
				    len = input.read(buffer);
				}
				out.close();
				levelZip = new ZipFile(file);
				System.out.println("[FileLoader] Found file \"" + fileName + ".zip\"");
			}
			
			in = levelZip.getInputStream(levelZip.getEntry(fileName + "/level.json"));
			s = new Scanner(in);
			//"\A" means "beginning of string," so the whole thing is a single token.
			s.useDelimiter("\\A");
			levelData.setLevelData(s.hasNext() ? s.next() : "");
			s.close();
			System.out.println("[FileLoader] Loaded \"level.json\"");
			
			in = levelZip.getInputStream(levelZip.getEntry(fileName + "/terrain.json"));
			s = new Scanner(in);
			s.useDelimiter("\\A");
			levelData.setTerrainData(s.hasNext() ? s.next() : "");
			s.close();
			System.out.println("[FileLoader] Loaded \"terrain.json\"");
			
			in = levelZip.getInputStream(levelZip.getEntry(fileName + "/entities.json"));
			s = new Scanner(in);
			s.useDelimiter("\\A");
			levelData.setEntityData(s.hasNext() ? s.next() : "");
			s.close();
			System.out.println("[FileLoader] Loaded \"entities.json\"");
			
			levelZip.close();
			in.close();
			return levelData;
	}

	/**
	 * Loads a level from a file. Searches the custom levels folder.
	 * @param fileName - The name of the .level or .zip file without the extension
	 * @return A JSONData object containing all level data
	 * @throws ZipException
	 * @throws IOException
	 */
	public static JSONData extractCustomLevel(String fileName) throws ZipException, IOException {
			JSONData levelData = new JSONData();
			InputStream in;
			Scanner s;
			//.level files are just renamed .zip files, so this works.
			ZipFile levelZip = null;
			File customLevels = new File("./customLevels");
			if(!customLevels.exists()) customLevels = new File("../customLevels");
			try {
				levelZip = new ZipFile(new File(customLevels + "/" + fileName + ".level"));
				System.out.println("[FileLoader] Found file \"" + fileName + ".level\"");
			} catch(Exception e) {
				//Accept ZIP files, but prefer LEVEL files
				levelZip = new ZipFile(new File(customLevels + "/" + fileName + ".zip"));
				System.out.println("[FileLoader] Found file \"" + fileName + ".zip\"");
			}
			
			in = levelZip.getInputStream(levelZip.getEntry(fileName + "/level.json"));
			s = new Scanner(in);
			//"\A" means "beginning of string," so the whole thing is a single token.
			s.useDelimiter("\\A");
			levelData.setLevelData(s.hasNext() ? s.next() : "");
			s.close();
			System.out.println("[FileLoader] Loaded \"level.json\"");
			
			in = levelZip.getInputStream(levelZip.getEntry(fileName + "/terrain.json"));
			s = new Scanner(in);
			s.useDelimiter("\\A");
			levelData.setTerrainData(s.hasNext() ? s.next() : "");
			s.close();
			System.out.println("[FileLoader] Loaded \"terrain.json\"");
			
			in = levelZip.getInputStream(levelZip.getEntry(fileName + "/entities.json"));
			s = new Scanner(in);
			s.useDelimiter("\\A");
			levelData.setEntityData(s.hasNext() ? s.next() : "");
			s.close();
			System.out.println("[FileLoader] Loaded \"entities.json\"");
			
			levelZip.close();
			in.close();
			return levelData;
	}
}
