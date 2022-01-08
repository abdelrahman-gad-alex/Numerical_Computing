package com.example.system.of.linear.equations.Nonlinear;

import org.mariuszgromada.math.mxparser.* ;
import java.util.HashMap;
import java.util.LinkedList;
public class FixedPoint {
    double es = 0.00001 ;
    int noFigures = 0 ;
    int maxIterations = 50 ;
   double x0 =0;
    double time ;
    LinkedList<HashMap<String,Double>> steps ;
    boolean hasSolution = true ;
    public FixedPoint setEs(double es) {
        this.es = es;
        return this ;
    }
    public FixedPoint setNoFigures(int noFigures) {
        this.noFigures = noFigures;
        return this ;

    }
    public FixedPoint setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this ;
    }
    public FixedPoint setx0(double x0) {
        this.x0 = x0;
        return this ;
    }
    public boolean HasSolution(){
        return hasSolution ;
    }
    public double getTime(){
        return time ;
    }
    void endTime(long start){
        long endd = System.nanoTime();
        time = (endd-start) / 1e6 ;

    }
    public LinkedList getSteps(){
        return steps ;
    }
    double gx(String function, double x){
        Function f = new Function(function) ;
        Argument x0 = new Argument("x = 1") ;
        x0.setArgumentValue(x);
        double gx = round(new Expression("g(x)",f, x0).calculate()) ;
        if(Double.isNaN(gx)){
            hasSolution =false;
        }
        return gx ;
    }
    void gdx(String function, double x) {
        Function f = new Function(function);
        Argument x0 = new Argument("x = 1");
            x0.setArgumentValue(x);
        System.out.println(function);
        double gdx = new Expression("der(g(x),x)", f, x0).calculate();
        System.out.println("GDX" + gdx);

        if( gdx == 0.0){ return;}
        if (!(Math.abs(gdx) < 1)) {
            hasSolution = false;
        } else {
            hasSolution = true;
        }
        System.out.println("aa :"+hasSolution);
    }
    public double fixedpt(String function) {
        long start = System.nanoTime();
        steps = new LinkedList<HashMap<String, Double>>();
        hasSolution = true;
        function = function.replaceAll("f\\(x\\)=","g\\(x\\)=");
        function= function.trim();
       String fn = function +"+x";
        System.out.println("ah:"+fn);
        if(Double.isNaN(gx(fn,x0))){
            hasSolution = false ;
            endTime(start);
            return -1 ;
        }
        double x_old=0 ;
        double ea=0;
        double xr=x0;

        for(int i=0 ; i< maxIterations ; i++){
            x_old=xr;
            System.out.println("old : "+xr);

          //  gdx(fn,xr);
          xr = gx(fn,x_old);
          System.out.println("new :"+xr);
          ea = Math.abs(xr-x_old);
          if (ea<es){
              endTime(start);
              return xr;
          }
            update(x_old,xr,ea);
        }
        if(Math.abs(gx(function,xr))>100){
            hasSolution =false;
            return -1;
        }
        else {
            hasSolution=true;
        }
       /* if(hasSolution == false && flag == false){
            flag =true;
            steps.clear();
           function = function.replaceAll("g\\(x\\)=","");
           function= function.trim();

         String F = "g(x)= (-1*("+function +"))";
          return  fixedpt(F);
        }*/
        endTime(start);
        return xr;
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
    void update(double xr,double gx, double ea){
        HashMap<String ,Double> step = new HashMap<String ,Double>() ;
        step.put("xr",xr) ;
        step.put("gx",gx) ;
        step.put("ea",ea) ;
        steps.add(step) ;

    }


}
