package com.example.system.of.linear.equations.Nonlinear;

public class Nonlinear {

    public void tryy(){

        try {
            String f = "f(x)= -x^2+6*x-9" ;
            Secant b = new Secant() ;

            System.out.println("this the try" + b.secant(f) );
            System.out.println("has" + b.HasSolution());
            System.out.println("Time " + b.getTime());
            System.out.println(b.getSteps().toString() );
            System.out.println(b.getSteps().size() );

        }catch (Exception e){
            System.out.println("error");

        }

    }
}
