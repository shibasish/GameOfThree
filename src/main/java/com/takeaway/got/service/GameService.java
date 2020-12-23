package com.takeaway.got.service;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.dto.ResponseDto;

public interface GameService {

    ResponseDto startGame(CurrentPlayedDto currentPlayedDto);
    ResponseDto playTurn(CurrentPlayedDto currentPlayedDto);
    ResponseDto playManual(CurrentPlayedDto currentPlayedDto);
    int calculateNextMove(int playedNumber);
}
