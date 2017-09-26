package udemy.scope.scopechallange;

public class X {

    private int x;

    public void x(int x){

        for (this.x = 1; this.x <= 12;  this.x++){
            System.out.println(this.x + " * " + x + " = " + this.x * x);
        }
    }
}
