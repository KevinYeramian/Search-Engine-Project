package Engine;

import java.util.ArrayList;

public interface ElasticSearchInterface {
	
	public ArrayList<QuestionAnswer>search(Query q);
	
}
