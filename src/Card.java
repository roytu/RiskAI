
public class Card {
	Territory territory;
	enum UnitType { MAN, HORSE, CANNON };
	UnitType type;
	public Card(Territory territory, UnitType type)
	{
		this.territory = territory;
		this.type = type;
	}

}
