package com.karasov.transfer.repository;

import com.karasov.transfer.models.Card;

public interface CardRepository {

    Card getCardByNumber(String number);

}
