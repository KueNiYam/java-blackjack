package blackjack.view;

import blackjack.domain.card.Card;
import blackjack.domain.user.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class OutputView {
	private static final String NEW_LINE = System.lineSeparator();
	public static final String BUST_MESSAGE = "버스트 되었습니다!!!";

	public static void printStartInfo(Playable dealer, Players players) {
		printStartInfoHead(dealer, players);
		printPlayerCard(dealer);
		printAllPlayerCards(players);
	}

	private static void printStartInfoHead(Playable dealer, Players players) {
		String playerNames = players.getPlayers().stream()
				.map((player) -> player.getName().getString())
				.collect(Collectors.joining(", "));

		System.out.printf("%s와 %s에게 %d장을 나누었습니다." + NEW_LINE,
				dealer.getName().getString(), playerNames, dealer.getHand().getHand().size());
	}

	private static void printAllPlayerCards(Players players) {
		for (Playable player : players.getPlayers()) {
			printPlayerCard(player);
		}
	}

	public static void printPlayerCard(Playable player) {
		String userCards = createPlayerStartCardInfo(player);
		System.out.printf("%s : %s" + NEW_LINE, player.getName().getString(), userCards);

		printIfBust(player);
	}

	private static String createPlayerStartCardInfo(Playable player) {
		return player.getStartHand().stream()
				.map(Card::getName)
				.collect(Collectors.joining(", "));
	}

	public static void printDealerTurn(Playable dealer) {
		System.out.printf("딜러는 %s미만이라 한장의 카드를 더 받았습니다." + NEW_LINE,
				Playable.MINIMUM_NUMBER_TO_DEALER_STAY);

		printIfBust(dealer);
	}

	private static void printIfBust(Playable player) {
		if (player.isBust()) {
			System.out.println(BUST_MESSAGE);
		}
	}

	public static void printFinalInfo(Playable dealer, Players players) {
		List<Playable> users = new ArrayList<>();
		users.add(dealer);
		users.addAll(players.getPlayers());

		for (Playable player : users) {
			String userCards = createPlayerCardInfo(player);
			String score = createResultScore(player);

			System.out.printf("%s : %s - 결과: %s" + NEW_LINE,
					player.getName().getString(), userCards, score);
		}
	}

	private static String createPlayerCardInfo(Playable player) {
		return player.getHand().getHand().stream()
				.map(Card::getName)
				.collect(Collectors.joining(", "));
	}

	private static String createResultScore(Playable player) {
		if (player.isBust()) {
			return "bust";
		}
		return String.valueOf(player.computeScore().getScore());
	}

	public static void printResult(Results results) {
		System.out.println("## 최종 승패");
		System.out.println("딜러" + " : " + createDealerResult(results));
		for (Result result : results.getResults()) {
			System.out.printf("%s : %s\n", result.getPlayable().getName().getString(),
					boolToResultWord(result.getResultType()));
		}
	}

	private static String createDealerResult(Results results) {
		return String.format("%d승 %d무 %d패",
				results.getDealerWin(), results.getDealerDraw(), results.getDealerLose());
	}

	private static String boolToResultWord(ResultType resultType) {
		if (resultType.isWinOrBlackjackWin()) {
			return "승";
		}
		if (resultType.isDraw()) {
			return "무";
		}

		return "패";
	}

	public static void printGameResultMonies(Results results) {
		System.out.println("## 최종수익");
		System.out.printf("%s: %f\n", Playable.DEALER_NAME, results.getDealerMoney());
		for (Result result : results.getResults()) {
			System.out.printf("%s: %f\n", result.getPlayable().getName().getString(),
					result.getResultType().computeResultAmount(
							((Player)result.getPlayable()).getMoney().getAmount()));
		}
	}
}
