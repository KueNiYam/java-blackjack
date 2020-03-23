package blackjack.domain.user;

import blackjack.domain.card.Card;
import blackjack.domain.result.ResultType;
import blackjack.domain.user.exceptions.PlayerException;

import java.util.List;

public final class Player extends AbstractPlayer {
	private final Money money;

	private Player(Name name, Money money) {
		super(name);
		this.money = money;
		validateNameIsDifferentFromDealer(name);
	}

	private void validateNameIsDifferentFromDealer(Name name) {
		if (name.equals(new Name(DEALER_NAME))) {
			throw new PlayerException("플레이어의 이름은 " + DEALER_NAME + "일 수 없습니다.");
		}
	}

	public static Player of(String name, String money) {
		return new Player(new Name(name), Money.of(money));
	}

	public Money computeResultMoney(ResultType resultType) {
		return money.computeResultingAmount(resultType);
	}

	public Money getMoney() {
		return money;
	}

	@Override
	public List<Card> getStartHand() {
		return getHand().getHand();
	}

	@Override
	public boolean canReceiveCard() {
		return !getHand().isBust();
	}
}
