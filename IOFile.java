import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Scanner;

public class IOFile {
	//imports text file & does splitting & adds to LinkedList
	PlayerLinkedList linkedList;
	static RedirectErrors errorCatch;

	/*Reads through the files names based off of the list of names and adds a node made from each line to the masterArray*/
	public static MasterArray fileReadingArray(String[] fileNames, MasterArray masterArray) throws IOException{
		Scanner scanner = new Scanner(System.in);
		BufferedReader br = null;

		String line;
		String name = null;
		String compound = null;
		boolean used = false;
		String type = null;

		

			
			try{
				for(int i = 0; i < 2; i++){
					int count = 0;
					File file = new File(fileNames[i]);
					
					FileReader fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
	
					//make array of strings when split line by ';'
					//goes through this array and adds characters to node with index of int types
					//if 1, = true; if 0, = false
					while((line = bufferedReader.readLine()) != null){
	
						//Array of strings split by ";" 
						//uses HashValue at word to find index
						String[] eachEle = line.split(";");
	
						QuestionNode newNode = new QuestionNode(eachEle[0], eachEle[1], Integer.parseInt(eachEle[2]));
						masterArray.add(i, count, newNode);
	
						count ++;
	
	
					}
	
				} 
			}
			catch (IOException ioe){
				errorCatch = new RedirectErrors();
			}
			finally
			{
				try{
					if(br!=null)
						br.close();
				}catch(Exception ex){
					errorCatch = new RedirectErrors();
				}
			}


		
		return masterArray;
	}


	/*reads through the file and writes it to a playerLinkedList*/
	public static PlayerLinkedList fileReadingLinkedList(String fileName, PlayerLinkedList playerLinkedList) throws IOException{
		//splits doc by ; and adds username/level to linkedlist

		BufferedReader br = null;



		try{
			
			File file = new File(fileName);
			if(!file.exists()){
				/*this is me avoiding a null pointer exception*/
				writeNewFile(fileName);
			}
			FileReader fr = new FileReader(file);
			
			BufferedReader bufferedReader = new BufferedReader(fr);
			

			String line;
			String username = null;
			double level = 0;

			//make array of strings when split line by ';'

			while((line = bufferedReader.readLine()) != null){

				//Array of strings split by ";" 
				String[] eachEle = line.split(";");

				//goes through textFile and adds to LinkedList
				for(int i = 0; i < eachEle.length-1; i++){
					//goes through array eachEle and adds username and level to LinkedList
					username = eachEle[i];
					level = Double.parseDouble(eachEle[i+1]);

					playerLinkedList.addInfo(username, level);

				}
			}		
		}

		catch (IOException ioe){
			errorCatch = new RedirectErrors();
		}
		finally
		{
			try{
				if(br!=null)
					br.close();
			}catch(Exception ex){
				errorCatch = new RedirectErrors();
			}
		}
		return playerLinkedList;


	} 


	/*Writes new empty file under a certain name*/
	public static void writeNewFile(String fileName){
		BufferedWriter bw = null;
		
		try{
			//makes new textfilewriter and bufferedwriter
			FileWriter w = new FileWriter(fileName);
			bw = new BufferedWriter(w);

			bw.close();
		

		} catch (IOException ioe){
			errorCatch = new RedirectErrors();
		}
		finally
		{
			try{
				if(bw!=null)
					bw.close();
			}catch(Exception ex){
				errorCatch = new RedirectErrors();
			}
		}

	}
	
	/*writes the information of a playerLinkedList to a file*/
	public static void fileWriting(PlayerLinkedList l, String fileName) throws IOException {
		//writes LinkedList to textFile wordIndex when button "Exit" is pressed
		
		BufferedWriter bw = null;
		
		try{
			//makes new textfilewriter and bufferedwriter
			FileWriter w = new FileWriter(fileName);
			bw = new BufferedWriter(w);
		

			//writes LinkedList to textFile separating by ;
			PlayerNode currentNode = l.head;

			while(currentNode != null){
				bw.write(currentNode.getName());
				bw.write(";" + currentNode.getLevel() + ";");
				bw.newLine();

				currentNode = currentNode.getNext();
			}

			bw.close();
		

		} catch (IOException ioe){
			errorCatch = new RedirectErrors();
		}
		finally
		{
			try{
				if(bw!=null)
					bw.close();
			}catch(Exception ex){
				errorCatch = new RedirectErrors();
			}
		}

	}
	
	
}
