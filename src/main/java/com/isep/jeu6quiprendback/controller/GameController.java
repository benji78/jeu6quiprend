package com.isep.jeu6quiprendback.controller;

import com.isep.jeu6quiprendback.DTO.CardsDto;
import com.isep.jeu6quiprendback.DTO.GameResponseDto;
import com.isep.jeu6quiprendback.DTO.SelectCardRequestDto;
import com.isep.jeu6quiprendback.DTO.StartResponseDto;
import com.isep.jeu6quiprendback.service.GameService;
import com.isep.jeu6quiprendback.util.Card;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public StartResponseDto startGame() {
        // @todo add parameters for the game
        return gameService.startGame();
    }

    @PostMapping("/selectCard")
    public GameResponseDto selectCard(@RequestBody SelectCardRequestDto selectCardRequestDto) {
        // @todo add parameters for the game
        return gameService.selectCard(selectCardRequestDto.getCardId(), selectCardRequestDto.getPlayerName());
    }

    @GetMapping("/getAllCards")
    public CardsDto getAllCards() {
        // @todo add parameters for the game
        return gameService.getAllCards();
    }

}