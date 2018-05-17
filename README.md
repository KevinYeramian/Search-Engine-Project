# Search-Engine-Project
Final project : DD2476 Search Engines and Information Retrieval systems at KTH

## Step 1: Software Dependencies

•	Eclipse: http://www.eclipse.org/downloads/ 

•	Java Development kit: http://www.oracle.com/technetwork/java/javase/downloads/index.html 

•	Elastic Search: https://www.elastic.co/downloads 
Note: Make sure to set environment variable of JAVAHOME and add java bin path in the path variable

## Step 2: Download the following repositories and models

1.	Clone/download the code repository from: https://github.com/KevinYeramian/Search-Engine-Project

2.	Download the Dataset from: https://bitbucket.org/ameliaa/sokteknologi-meetup/src/30fa447426c5/data/data/?at=master 

3.	Download Word2vector model from https://drive.google.com/drive/folders/1gKLKuVYjk1PAofLeZO7CqH-VIOrMUxyI  and unzip the file. It seems to be a txt file but you need to unzip it. This is the large model trained on Swedish wiki.

## Step 3: Importing project

1.	Open Eclipse and Create a project. Make a package in src with the name of Engine. Import the code from the code repository you have downloaded before from github.

1.	Right Click on the Engine package of the newly created Eclipse project and click on import.

2.	In the new window, select General -> File System and click on next.

3.	In the new window, select the code folder named Engine that you have downloaded from Github and check on Engine so that all the files in the package got checked and click on finish. You’ll see all the java files in your project now.

## Step 4: Installing Maven

1.	Most Eclipse downloads include the Maven tooling already. If it is missing in your installation, you can install it via the main update of your release via Help ▸ Install New Software. The following listing contains the update site for the Neon release and an update site maintained by the m2e project.

    a.	Neon Update Site http://download.eclipse.org/releases/neon

    b.	M2e Update Site http://download.eclipse.org/technology/m2e/releases 

2.	By default, the Maven tooling does not download the Maven index for the Eclipse IDE. Via the Maven index you can search for dependencies, select them and add them to your pom file. To download the index, select Windows ▸ Preferences ▸ Maven and enable the Download repository index updates on startup option.

## Step 5: Set up the Project:

1.	Now we have to convert the project to Maven project, click on the project name ▸Configure ▸Convert to Maven Project. Make sure you don’t have any spaces in the Group ID and Artifact ID. Remember these ID’s or name them same as your project name. By clicking finish you’ll see two new folders bin, target and an xml file named pom.xml

2.	Copy and replace the pom.xml file with the pom.xml in the Github code repository. 

3.	Open pom.xml file and write the Group ID and Artifact ID of your project and save the file.

4.	Right click on the project name ▸Run As ▸Maven build (2nd Maven build option. Please note that there will be two Maven build options, select the 2nd one which is right above Maven clean). 

5.	The new window will open after selecting Maven build. Write “clean verify” in the Goals textbox and run the build. Wait 1-2 mins for the project to build. After build being finished you’ll see “BUILD SUCCESS” in the console. 

6.	Now right click on your Project ▸Build path ▸Add external archives. Go to your project directory in the explorer usually in the path *C:\Users\%username%\eclipse-workspace\%projectname%\target* and select *%Projectname%-1.0-SNAPSHOT-jar-with-dependencies* file. My project name is SearchEngine so the file is “SearchEngine-1.0-SNAPSHOT-jar-with-dependencies” and open. Refresh the project.

7.	Open w2vImpoter.java and change the variable path on line 40. Put the path of the word2vector unzipped folder you downloaded earlier. My files are in the project folder so the path is  String path = "C:\\Users\\username\\eclipse-workspace\\Search Engine";

## Step 6: Set up the environment:

1.	Copy *elasticsearch.yml* from the code directory (downloaded from git) and replace the same file in *%pathtoelasticsearch%\elasticsearch-6.2.4\config\elasticsearch.yml*.

2.	Open command prompt/power shell in Elastic search folder and run elastic search by typing *.\bin\elasticsearch.bat*.

3.	Open DataParser.java file and open it’s Run Configurations by clicking on down arrow along with green play button. Click on the arguments tab, write (in the Program Arguments) data folder path (downloaded from bitbucket) give space and write 100000/10000/1000 (as we have 3 data files so this argument is the file number). For example, *C:\Users\%username%\Downloads\IR_Project\data 100000*. Click on run button. This will create a Json file in the project directory.

## Step 7: Run the project:

1.	Open Engine.java file and open it’s Run Configurations by clicking on down arrow along with green play button. Click on the arguments tab, write (in the VM Arguments) “-Xms512M -Xms8524M” in the VM arguments and click on the run file. These arguments increase the java VM memory size so it can take up to 8gb of memory while loading the word2vector.

2.	After running Engine.java file you might have to wait for 5-8 mins depending on the disk speed of your system. The system loaded the word2vector model and then displays the GUI.

