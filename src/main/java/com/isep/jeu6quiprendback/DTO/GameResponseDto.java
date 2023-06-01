package com.isep.jeu6quiprendback.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponseDto {

    private String cardStacksJson;
    private String scoreBoardJson;
    private String playerCardsJson;
    private String message;

}
