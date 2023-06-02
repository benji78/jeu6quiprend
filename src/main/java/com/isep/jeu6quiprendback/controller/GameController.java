package com.isep.jeu6quiprendback.controller;

import com.isep.jeu6quiprendback.DTO.*;
import com.isep.jeu6quiprendback.service.GameService;
import com.isep.jeu6quiprendback.util.Card;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
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
        return gameService.selectCard(selectCardRequestDto.getCardId());
    }

    @GetMapping("/getAllCards")
    public CardsDto getAllCards() {
        // @todo add parameters for the game
        return gameService.getAllCards();
    }

    @PostMapping("/selectStack")
    public GameResponseDto selectStack(@RequestBody StackDto stackDto) {
        return gameService.takeStack(stackDto.getStackId(), stackDto.getName());
    }

}
