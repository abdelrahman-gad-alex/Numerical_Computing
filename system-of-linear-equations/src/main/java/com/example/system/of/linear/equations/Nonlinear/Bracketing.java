package com.example.system.of.linear.equations.Nonlinear;

import org.mariuszgromada.math.mxparser.* ;

import java.util.HashMap;
import java.util.LinkedList;

public class Bracketing {

    double es = 0.00001 ;
    int noFigures = 0 ;
    int maxIterations = 50 ;
    LinkedList<HashMap<String,Double>> steps ;
    boolean hasSolution ;

    public void setEs(double es) {this.es = es;}
    public void setNoFigures(int noFigures) {this.noFigures = noFigures;}
    public void setMaxIterations(int maxIterations) {this.maxIterations = maxIterations;}

    public boolean HasSolution(){
        return hasSolution ;
    }

    public LinkedList getSteps(){
        return steps ;
    }


    public double bisections(String function, double xl, double xu){
        steps = new LinkedList<HashMap<String,Double>>() ;
        Function f = new Function(function) ;
        Argument x = new Argument("x = 1") ;


        // testing the number of roots
        x.setArgumentValue(xl);
        double fl = round(new Expression("f(x)",f, x).calculate()) ;
        x.setArgumentValue(xu);
        System.out.print("f(xl)=");
        System.out.println(fl);
        double fr =  round(new Expression("f(x)",f, x).calculate()) ;
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
            fl = round(new Expression("f(x)",f, x).calculate());
            x.setArgumentValue(xr);
            fr = round(new Expression("f(x)",f, x).calculate());

            update(xl, xu, xr, fr, ea);

            if(fl*fr <0 ){ // test
                xu = xr ;
            }else {
                xl = xr ;
            }

            if(Math.abs(fr) <es || ea<es){
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
    void update(double xl,double xu,double xr,double fr,double ea){
        HashMap<String ,Double> step = new HashMap<String ,Double>() ;
        step.put("xl",xl) ;
        step.put("xu",xu) ;
        step.put("xr",xr) ;
        step.put("f(xr)",fr) ;
        step.put("ea",ea) ;
        steps.add(step) ;
    }


    double round(double x){
        if(noFigures ==0){
            return x ;
        }

        if(Math.abs(x)< es){
            return 0 ;
        }
        int shift = (int)Math.ceil(Math.log10(x)) - noFigures;
        x = x/ Math.pow(10, shift) ;
        x = Math.round(x) ;
        x = x*  Math.pow(10, shift) ;
        x = Double.parseDouble( String.format("%.10g%n",x) ) ;
        return x ;
    }

}
