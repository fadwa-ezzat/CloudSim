
/*    */
/*    */ import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Random;

/**
 * 
 * @author Fadwa Ezzat This is the server side main class; generates the clouds,
 *         their service files and services ans stores it to
 *         "configurations\config.txt"
 */

/*    */ public class GenerateServicesConfig
/*    */ {
	/* 18 */ public static int CLOUD_NUM = 100;
	// the maximum number of clouds in the multicloud environment
	/* 19 */ public static int SERVICE_FILE_MAX = 50;
	// the maximum number of services found in the multicloud environment
	/* 20 */ public static int SERVICE_MAX = 50; // the maximum number of services a cloud can have

	// this file stores the clouds and their services
	/*    */ public static final String FILENAME = System.getProperty("user.dir") + "\\configurations\\config.txt";

	/*    */ public static final int INTERVAL = 900000000; // tested many numbers for thread sleep and chose this :-D

	/*    */ public static HashMap<String, CloudServer> clouds;

	/*    *//**
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
	/*    */ public static void Generate(int cloudNum, int serviceFileMax, int serviceMax, String fileName)
	/*    */ {
		/* 35 */ CLOUD_NUM = cloudNum;
		/* 36 */ SERVICE_FILE_MAX = serviceFileMax;
		/* 37 */ SERVICE_MAX = serviceMax;
		/*    */
		/* 41 */ BufferedWriter bw = null;
		/* 42 */ FileWriter fw = null;

		// stores the service file names
		/* 43 */ HashMap sfMap = new HashMap();
		/*    */ try
		/*    */ {

			/* 46 */ fw = new FileWriter(System.getProperty("user.dir") + "\\configurations\\config.txt");

			/* 47 */ bw = new BufferedWriter(fw);
			/* 48 */ StringBuilder builder = new StringBuilder();
			/*    */
			/* 50 */ for (int c = 0; c < CLOUD_NUM; c++) {
				// writing a line in the file
				/* 51 */ String cstr = String.format("%02d", new Object[] { Integer.valueOf(c + 1) });
				// the cloud names and ports are written in order
				/* 52 */ builder.append("CloudServer" + cstr + ":" + (7800 + c + 1) + ":");
				// creating clouds and writing their names and ports to file
				/* 53 */ Random rand = new Random();
				/* 54 */ int sf_num = rand.nextInt(SERVICE_FILE_MAX) + 1;
				// a random number of services for a service file
				/*    */
				// to ensure that any generated service file name is not repeated for the same
				// cloud
				/* 56 */ HashMap sfUsed = new HashMap();
				/*    */
				/* 58 */ int sf = 0;
				/* 59 */ while (sf < Math.ceil(sf_num / 2.0D)) {
					/* 60 */ Random rand2 = new Random();
					/* 61 */ int sfi = rand2.nextInt(SERVICE_FILE_MAX) + 1;
					/* 62 */ if (!sfUsed.containsKey(Integer.valueOf(sfi)))// the service file name is not found from
																			// before in the current cloud
					/*    */ {
						/* 64 */ String sfo = (String) sfMap.get(Integer.valueOf(sfi));
						// a random service file name
						/*    */
						/* 66 */ if (sfo == null) {
							/* 67 */ Random rand1 = new Random();
							/* 68 */ int s_num = rand1.nextInt(SERVICE_MAX) + 1;
							/* 69 */ String sfstr = String.format("%02d", new Object[] { Integer.valueOf(sfi) });
							/* 70 */ String sstr = String.format("%02d", new Object[] { Integer.valueOf(s_num) });
							/* 71 */ sfo = new String(sstr + "-f" + sfstr);
							/* 72 */ sfMap.put(Integer.valueOf(sfi), sfo);
							// add this service file
							/*    */ }
						/*    */
						/* 75 */ if (sf > 0)
							/* 76 */ builder.append(",");
						/* 77 */ builder.append(sfo);
						/* 78 */ sfUsed.put(Integer.valueOf(sfi), sfo);
						/* 79 */ sf++;
						/*    */ }
					/*    */ }
				/* 81 */ builder.append("\r\n");
				/*    */ }
			// finished writing a cloud and its service files and their services
			/*    */
			/* 84 */ bw.write(builder.toString());
			// write all clouds to config.txt
			/*    */ }
		/*    */ catch (IOException e) {
			/* 87 */ e.printStackTrace();
			/*    */ try
			/*    */ {
				/* 90 */ bw.close();
				/* 91 */ fw.close();
				/*    */ } catch (IOException ee) {
				/* 93 */ ee.printStackTrace();
				/*    */ }
			/*    */ }
		/*    */ finally
		/*    */ {
			/*    */ try
			/*    */ {
				/* 90 */ bw.close();
				/* 91 */ fw.close();
				/*    */ } catch (IOException e) {
				/* 93 */ e.printStackTrace();
				/*    */ }
			/*    */ }
		/*    */ }

	/*    */
	public static void main(String[] args) {

		/* 26 */ clouds = new HashMap();
		/* 27 */ BufferedReader br = null;
		/*    */ try
		/*    */ {
			/*    */ while (true)
			/*    */ {
				// generate maximum clouds, maximum service files, maximum services
				Generate(30, 35, 35, System.getProperty("user.dir") + "\\configurations\\config.txt");
				/*    */
				/* 56 */ for (String key : clouds.keySet()) {
					/* 57 */ clouds.get(key).terminate();
					/* 58 */ clouds.get(key).join();
					/*    */ }
				/* 60 */ clouds.clear();
				/*    */
				/* 62 */ br = new BufferedReader(
						new FileReader(System.getProperty("user.dir") + "\\configurations\\config.txt"));
				/*    */ String line;
				/* 66 */ while ((line = br.readLine()) != null)
				/*    */ {
					/* 67 */ String[] lineSplits = line.split(":");
					/* 68 */ if (lineSplits.length == 3) {
						/* 69 */ CloudServer cloudServer = new CloudServer(lineSplits[0],
								Integer.valueOf(Integer.parseInt(lineSplits[1])), lineSplits[2].split(","));
						/* 71 */ cloudServer.start();
						/* 72 */ clouds.put(lineSplits[0], cloudServer);
						/*    */ }
					/*    */ }
				/*    */
				/* 76 */ Thread.sleep(INTERVAL);
				/*    */ }
			/*    */ } catch (FileNotFoundException e) {
			/* 79 */ e.printStackTrace();
			/*    */ } catch (IOException e) {
			/* 81 */ e.printStackTrace();
			/*    */ } catch (InterruptedException e) {
			/* 83 */ e.printStackTrace();
			/*    */ } finally {
			/* 85 */ if (br != null)
				/*    */ try {
					/* 87 */ br.close();
					/*    */ } catch (IOException e) {
					/* 89 */ e.printStackTrace();
					/*    */ }
			/*    */ }
	}

	/*    */ }
