package com.isep.jeu6quiprendback.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartResponseDto {

    private String cardStacksJson;
    private String scoreBoardJson;
    private String playerCardsJson;

}
