package blackjack.domain.card;

import blackjack.domain.card.exceptions.DeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class CardsTest {
	private Deck simpleDeck;
	private List<Card> fourCards;

	@BeforeEach
	void setUp() {
		fourCards = new ArrayList<>(Arrays.asList(Card.of(Symbol.THREE, Type.HEART),
				Card.of(Symbol.FOUR, Type.DIAMOND),
				Card.of(Symbol.FIVE, Type.CLUB),
				Card.of(Symbol.SIX, Type.SPADE)));

		simpleDeck = Cards.of(fourCards);
	}

	@Test
	void create() {
		// then
		assertThat(simpleDeck).isNotNull();
	}

	@Test
	void draw() {
		// given
		List<Card> expected = new ArrayList<>(fourCards);
		Collections.reverse(expected);

		// when
		List<Card> drawn = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			drawn.add(simpleDeck.draw());
		}

		// then
		assertThat(drawn)
				.isEqualTo(expected);
	}

	@Test
	void draw_ThereAreNoCard_ShouldThrowException() {
		// given
		for (int i = 0; i < 4; i++) {
			simpleDeck.draw();
		}

		// then
		assertThatThrownBy(() -> {
			// when
			simpleDeck.draw();
		}).isInstanceOf(DeckException.class);
	}

	@Test
	void equals() {
		// given
		Deck expected = Cards.of(fourCards);
		Deck notExpected = Cards.of(Collections.singletonList(Card.of(Symbol.THREE, Type.HEART)));

		// then
		assertThat(simpleDeck.equals(expected)).isTrue();
		assertThat(simpleDeck.equals(notExpected)).isFalse();
	}
}