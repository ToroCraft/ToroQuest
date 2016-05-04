package net.torocraft.dailies;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.torocraft.dailies.quests.DailyQuest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DailiesRequester {
	
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse response;
	
	private String url = "https://minecraft-dailies.herokuapp.com/quests";
	
	private List<DailyQuest> dailies;
	
	public DailiesRequester() {
		buildHttpClient();
	}
	
	public List<DailyQuest> getDailies() {
		makeRequest();
		parseResponse();
		
		return dailies;
	}
	
	private void buildHttpClient() {
		httpClient = HttpClients.createDefault();
	}
	
	private void makeRequest()  {
		httpGet = new HttpGet(url);
		
		try {
			response = httpClient.execute(httpGet);
		} catch(Exception ex) {
			
		}
	}
	
	private void parseResponse() {
		if(response != null) {
			Type listType = new TypeToken<List<DailyQuest>>(){}.getClass();
			Gson gson = new GsonBuilder().create();
			try {
				String jsonString = EntityUtils.toString(response.getEntity());
				dailies = gson.fromJson(jsonString, listType);
			} catch (Exception ex) {
				
			}
		}
	}
}
