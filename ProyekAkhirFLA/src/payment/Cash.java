package payment;

public class Cash implements Payment{
	private double price;
	public Cash(double price) {
		// TODO Auto-generated constructor stub
		super();
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
		System.out.println("Processing cash payment of " + amount);
		return true;
	}


}
