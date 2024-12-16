package adapter;

import payment.Cash;

public class DigitalWalletToCashAdapter extends Cash {

	public DigitalWalletToCashAdapter(DigitalWalletToCashAdapter dw) {
		// TODO Auto-generated constructor 
		super(dw.getPrice()*1.1);
	}

}
