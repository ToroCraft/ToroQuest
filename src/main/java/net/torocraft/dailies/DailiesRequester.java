package net.torocraft.dailies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.torocraft.dailies.quests.DailyQuest;

public class DailiesRequester {

	private static final String URL = "http://www.minecraftdailies.com/quests";

	public Set<DailyQuest> getDailies() {
		try {
			return parseResponse(makeRequest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashSet<DailyQuest>(0);
	}

	private String makeRequest() throws IOException {
		HttpURLConnection conn = null;
		String json = null;
		try {
			URL url = new URL(URL);
			conn = (HttpURLConnection) url.openConnection();
			json = s(conn.getInputStream());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return json;
	}

	private String s(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String read;
		while ((read = br.readLine()) != null) {
			sb.append(read);
		}
		br.close();
		return sb.toString();
	}

	private Set<DailyQuest> parseResponse(String jsonString) {
		Set<DailyQuest> dailyQuests = new HashSet<DailyQuest>();
		int maxQuests = 5;
		int i = 0;

		if (jsonString == null) {
			return dailyQuests;
		}
		Gson gson = new GsonBuilder().create();
		DailyQuest[] aQuests = gson.fromJson(jsonString, DailyQuest[].class);
		for (DailyQuest quest : aQuests) {
			dailyQuests.add(quest);
			i++;
			if (i >= maxQuests) {
				return dailyQuests;
			}
		}
		return dailyQuests;
	}
}
