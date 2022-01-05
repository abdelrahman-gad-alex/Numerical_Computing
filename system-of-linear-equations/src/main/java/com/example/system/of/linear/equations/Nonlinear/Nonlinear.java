package com.example.system.of.linear.equations.Nonlinear;
import org.mariuszgromada.math.mxparser.*;

public class Nonlinear {

    public void tryy(){

        try {
            String f = "f(x)= -x^2+x+3" ;
            Bracketing b = new Bracketing() ;

            System.out.println("this the try" + b.bisections(f,1,4) );

        }catch (Exception e){
            System.out.println("error");

        }

    }
}
