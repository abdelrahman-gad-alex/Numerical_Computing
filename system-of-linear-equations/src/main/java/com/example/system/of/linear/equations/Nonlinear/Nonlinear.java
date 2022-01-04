package com.example.system.of.linear.equations.Nonlinear;
import org.mariuszgromada.math.mxparser.*;

public class Nonlinear {

    public void tryy(){

        String f = "f(x)= -x^2+x+3" ;
        Bracketing b = new Bracketing() ;

        System.out.println("this the try" + b.bisections(f,1,4) );

    }
}
