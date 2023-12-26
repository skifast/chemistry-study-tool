import java.util.Random;

public class MasterArray {

	int arraySize = 4;
	int array2Size = 132;
	QuestionNode[][] masterArray;

	public MasterArray(){
		
		//goes through table and sets all LinkedLists to null		

		masterArray = new QuestionNode[arraySize][array2Size];
		for(int i = 0; i < array2Size; i ++){
			for(int j = 0; j < arraySize; j++){
				masterArray[j][i] = new QuestionNode();
			}
		}
	}

	public void add(int index1, int index2, QuestionNode questionNode){
		masterArray[index1][index2] = questionNode;
	}
	
	public QuestionNode[] addToUnanswered(QuestionNode[] unanswered, String compoundTypes){
		if("ternaryCompounds".equals(compoundTypes)){
			for(int i = 0; i < 101; i++){
				unanswered[i] = masterArray[1][i];
			}
		}
		else if("polyatomicIons".equals(compoundTypes)){
			for(int i = 0; i < 31; i++){
				unanswered[i] = masterArray[0][i];
			}
		}
		else if("both".equals(compoundTypes)){
			for(int i = 0; i < 31; i++){
				unanswered[i] = masterArray[0][i];
			}
			for(int i = 0; i < 101; i++){
				unanswered[i + 31] = masterArray[1][i];
			}
		}
		/*
		for(int i =0; i< unanswered.length; i++){
			System.out.println(unanswered[i].getName());
		}*/
		return unanswered;
		
	}
	

	/*
	public int indxVal(String type){
		//uses type of chemistry to find index of array 

		int indx = 0; 

		if(type == "Type 1"){
			indx = 0;
		}

		if(type == "Type 2"){
			indx = 1;
		}

		if(type == "Type 3"){
			indx = 2;
		}

		if(type == "Type 4"){
			indx = 3;
		}

		return indx;
	}
	*/
}
