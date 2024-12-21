package payment;

public class PaymentProxy implements Payment{

	private Payment payment;
	private String userRole;

    public PaymentProxy(Payment payment, String userRole){
        this.payment = payment;
        this.userRole = userRole;
    }

    @Override
    public boolean processPayment(double amount){
        System.out.println("Verifying payment...");
        if (isValid()) {
            double finalAmount = calculateTax(amount);
            System.out.println("Final amount after tax: " + finalAmount);
            return payment.processPayment(finalAmount);
        } else {
            System.out.println("Payment verification failed");
            return false;
        }
    }

    private boolean isValid(){
    	if (!userRole.equals("Registrant")) {
            System.out.println("Access denied. Only users with the role 'registrant' can process payments");
            return false;
        }

    	return true;
    }

    private double calculateTax(double amount) {
        double taxRate;
        if(payment instanceof Cash) {
            taxRate = 0.05;
            System.out.println("Applying 5% tax for cash");
        }
        else if(payment instanceof CreditCard) {
            taxRate = 0.10;
            System.out.println("Applying 10% tax for credit card");
        }
        else{
            taxRate = 0.0;
        }
        return amount + (amount * taxRate);
    }

}
