package udemy.scope;

public class AccountMain {

    public static void main(String[] args) {

        Account thisAccount = new Account("Tomi");

        thisAccount.deposit(1000);
        thisAccount.withdraw(500);
        thisAccount.withdraw(-200);
        thisAccount.deposit(-20);
        thisAccount.calculateBalance();



        System.out.println("Balance on this account is " + thisAccount.getBalance());

    }
}
