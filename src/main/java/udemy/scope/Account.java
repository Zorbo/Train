package udemy.scope;

import java.util.ArrayList;
import java.util.List;

public class Account {

    protected String accountname;
    private int balance = 0;
    private List<Integer> transactions;

    public Account(String accountname) {
        this.accountname = accountname;
        this.transactions = new ArrayList<>();
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount){
        if (amount > 0){
            transactions.add(amount);
            this.balance += amount;
            System.out.println(amount + " deposited balance is now: " + this.balance);
        } else {
            System.out.println("Cannot deposit negative sums");
        }
    }

    public void withdraw(int amount){
        int withdraw = -amount;
        if (withdraw < 0){
            this.transactions.add(withdraw);
            this.balance += withdraw;
            System.out.println(amount + " withdrawn. Balance is now " + this.balance);
        } else {
            System.out.println("Cannot withdraw negative sums");
        }
    }

    public void calculateBalance(){
        this.balance = 0;
        for (int i : this.transactions){
            this.balance += i;
        }
        System.out.println("Calculated balance is: " + this.balance);
    }
}
