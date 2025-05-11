package model.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Station;
import model.Trail;

public class JsonReader {

    public static List<Station> readStationsFromJson(String filePath) throws IOException, JsonParseException {
        List<Station> stations = new ArrayList<>();

        try (InputStream inputStream = JsonReader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("JSON file not found in classpath: " + filePath);
            }

            // Read JSON content
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Parse JSON with Gson
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
            JsonArray stationsArray = jsonObject.getAsJsonArray("stations");

            // Parse each station
            for (int i = 0; i < stationsArray.size(); i++) {
                JsonObject stationObj = stationsArray.get(i).getAsJsonObject();

                int id = stationObj.get("id").getAsInt();
                String name = stationObj.get("name").getAsString();
                double x = stationObj.get("x").getAsDouble();
                double y = stationObj.get("y").getAsDouble();

                stations.add(new Station(id, name, x, y));
            }
        } catch (JsonParseException e) {
            throw new JsonParseException("Error parsing JSON file: " + e.getMessage(), e);
        }

        return stations;
    }

    public static List<Trail> readTrailsFromJson(String filePath, List<Station> stations)
            throws IOException, JsonParseException {
        List<Trail> trails = new ArrayList<>();

        try (InputStream inputStream = JsonReader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("JSON file not found in classpath: " + filePath);
            }

            // Create a map of stations by ID for efficient lookup
            Map<Integer, Station> stationMap = stations.stream()
                    .collect(Collectors.toMap(Station::getId, station -> station));

            // Read JSON content
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Parse JSON with Gson
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
            JsonArray trailsArray = jsonObject.getAsJsonArray("trails");

            // Parse each trail
            for (int i = 0; i < trailsArray.size(); i++) {
                JsonObject trailObj = trailsArray.get(i).getAsJsonObject();

                int startId = trailObj.get("startId").getAsInt();
                int endId = trailObj.get("endId").getAsInt();
                int environmentalImpact = trailObj.get("environmentalImpact").getAsInt();

                Station startStation = stationMap.get(startId);
                Station endStation = stationMap.get(endId);

                if (startStation == null || endStation == null) {
                    throw new JsonParseException("Invalid station ID in trail: startId=" + startId + ", endId=" + endId);
                }

                trails.add(new Trail(startStation, endStation, environmentalImpact));
            }
        } catch (JsonParseException e) {
            throw new JsonParseException("Error parsing JSON file: " + e.getMessage(), e);
        }

        return trails;
    }
}