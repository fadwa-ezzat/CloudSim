 import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.HashMap;
 import java.util.Random;

/**
 * 
 * @author Fadwa Ezzat This is the server side main class; generates the clouds,
 *         their service files and services and stores it to
 *         "configurations\config.txt"
 */

 public class GenerateServicesConfig
 {
	
	// the maximum number of clouds in the multicloud environment
	 public static int CLOUD_NUM = 100;
	// the maximum number of services found in the multicloud environment
	 public static int SERVICE_FILE_MAX = 50;
	 public static int SERVICE_MAX = 50; // the maximum number of services a cloud can have

	// this file stores the clouds and their services
	 public static final String FILENAME = System.getProperty("user.dir") + "\\configurations\\config.txt";

	 public static final int INTERVAL = 900000000; // tested many numbers for thread sleep and chose this :-D

	 public static HashMap<String, CloudServer> clouds;

	/**
			 * This function generates a set of cloud, each having a set of service files
			 * containing a number of services It ensures that if a service file "f01" in a
			 * cloud has 25 services, it will have the same number of services if it was to
			 * be found in another cloud so, if cloud01 has f01 which has 25 services and
			 * cloud03 also has f01, it will have the same 25 services
			 * 
			 * @param cloudNum
			 * @param serviceFileMax
			 * @param serviceMax
			 * @param fileName
			 */
	 public static void Generate(int cloudNum, int serviceFileMax, int serviceMax, String fileName)
	 {
		 CLOUD_NUM = cloudNum;
		 SERVICE_FILE_MAX = serviceFileMax;
		 SERVICE_MAX = serviceMax;
		
		 BufferedWriter bw = null;
		 FileWriter fw = null;

		// stores the service file names
		 HashMap sfMap = new HashMap();
		 try
		 {

			 fw = new FileWriter(System.getProperty("user.dir") + "\\configurations\\config.txt");

			 bw = new BufferedWriter(fw);
			 StringBuilder builder = new StringBuilder();
			
			 for (int c = 0; c < CLOUD_NUM; c++) {
				// writing a line in the file
				 String cstr = String.format("%02d", new Object[] { Integer.valueOf(c + 1) });
				// the cloud names and ports are written in order
				 builder.append("CloudServer" + cstr + ":" + (7800 + c + 1) + ":");
				// creating clouds and writing their names and ports to file
				 Random rand = new Random();
				 int sf_num = rand.nextInt(SERVICE_FILE_MAX) + 1;
				// a random number of services for a service file
				
				// to ensure that any generated service file name is not repeated for the same
				// cloud
				 HashMap sfUsed = new HashMap();
				
				 int sf = 0;
				 while (sf < Math.ceil(sf_num / 2.0D)) {
					 Random rand2 = new Random();
					 int sfi = rand2.nextInt(SERVICE_FILE_MAX) + 1;
					 if (!sfUsed.containsKey(Integer.valueOf(sfi)))// the service file name is not found from
																			// before in the current cloud
					 {
						 String sfo = (String) sfMap.get(Integer.valueOf(sfi));
						// a random service file name
						
						 if (sfo == null) {
							 Random rand1 = new Random();
							 int s_num = rand1.nextInt(SERVICE_MAX) + 1;
							 String sfstr = String.format("%02d", new Object[] { Integer.valueOf(sfi) });
							 String sstr = String.format("%02d", new Object[] { Integer.valueOf(s_num) });
							 sfo = new String(sstr + "-f" + sfstr);
							 sfMap.put(Integer.valueOf(sfi), sfo);
							// add this service file
							 }
						
						 if (sf > 0)
							 builder.append(",");
						 builder.append(sfo);
						 sfUsed.put(Integer.valueOf(sfi), sfo);
						 sf++;
						 }
					 }
				 builder.append("\r\n");
				 }
			// finished writing a cloud and its service files and their services
			
			 bw.write(builder.toString());
			// write all clouds to config.txt
			 }
		 catch (IOException e) {
			 e.printStackTrace();
			 try
			 {
				 bw.close();
				 fw.close();
				 } catch (IOException ee) {
				 ee.printStackTrace();
				 }
			 }
		 finally
		 {
			 try
			 {
				 bw.close();
				 fw.close();
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			 }
		 }

	
	public static void main(String[] args) {

		 clouds = new HashMap();
		 BufferedReader br = null;
		 try
		 {
			 while (true)
			 {
				// generate maximum clouds, maximum service files, maximum services
				Generate(30, 35, 35, System.getProperty("user.dir") + "\\configurations\\config.txt");
				
				 for (String key : clouds.keySet()) {
					 clouds.get(key).terminate();
					 clouds.get(key).join();
					 }
				 clouds.clear();
				
				 br = new BufferedReader(
						new FileReader(System.getProperty("user.dir") + "\\configurations\\config.txt"));
				 String line;
				 while ((line = br.readLine()) != null)
				 {
					 String[] lineSplits = line.split(":");
					 if (lineSplits.length == 3) {
						 CloudServer cloudServer = new CloudServer(lineSplits[0],
								Integer.valueOf(Integer.parseInt(lineSplits[1])), lineSplits[2].split(","));
						 cloudServer.start();
						 clouds.put(lineSplits[0], cloudServer);
						 }
					 }
				
				 Thread.sleep(INTERVAL);
				 }
			 } catch (FileNotFoundException e) {
			 e.printStackTrace();
			 } catch (IOException e) {
			 e.printStackTrace();
			 } catch (InterruptedException e) {
			 e.printStackTrace();
			 } finally {
			 if (br != null)
				 try {
					 br.close();
					 } catch (IOException e) {
					 e.printStackTrace();
					 }
			 }
	}

	 }
