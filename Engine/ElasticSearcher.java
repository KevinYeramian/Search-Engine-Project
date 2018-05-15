package Engine;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ElasticSearcher implements ElasticSearchInterface {
	
	private static HttpClient httpClient;
	
	public static HttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = HttpClientBuilder.create().build();
		}
		return httpClient;
	}
	
	
	@Override
	public ArrayList<QuestionAnswer> search(Query q, double answersWeight, int size) {
		
		ArrayList<QuestionAnswer> result = new ArrayList<QuestionAnswer>();
		
		try {
		    HttpPost request = new HttpPost("http://localhost:9200/chatbot_index/_search");
		    request.addHeader("content-type", "application/json");
		    
		    JSONArray shouldList = new JSONArray();
		    for (Query.QueryTerm qt : q.queryterm) {
		    	
		    	shouldList.put(new JSONObject()
		    		.put("match", new JSONObject()
		    			.put("question", new JSONObject()
		    				.put("query", qt.term)
		    				.put("fuzziness", 2)
		    				.put("prefix_length", 1)
		    				.put("boost", qt.weight))));
		    	shouldList.put(new JSONObject()
			    		.put("match", new JSONObject()
			    			.put("answers", new JSONObject()
			    				.put("query", qt.term)
			    				.put("fuzziness", 2)
			    				.put("prefix_length", 1)
			    				.put("boost", qt.weight*answersWeight))));
		    	
		    }
		    
		    JSONObject body = new JSONObject()
		    	.put("from", 0)
		    	.put("size", size)
		    	.put("query", new JSONObject()
		    		.put("bool", new JSONObject()
		    			.put("should", shouldList)));
		    
		    StringEntity params = new StringEntity(body.toString());
		    request.setEntity(params);
		    HttpResponse response = getHttpClient().execute(request);
		    HttpEntity entity = response.getEntity();
            if (entity == null) {
            	return null;
            }
            
        	String strResponse = EntityUtils.toString(entity);
            
            try {
                JSONObject parsedResponce = new JSONObject(strResponse);
	            JSONArray hits = parsedResponce.getJSONObject("hits").getJSONArray("hits");
	            for (int i = 0; i < hits.length(); i++) {
	            	JSONObject hit = hits.getJSONObject(i);
	            	
	            	double rank = hit.getDouble("_score");
	            	
	            	JSONObject source = hit.getJSONObject("_source");
	            	JSONArray jsonAnswers = source.getJSONArray("answers");
	            	int id = source.getInt("id");
	            	ArrayList<SearchEntry> answers = new ArrayList<SearchEntry>();
	            	for (int j = 0; j < jsonAnswers.length(); j++) {
	            		answers.add(new SearchEntry(jsonAnswers.getString(j), rank));
	            	}
	            	result.add(new QuestionAnswer(new SearchEntry(source.getString("question"), rank), answers, id));
	            }
            } catch (Exception e) {
            	System.err.println(strResponse);
            	return null;
            }
            
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	
	// TODO: remove
	/*
	public static void main( String[] args ) {
		
		String str = args[0] + " " + args[1];
		//situation svårt
		Query q = new Query(str);
		ArrayList<QuestionAnswer> res = new ElasticSearcher().search(q, 0.1, 10);
		
	}
	*/
}
