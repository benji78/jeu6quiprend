package com.isep.jeu6quiprendback.service;

import com.isep.jeu6quiprendback.DTO.CardsDto;
import com.isep.jeu6quiprendback.DTO.GameResponseDto;
import com.isep.jeu6quiprendback.DTO.StartResponseDto;
import com.isep.jeu6quiprendback.util.Game;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final Game game; // Inject the Game instance

    public GameService(Game game) {
        this.game = game;
    }

    public StartResponseDto startGame() {

        JSONArray jsonArray = game.start();

        String cardStacksJson = jsonArray.get(0).toString();
        String scoreBoardJson = jsonArray.get(1).toString();
        String playerCardsJson = jsonArray.get(2).toString();

        return StartResponseDto.builder()
                .cardStacksJson(cardStacksJson)
                .scoreBoardJson(scoreBoardJson)
                .playerCardsJson(playerCardsJson)
                .build();
    }

    public GameResponseDto selectCard(int cardId, String playerName) {

        JSONArray jsonArray = game.selectCard(cardId, playerName);

        String cardStacksJson = jsonArray.get(0).toString();
        String scoreBoardJson = jsonArray.get(1).toString();
        String playerCardsJson = jsonArray.get(2).toString();

        return GameResponseDto.builder()
                .cardStacksJson(cardStacksJson)
                .scoreBoardJson(scoreBoardJson)
                .playerCardsJson(playerCardsJson)
                .build();

    }

    //getAllCards
    public CardsDto getAllCards() {

        JSONArray jsonArray = game.getAllCards();

        String playerCardsJson = jsonArray.get(0).toString();

        return CardsDto.builder()
                .cardsJson(playerCardsJson)
                .build();

    }
}