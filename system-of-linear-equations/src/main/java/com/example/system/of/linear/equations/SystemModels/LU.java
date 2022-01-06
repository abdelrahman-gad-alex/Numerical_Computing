package com.example.system.of.linear.equations.SystemModels;

import java.util.Arrays;
import java.util.LinkedList;

public class LU implements ISystem{
    public double[][] system ;
    public double[][] l ;
    public double[][] u ;
    public double[] b ;
    // system = [ A  | b ]
    double[] solution ;
    int n ;
    double es = 1e-10 ;
    int figures ;
    boolean hasSolution ;
    LinkedList<double[][]> steps ;
    String theMethod ;
    double time ;


    public LU(String method){
        theMethod = method ;
    }

    public void setSystem(double[][] A, int f){
        figures = f ;
        system = new double[A.length][A.length] ;
        b = new double[A.length] ;
        n = system.length ;
        for(int i=0 ; i<n ; i++){
            for(int j=0 ; j<n ; j++){
                system[i][j] = round(A[i][j]) ;
            }
            b[i] = A[i][n] ;
        }
    }

    public double[] solve(){
        long start = System.nanoTime();


        steps = new LinkedList<>();
        eliminate();


        if(theMethod.equals("doolittle")){
            for(int i=0 ; i<n; i++){
                for (int j=0 ; j<i ; j++){
                    l[i][j] = system[i][j] ;
                    system[i][j] = 0 ;
                }
                l[i][i] = 1 ;
            }
            u = system ;
        }
        steps.add(l) ;
        steps.add(u) ;
        system = l ;
        print();
        system = u;
        print();

        double[][] y = new double[1][n] ;
        y[0] = forwardSubsitution(l, b) ;
        solution = backSubsitution(u,y[0]) ;
        steps.add(y) ;

        long endd = System.nanoTime();
        time = (endd-start) / 1e6 ;

        return solution ;

    }
    public double getTime(){
        return time ;
    }

    private void eliminate(){
        l = new double[n][n] ;
        u = new double[n][n] ;
        if(theMethod.equals("doolittle")){
            doolittle();
        }else {
            crout();
        }

    }


    private void doolittle(){

        for(int i=0 ; i< n ; i++){

            if(Math.abs(system[i][i]) < es){
                // has infinite NO solutions
                hasSolution = false ;
                return;
            }

            for(int k=i+1 ; k<n ; k++){
                if(Math.abs(system[k][i]) < es){
                    continue;
                }
                double f = system[k][i]/ system[i][i] ;   // l od k,i
                system[k][i] = f ;
                for(int j=i+1; j< n ; j++){
                    system[k][j] =round(system[k][j]-f*system[i][j]) ;

                }
            }

        }
        hasSolution = true ;

    }


    private void crout(){
        for(int i=0 ; i<n ; i++){
            u[i][i] =1 ;
        }
        for(int j=0 ; j<n ; j++){
            for(int i=j ; i<n; i++){
                l[i][j] = system[i][j] ;
                for(int k=0 ; k<j ; k++){
                    l[i][j] -= round(l[i][k]*u[k][j]) ;
                }

            }

            for (int i=j ; i<n ; i++){
                u[j][i] = system[j][i] ;
                for (int k=0 ; k<j ; k++){
                    u[j][i] -=round(l[j][k] * u[k][i] ) ;
                }
                if(l[j][j] ==0){
                    hasSolution = false ;
                    return;
                }
                u[j][i] = round(u[j][i]/l[j][j]) ;



            }
        }
    }

    private double[] backSubsitution(double[][] u ,double [] b){
        double[] x = new double[n];
        for(int i=n-1 ; i>= 0 ; i--){
            x[i] = b[i] ;
            for(int j=i+1 ; j<n ; j++){
                x[i] -= u[i][j]*x[j] ;
            }
            x[i] =round(x[i]/u[i][i]) ;
        }

        return x ;
    }
    private double[] forwardSubsitution(double[][] l ,double [] b){
        double[] x = new double[n];
        for(int i=0 ; i < n ; i++){
            x[i] = b[i] ;
            for(int j=0 ; j<i ; j++){
                x[i] -= l[i][j]*x[j] ;
            }
            x[i] =round(x[i]/l[i][i] ) ;
        }

        return x ;
    }

    public void print(){
        for(int i=0 ; i< system.length ; i++){
            for(int j=0 ; j< system[0].length ; j++){
                System.out.print(system[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
        System.out.println("");

    }
    double round(double x){
        if(Math.abs(x)< es){
            return 0 ;
        }
        int shift = (int)Math.ceil(Math.log10(x)) - figures;
        x = x/ Math.pow(10, shift) ;
        x = Math.round(x) ;
        x = x*  Math.pow(10, shift) ;
        x = Double.parseDouble( String.format("%.10g%n",x) ) ;
        return x ;
    }

    public LinkedList<double[][]> getSteps() {
        return steps;
    }
    public boolean getHasSolution()
    {
        return hasSolution;
    }

}
