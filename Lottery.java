import java.security.SecureRandom;
import java.util.Random;

public class Lottery{


	public Lottery(){
		
	}

	/*Returns a random QuestionNode*/
	public QuestionNode getRandomNode(QuestionNode[] lottery){
		Random rand = new SecureRandom();
		
		int  n = rand.nextInt(9);
		while(lottery[n] == null){
			//generates a random integer between 0 and 10
			 n = rand.nextInt(9);
			//returns the question node that is found at the random index of the lottery
		}
		
		return lottery[n];
	}
	
	//creates a pool of ten numbers to draw from
	public QuestionNode[] Lottery(QuestionNode[] hard, QuestionNode[] harder, QuestionNode[] hardest, QuestionNode[] unanswered, QuestionNode[] easy){
		//queue to change per plant
		QuestionNode[] lottery = new QuestionNode[10];
		
		//3 hardest
		//2 harder
		//1 hard
		//4 unanswered
		//if unanswered is empty go to easy
		
		Random rand = new SecureRandom();
		
		/*The following code generates decreasing numbers of random integers based
		 * on the lengths of the various questionNodearrays. Since it is based off
		 * of their lengths, the goal is to have no null values. There are more
		 * values in the pool for the hardest questions so they are more likely to
		 * appear more often */
		int count = 0;
		if(hardest[2] != null){
			for(int i = 0; i < 3; i++){
				int  n = rand.nextInt(hardest.length);
				lottery[i] = hardest[n];
				count += 1;
			}
		}
		if(harder[1] != null){
			for(int i = 0; i < 2; i++){
				int  n = rand.nextInt(harder.length);
				lottery[count] = harder[n];
				count += 1;
			}
		}
		if(hard[0] != null){
			for(int i = 0; i < 1; i++){
				int  n = rand.nextInt(hard.length);
				lottery[count] = hard[n];
				count += 1;
			}
		}
		if(unanswered[9] != null){
			int ugh = count;
			for(int i = 0; i < (10-ugh); i++){
				int  n = rand.nextInt(unanswered.length);
				lottery[count] = unanswered[n];
				count += 1;
			}
		}
		else{
			int ugh = count;
			for(int i = 0; i < (10-count); i++){
				int  n = rand.nextInt(easy.length);
				lottery[count] = easy[n];
				count += 1;
			}
		}
		
		return lottery;
		
	}
	
	/*This method takes a parameterized node from one questionNode array and writes it into another
	 * questionNode array. Since the goal is to prevent the lottery from choosing from null values,
	 * the movingFrom questionNode array must be shifted and the movingTo questionNode array must
	 * be appended to.*/
	public void shiftDifficulty(QuestionNode[] movingFrom, QuestionNode[] movingTo, QuestionNode theNode){
		int theNodeIndex = 0;
		int moveToHere = 0;
		
		//gets index of the node we are shifting
		for(int i= 0; i < movingFrom.length; i++){
			if(movingFrom[i] == theNode){
				theNodeIndex = i;
			}
		}
		
		//moves every node past the index of the node we are shifting over one to the left 
		//so that we rid of the node we don't want. also so that all of the null values are at the end
		QuestionNode holding = new QuestionNode();
		int finalIndex = 0;
		if(theNodeIndex != movingFrom.length){
			for(int i = theNodeIndex; i< movingFrom.length - 1; i++){
				holding = movingFrom[i + 1];
				movingFrom[i] = holding;
				finalIndex = i;
			}
			movingFrom[finalIndex] = null;
		}	
		
		
		//finds where the first null in the list is
		for(int i = 0; i< movingTo.length; i++){
			if(movingTo[i] == null){
				moveToHere = i;
				break;
			}
		}
		
		//assigns theNode to its new home
		movingTo[moveToHere] = theNode;
	}
	
	/*finds the questionNode array in which a parameterized node appears*/
	public QuestionNode[] findQuestionNodeArray(QuestionNode[] hard, QuestionNode[] harder, QuestionNode[] hardest, QuestionNode[] unanswered, QuestionNode[] easy, QuestionNode currentNode){
		for(int i = 0; i< harder.length; i++){
			if(harder[i].equals(currentNode)){
				return harder;
			}
			
		}
		for(int i = 0; i< hardest.length; i++){
			if(hardest[i].equals(currentNode)){
				return hardest;
			}
		}
		for(int i = 0; i< hard.length; i++){
			if(hard[i].equals(currentNode)){
				return hard;
			}
		}
		for(int i = 0; i< unanswered.length; i++){
			if(unanswered[i].equals(currentNode)){
				return unanswered;
			}
		}
		for(int i = 0; i< easy.length; i++){
			if(easy[i].equals(currentNode)){
				return easy;
			}
		}
		
		return null;
	}
}
