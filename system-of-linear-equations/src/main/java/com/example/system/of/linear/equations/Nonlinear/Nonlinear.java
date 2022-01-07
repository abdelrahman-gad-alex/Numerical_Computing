package com.example.system.of.linear.equations.Nonlinear;

public class Nonlinear {

    public void tryy(){

        try {
            String f = "f(x)= 3*x-6" ;
            Bisection b = new Bisection().setEs(0.00001).setMaxIterations(50).setNoFigures(0).setxlxu(0,5) ;


            System.out.println("this the try" + b.bisections(f) );
            System.out.println("has" + b.HasSolution());
            System.out.println("Time " + b.getTime());
            System.out.println(b.getSteps().toString() );
            System.out.println(b.getSteps().size() );

        }catch (Exception e){
            System.out.println("error");

        }

    }
}
