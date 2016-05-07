package net.torocraft.dailies;

import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.torocraft.dailies.quests.DailyQuest;

public class DailiesRequester {

	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse response;

	private String url = "https://minecraft-dailies.herokuapp.com/quests";

	private Set<DailyQuest> dailyQuests;

	public DailiesRequester() {
		buildHttpClient();
	}

	public Set<DailyQuest> getDailies() {
		dailyQuests = new HashSet<DailyQuest>();
		makeRequest();
		parseResponse();

		return dailyQuests;
	}

	private void buildHttpClient() {
		httpClient = HttpClients.createDefault();
	}

	private void makeRequest() {
		httpGet = new HttpGet(url);

		try {
			response = httpClient.execute(httpGet);
		} catch (Exception ex) {

		}
	}

	private void parseResponse() {
		int maxQuests = 5;
		int i = 0;
		if (response != null) {
			Gson gson = new GsonBuilder().create();
			try {
				String jsonString = EntityUtils.toString(response.getEntity());
				DailyQuest[] aQuests = gson.fromJson(jsonString, DailyQuest[].class);
				for (DailyQuest quest : aQuests) {
					dailyQuests.add(quest);
					i++;
					if (i >= maxQuests) {
						return;
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
