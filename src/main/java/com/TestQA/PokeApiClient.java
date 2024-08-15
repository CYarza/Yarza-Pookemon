package com.TestQA;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PokeApiClient {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    // Mtodo para verificar la conexión a la API
    public static void checkConnection() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL);
            HttpResponse response = client.execute(request);
            System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
        }
    }

    // Mtodo para filtrar Pokémon por tipo
    public static String getPokemonByType(String type) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "type/" + type);
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                // Crear un StringBuilder para formatear la salida
                StringBuilder result = new StringBuilder();

                // Obtener el array de Pokémon de tipo especificado
                JsonArray pokemonArray = jsonObject
                        .getAsJsonArray("pokemon"); // pokemon es un JsonArray, no un JsonObject

                result.append("Pokémon de tipo ").append(type).append(":\n");
                for (int i = 0; i < pokemonArray.size(); i++) {
                    JsonObject pokemonObject = pokemonArray.get(i).getAsJsonObject();
                    JsonObject pokemonDetails = pokemonObject.getAsJsonObject("pokemon");
                    result.append(pokemonDetails.get("name").getAsString()).append("\n");
                }

                return result.toString();
            } else {
                return "Error: " + response.getStatusLine().getStatusCode();
            }
        }
    }

    // Mtodo para obtener y formatear los detalles del Pokémon
    public static String getFormattedPokemonDetails(String name) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "pokemon/" + name);
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                // Crear un StringBuilder para formatear la salida
                StringBuilder result = new StringBuilder();
                result.append("Name: ").append(jsonObject.get("name").getAsString()).append("\n");

                // Obtener y formatear los tipos del Pokémon
                JsonArray types = jsonObject.getAsJsonArray("types");
                result.append("Types: ");
                for (int i = 0; i < types.size(); i++) {
                    JsonObject type = types.get(i).getAsJsonObject();
                    JsonObject typeDetails = type.getAsJsonObject("type");
                    result.append(typeDetails.get("name").getAsString());
                    if (i < types.size() - 1) {
                        result.append(", ");
                    }
                }
                result.append("\n");

                // Obtener y formatear las habilidades del Pokémon
                JsonArray abilities = jsonObject.getAsJsonArray("abilities");
                result.append("Abilities: ");
                for (int i = 0; i < abilities.size(); i++) {
                    JsonObject ability = abilities.get(i).getAsJsonObject();
                    JsonObject abilityDetails = ability.getAsJsonObject("ability");
                    result.append(abilityDetails.get("name").getAsString());
                    if (i < abilities.size() - 1) {
                        result.append(", ");
                    }
                }
                result.append("\n");

                return result.toString();
            } else {
                return "Error: " + response.getStatusLine().getStatusCode();
            }
        }
    }

    // Mtodo principal
    public static void main(String[] args) throws Exception {
        checkConnection();
        System.out.println(getPokemonByType("fire")); // Obtener Pokémon de tipo fuego

        System.out.println("\nInformación de Pikachu:");
        System.out.println(getFormattedPokemonDetails("pikachu")); // Obtener detalles formateados de Pikachu
    }
}