package payment;

public class DigitalWallet implements Payment{
	private double price;
	public DigitalWallet(double price) {
		// TODO Auto-generated constructor stub
		this.price = price;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public boolean processPayment(double amount) {
		// TODO Auto-generated method stub
		System.out.println("Processing digital wallet payment of " + amount);
		return true;
	}


}
