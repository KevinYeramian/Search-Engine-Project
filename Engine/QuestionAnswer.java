package Engine;

import java.util.ArrayList;

public class QuestionAnswer {
	
	public final int id;
	public SearchEntry question;
	public ArrayList<SearchEntry> answer;
	
	public QuestionAnswer(SearchEntry question, ArrayList<SearchEntry> answer) {
		this.question = question;
		this.answer =  new ArrayList<SearchEntry>(answer);
		this.id = -1;
	}
	
	public QuestionAnswer(SearchEntry question, ArrayList<SearchEntry> answer, int id) {
		this.question = question;
		this.answer =  new ArrayList<SearchEntry>(answer);
		this.id = id;
	}
}
