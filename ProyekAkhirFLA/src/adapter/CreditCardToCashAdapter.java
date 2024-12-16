package adapter;

import payment.Cash;
import payment.CreditCard;

public class CreditCardToCashAdapter extends Cash{

	public CreditCardToCashAdapter(CreditCard card) {
		// TODO Auto-generated constructor stub
		super(card.getPrice()/1.5);
	}

}
