package udemy.scope;

interface Accessible {  //package private

    int SOME_CONSTANT = 100; //public
    public void methodA();   // public
    void methodB();          // public
    boolean methodC();       // public


}
