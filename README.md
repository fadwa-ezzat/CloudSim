# CloudSim
- Performance measures are the number of examined services (checked whether to add to the result or not)
and if they were useful (should be added to result), their cloud is added to the number of combined clouds

•	This project is responsible for the dynamic generation of MCE which contains clouds and their services.
•	The main class is GenerateServicesConfig.java
•	The simulation of the access time can also be changed (line 26).
•	At the beginning of the class, we have values reflecting the maximum number of clouds and services, which can be changed. In my environment, the services are file names (e.g. f01, f04, f12, etc.) and each file having a random number of services, e.g. if f01 has 5 services, it will be written as 05-f01. The service files and their services are created in the main class at lines 81-86. This means that a user’s request to this environment will be just the file name, i.e. if a user needs services 1,3,5, he will ask for f01 f03 f05.
•	To simulate a MCE, change the parameters of line 128 to reflect the number of clouds and services in your environment. The created clouds are saved to a file in the user directory folder config.txt”(line 52).
•	The simulated clouds are run (as they are Threads) and saved to a hashmap so as to be readily available when accesses/needed (line 143,144).
•	Run the main class; the console shows the number of clouds created and their ports, etc. (if you open the configuration file, you will also find them). If you monitor the console, you will find that the values will change after some time, simulating a dynamic environment (and so will the configuration file’s values).
