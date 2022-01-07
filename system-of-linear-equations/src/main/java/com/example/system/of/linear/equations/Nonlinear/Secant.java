package com.example.system.of.linear.equations.Nonlinear;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.HashMap;
import java.util.LinkedList;

public class Secant {

    double es = 0.00001 ;
    int noFigures = 0 ;
    int maxIterations = 50 ;
    LinkedList<HashMap<String,Double>> steps ;
    boolean hasSolution = true ;
    double x0 = 0 ;
    double x1 = 1 ;
    double time ;


    public Secant setEs(double es) {
        this.es = es;
        return this ;
    }
    public Secant setNoFigures(int noFigures) {
        this.noFigures = noFigures;
        return this ;

    }
    public Secant setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this ;
    }
    public Secant setx0(double x0) {
        this.x0 = x0;
        return this ;
    }
    public Secant setx1(double x1) {
        this.x1 = x1;
        return this ;
    }

    public boolean HasSolution(){
        return hasSolution ;
    }
    public LinkedList getSteps(){
        return steps ;
    }

    void endTime(long start){
        long endd = System.nanoTime();
        time = (endd-start) / 1e6 ;

    }
    public double getTime(){
        return time ;
    }

    double fx(String function, double x){
        Function f = new Function(function) ;
        Argument x0 = new Argument("x = 1") ;
        x0.setArgumentValue(x);
        double fx = new Expression("f(x)",f, x0).calculate() ;
        if(Double.isNaN(fx))
            return fx ;
        else
            return round(fx) ;
    }


    // regular
    public double secant (String function){
        steps = new LinkedList<HashMap<String,Double>>() ;

        long start = System.nanoTime();

        if(Double.isNaN(fx(function,x0))){
            hasSolution = false ;
            endTime(start);
            return -1 ;
        }

        double xprev = x0 ;
        double xi = x1 ;
        double fprev =  fx(function,x0) ;
        double fi =  fx(function,x1) ;

        update(x0, fprev, Double.NaN);
        update(x1, fi, Double.NaN);

        for(int i=0 ; i< maxIterations ; i++){

            double xi1 =xi - (fi*(xprev-xi) ) / (fprev-fi) ;
            xi1 = round(xi1) ;
            double f = fx(function,xi1) ;

            double ea =Math.abs(round ((xi1-xi)/xi1)) ;
            update(xi1,f,ea);

            if(ea < es || Math.abs(f)<es){
                hasSolution = true ;
                endTime(start);

                return xi1 ;
            }
            xprev = xi ;
            fprev = fi ;
            xi = xi1;
            fi = f ;
        }
        hasSolution = false ;
        endTime(start);

        return -1 ;
    }





    void update(double xi,double f, double ea){
        HashMap<String ,Double> step = new HashMap<String ,Double>() ;
        step.put("xi",xi) ;
        step.put("fxi",f) ;
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
