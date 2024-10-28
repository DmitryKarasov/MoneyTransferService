package com.karasov.transfer.repository;

import com.karasov.transfer.models.Card;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Реализация интерфейса {@link CardRepository}, предоставляющая методы для работы с картами.
 * <p>
 * Данный класс содержит фиксированный набор карт, и предоставляет возможность
 * получения карты по номеру карты.
 */
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

    /**
     * Получает карту по номеру карты.
     *
     * @param number номер карты для поиска
     * @return {@link Card} объект, если карта найдена; {@code null}, если карта с указанным номером не найдена
     */
    public Card getCardByNumber(String number) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(number)) {
                return card;
            }
        }
        return null;
    }
}
