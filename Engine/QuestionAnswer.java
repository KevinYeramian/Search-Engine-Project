package Engine;

import java.util.ArrayList;

public class QuestionAnswer {
	public SearchEntry question;
	public ArrayList<SearchEntry> answer;
	
	public QuestionAnswer(SearchEntry question, ArrayList<SearchEntry> answer) {
		this.question = question;
		this.answer =  new ArrayList<SearchEntry>(answer);
	}
}
