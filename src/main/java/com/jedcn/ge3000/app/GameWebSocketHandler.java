package com.jedcn.ge3000.app;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	private final GameState gameState;

	private final ObjectMapper objectMapper;

	public GameWebSocketHandler(GameState gameState, ObjectMapper objectMapper) {
		this.gameState = gameState;
		this.objectMapper = objectMapper;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String playerId = session.getId();
		sessions.put(playerId, session);

		// Send welcome message
		String welcomeText = gameState.joinGame(playerId);
		GameMessage welcome = new GameMessage("state_update", welcomeText);
		session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcome)));

		// Broadcast player joined
		broadcastToAll("Player " + playerId + " joined the game!");
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String playerId = session.getId();
		GameMessage gameMessage = objectMapper.readValue(message.getPayload(), GameMessage.class);

		if (gameMessage.getType().equals("command")) {
			String result = gameState.processCommand(playerId, gameMessage.getData());

			// Send command response
			GameMessage response = new GameMessage("command_response", result);
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));

			// Broadcast state update to all players
			gameState.broadcastState(this::broadcastToAll);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String playerId = session.getId();
		sessions.remove(playerId);
		gameState.leaveGame(playerId);

		// Broadcast player left
		broadcastToAll("Player " + playerId + " left the game.");
	}

	private void broadcastToAll(String message) {
		GameMessage broadcastMessage = new GameMessage("state_update", message);
		sessions.values().forEach(session -> {
			try {
				session.sendMessage(new TextMessage(objectMapper.writeValueAsString(broadcastMessage)));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}