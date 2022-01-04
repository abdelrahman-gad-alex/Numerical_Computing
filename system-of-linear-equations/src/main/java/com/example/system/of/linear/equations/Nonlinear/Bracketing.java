package com.example.system.of.linear.equations.Nonlinear;
import org.mariuszgromada.math.mxparser.*;

public class Bracketing {

    double es = 0.00001 ;
    int noFigures = 0 ;
    int maxIterations = 50 ;

    boolean hasSolution ;

    public void setEs(double es) {this.es = es;}
    public void setNoFigures(int noFigures) {this.noFigures = noFigures;}
    public void setMaxIterations(int maxIterations) {this.maxIterations = maxIterations;}


    public double bisections(String function, double xl, double xu){
        Function f = new Function(function) ;
        Argument x = new Argument("x = 1") ;


        // testing the number of roots
        x.setArgumentValue(xl);
        double fl = new Expression("f(x)",f, x).calculate();
        x.setArgumentValue(xu);
        double fr = new Expression("f(x)",f, x).calculate();
        if(fl*fr > 0){  // same sign
            hasSolution = false ;
            return -1 ;
        }

        double xr=0 ;
        double ea ;
        for(int i=0 ; i< maxIterations ; i++){
            xr = (xu+xl) /2.0 ;
            System.out.println(xr);
            ea = Math.abs(xu-xl)/xl ;

            x.setArgumentValue(xl);
            fl = new Expression("f(x)",f, x).calculate();
            x.setArgumentValue(xr);
            fr = new Expression("f(x)",f, x).calculate();

            if(fl*fr <0 ){ // test
                xu = xr ;
            }else {
                xl = xr ;
            }

            if(Math.abs(fr) <0.000000001 || ea<es){
                System.out.print("f(xr)=");
                System.out.println(fr);
                System.out.print("ea=");
                System.out.println(ea);

                hasSolution = true ;
                break;
            }
        }
        return xr ;

    }

}
