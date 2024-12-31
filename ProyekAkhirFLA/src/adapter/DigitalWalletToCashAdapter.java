package adapter;

import payment.Cash;
import payment.DigitalWallet;

public class DigitalWalletToCashAdapter extends Cash {

	public DigitalWalletToCashAdapter(DigitalWallet digitalWallet) {
		// TODO Auto-generated constructor
		super(digitalWallet.getPrice()*1.1);
	}

}
