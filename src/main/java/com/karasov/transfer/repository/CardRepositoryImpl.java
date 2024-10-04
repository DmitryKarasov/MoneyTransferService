package com.karasov.transfer.repository;

import com.karasov.transfer.models.Card;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class CardRepositoryImpl implements CardRepository {

    private final Set<Card> cards;

    {
        cards = Set.of(
                new Card(
                        "1111111111111111",
                        "1231",
                        "111",
                        1000.0
                ),
                new Card(
                        "2222222222222222",
                        "1232",
                        "222",
                        2000.0
                ),
                new Card(
                        "3333333333333333",
                        "1233",
                        "333",
                        3000.0
                )
        );
    }

    public Card getCardByNumber(String number) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(number)) {
                return card;
            }
        }
        return null;
    }
}
