package blackjack.domain.user;

import blackjack.domain.card.Drawable;
import blackjack.domain.user.exceptions.PlayersException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class Players {
	private final List<Player> players;

	private Players(List<Player> players) {
		validateDistinctNames(players);
		validateNotContainsDealer(players);
		validateIsSizeNotZero(players);
		this.players = players;
	}

	public static Players of(String playerNames) {
		List<Player> players = Arrays.stream(playerNames.split(","))
				.map(String::trim)
				.map(DefaultPlayer::of)
				.collect(collectingAndThen(toList(),
						Collections::unmodifiableList));

		return new Players(players);
	}

	private void validateDistinctNames(List<Player> Players) {
		int distinctCount = (int) Players.stream()
				.map(Player::getName)
				.distinct()
				.count();

		if (Players.size() != distinctCount) {
			throw new PlayersException("중복된 이름이 있으면 안 됩니다.");
		}
	}

	private void validateNotContainsDealer(List<Player> players) {
		boolean hasDealer = players.stream()
				.anyMatch(Player::isDealer);

		if (hasDealer) {
			throw new PlayersException("딜러는 플레이어 목록에 포함될 수 없습니다.");
		}
	}

	private void validateIsSizeNotZero(List<Player> players) {
		if (players.isEmpty()) {
			throw new PlayersException("플레이어는 한 명 이상이어야 합니다.");
		}
	}

	public void giveCardEachPlayer(Drawable deck) {
		for (Player player : players) {
			player.giveCard(deck.draw());
		}
	}

	public int memberSize() {
		return players.size();
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}
}
