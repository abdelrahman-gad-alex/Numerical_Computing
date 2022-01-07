package com.example.system.of.linear.equations.Nonlinear;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.HashMap;
import java.util.LinkedList;

public class NewtonRaphson {

    double es = 0.00001 ;
    int noFigures = 0 ;
    int maxIterations = 50 ;
    LinkedList<HashMap<String,Double>> steps ;
    boolean hasSolution = true ;
    double x0 = 0 ;
    double time ;


    public NewtonRaphson setEs(double es) {
        this.es = es;
        return this ;
    }
    public NewtonRaphson setNoFigures(int noFigures) {
        this.noFigures = noFigures;
        return this ;

    }
    public NewtonRaphson setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this ;
    }
    public NewtonRaphson setx0(double x0) {
        this.x0 = x0;
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
    double fdx(String function, double x){
        Function f = new Function(function) ;
        Argument x0 = new Argument("x = 1") ;
        x0.setArgumentValue(x);
        double fd = new Expression("der(f(x),x)",f, x0).calculate() ;
        if(Double.isNaN(fd))
            return fd ;
        else
            return round(fd) ;
    }
    double fddx(String function, double x){
        Function f = new Function(function) ;
        Argument x0 = new Argument("x = 1") ;
        x0.setArgumentValue(x);
        double fd = new Expression("der( der(f(x),x), x)",f, x0).calculate() ;
        if(Double.isNaN(fd))
            return fd ;
        else
            return round(fd) ;
    }

    // regular
    public double newton (String function){
        steps = new LinkedList<HashMap<String,Double>>() ;

        long start = System.nanoTime();

        if(Double.isNaN(fx(function,x0))){
            hasSolution = false ;
            endTime(start);
            return -1 ;
        }
        double xi = x0 ;


        for(int i=0 ; i< maxIterations ; i++){
            double fi = fx(function, xi) ;
            double fd = fdx(function, xi) ;
            double xi1 =round(xi - fi/fd) ;
            double ea =Math.abs(round ((xi1-xi)/xi1)) ;
            double f = fx(function, xi1) ;
            update(xi1,f,xi,fi,fd,ea);

            if(ea < es || Math.abs(f)<es/10){
               hasSolution = true ;
                endTime(start);

                return xi1 ;
            }

            xi = xi1;
        }
        hasSolution = false ;
        endTime(start);

        return -1 ;
    }

    // modified 1
    public double newtonModify1(String function, int m){
        steps = new LinkedList<HashMap<String,Double>>() ;

        long start = System.nanoTime();

        if(Double.isNaN(fx(function,x0))){
            hasSolution = false ;
            endTime(start);

            return -1 ;
        }
        double xi = x0 ;

        for(int i=0 ; i< maxIterations ; i++){
            double fi = fx(function, xi) ;
            double fd = fdx(function, xi) ;
            double xi1 =round(xi - (m*fi)/fd) ;
            double ea =Math.abs(round ((xi1-xi)/xi1)) ;
            double f = fx(function, xi1) ;

            update(xi1,f,xi,fi,fd,ea);

            if(ea < es || Math.abs(f)<es){
               hasSolution = true ;
                endTime(start);

                return xi1 ;
            }

            xi = xi1;
        }
        hasSolution = false ;
        endTime(start);

        return -1 ;
    }

    // modified 2
    public double newtonModify2 (String function){
        steps = new LinkedList<HashMap<String,Double>>() ;

        long start = System.nanoTime();

        if(Double.isNaN(fx(function,x0))){
            hasSolution = false ;
            endTime(start);

            return -1 ;
        }
        double xi = x0 ;

        for(int i=0 ; i< maxIterations ; i++){
            double fi = fx(function, xi) ;
            double fd = fdx(function, xi) ;
            double fdd = fddx(function, xi) ;
            double xi1 =xi - fi*fd/(fd*fd - fi*fdd)    ;
            xi1 = round(xi1) ;
            double f = fx(function, xi1) ;

            double ea =Math.abs(round ((xi1-xi)/xi1)) ;

            updateModified(xi1,f,xi,fi,fd,fdd,ea);

            //if(ea < es || Math.abs(f)<es){
            if(ea < es ){
                hasSolution = true ;
                endTime(start);

                return xi1 ;
            }

            xi = xi1;
        }
        hasSolution = false ;
        endTime(start);

        return -1 ;
    }







    void update(double xi1,double fi1, double xi,double f,double fd,double ea){
        HashMap<String ,Double> step = new HashMap<String ,Double>() ;
        step.put("xi1",xi1) ;
        step.put("fxi1",fi1) ;
        step.put("xi",xi) ;
        step.put("fxi",f) ;
        step.put("fdxi",fd) ;
        step.put("ea",ea) ;

        steps.add(step) ;
    }
    void updateModified(double xi1,double fi1, double xi,double fi,double fd, double fdd, double ea){
        HashMap<String ,Double> step = new HashMap<String ,Double>() ;
        step.put("xi1",xi1) ;
        step.put("fxi1",fi1) ;
        step.put("xi",xi) ;
        step.put("fxi",fi) ;
        step.put("fdxi",fd) ;
        step.put("fddxi",fdd) ;
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
