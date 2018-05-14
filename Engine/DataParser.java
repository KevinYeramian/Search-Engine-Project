package Engine;

import java.io.*;
import java.nio.charset.*;
import org.json.*;

public class DataParser {
	
	public static void main( String[] args ) {
		
		if (args.length == 0) {
			System.err.println("Pass the data set path as an argument.");
			return;
		}
		
		int sizeOption = 1000;
		if (args.length > 1) {
			sizeOption = Integer.parseInt(args[1]);
		}
		
		String path = args[0];
		
		File fQuestions = new File(path, "questions_" + sizeOption + ".txt");
		File fAnswers = new File(path, "answers_" + sizeOption + ".txt");
		File fResult = new File("data_" + sizeOption + ".json");
		InputStreamReader questionsReader;
		InputStreamReader answersReader;
		OutputStreamWriter resultWriter;
		try {
			questionsReader = new InputStreamReader(new FileInputStream(fQuestions), StandardCharsets.UTF_8);
			answersReader = new InputStreamReader(new FileInputStream(fAnswers), StandardCharsets.UTF_8);
			resultWriter = new OutputStreamWriter(new FileOutputStream(fResult), StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		BufferedReader questionsBufferedReader = new BufferedReader(questionsReader);
		BufferedReader answersBufferedReader = new BufferedReader(answersReader);
		BufferedWriter resultBufferedWriter = new BufferedWriter(resultWriter);
		
		try {
			String questionLine;
			String answerLine = answersBufferedReader.readLine();
			while ((questionLine = questionsBufferedReader.readLine()) != null) {
				
				String[] questionLineParts = questionLine.split(" ");
				int questionNumber = Integer.parseInt(questionLineParts[0]);
				
				JSONObject row = new JSONObject();
				row.put("id", questionNumber);
				row.put("question", questionLine.substring(questionLineParts[0].length() + 1));
				
				JSONArray answersArray = new JSONArray();
				String[] answerLineParts;
				while (Integer.parseInt((answerLineParts = answerLine.split(" "))[0]) == questionNumber ) {
					answersArray.put(answerLine.substring(answerLineParts[0].length() + 1));
					answerLine = answersBufferedReader.readLine();
					if (answerLine == null) {
						break;
					}
				}
				
				row.put("answers", answersArray);
				
				resultBufferedWriter.write(row.toString() + "\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			questionsBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			answersBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			resultBufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
