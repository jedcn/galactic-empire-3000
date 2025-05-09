package com.jedcn.ge3000.app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameState {
    private final Map<String, String> playerLocations = new ConcurrentHashMap<>();
    private final Map<String, String> locationDescriptions = new ConcurrentHashMap<>();

    public GameState() {
        // Initialize world
        locationDescriptions.put("forest", "You are in a dark forest. Paths lead north and south.");
        locationDescriptions.put("village", "You are in a small village. The forest lies to the south.");
        locationDescriptions.put("cave", "You are in a damp cave. An exit leads north.");
    }

    public String joinGame(String playerId) {
        playerLocations.put(playerId, "forest");
        return locationDescriptions.get("forest");
    }

    public void leaveGame(String playerId) {
        playerLocations.remove(playerId);
    }

    public String processCommand(String playerId, String command) {
        String currentLocation = playerLocations.get(playerId);
        if (currentLocation == null) return "You are not in the game!";

        String[] parts = command.toLowerCase().split(" ");
        if (parts.length < 2) return "I don't understand that command.";

        String action = parts[0];
        String direction = parts[1];

        if (action.equals("move") || action.equals("go")) {
            switch (currentLocation) {
                case "forest":
                    if (direction.equals("north")) {
                        playerLocations.put(playerId, "village");
                        return locationDescriptions.get("village");
                    } else if (direction.equals("south")) {
                        playerLocations.put(playerId, "cave");
                        return locationDescriptions.get("cave");
                    }
                    break;
                case "village":
                    if (direction.equals("south")) {
                        playerLocations.put(playerId, "forest");
                        return locationDescriptions.get("forest");
                    }
                    break;
                case "cave":
                    if (direction.equals("north")) {
                        playerLocations.put(playerId, "forest");
                        return locationDescriptions.get("forest");
                    }
                    break;
            }
            return "You can't go that way!";
        } else if (action.equals("look")) {
            return locationDescriptions.get(currentLocation);
        }

        return "Unknown command. Try 'move north' or 'look'.";
    }

    public void broadcastState(BroadcastCallback callback) {
        StringBuilder state = new StringBuilder("Current player locations:\n");
        playerLocations.forEach((playerId, location) ->
                state.append(playerId).append(" is in ").append(location).append("\n"));
        callback.broadcast(state.toString());
    }

    public interface BroadcastCallback {
        void broadcast(String message);
    }
}