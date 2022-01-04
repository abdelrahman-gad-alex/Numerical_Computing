package com.example.system.of.linear.equations.SystemModels;


import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class Gauss implements ISystem{
    public double[][] system ;
    double[] solution ;
    int n ;
    double es = 1e-10 ;
    int figures ;
    boolean hasSolution ;
    boolean infinite = false ;
    LinkedList<double[][]> steps ;
    double time ;

    public boolean getHasSolution()
    {
        return hasSolution;
    }
    public boolean isInfinite(){
        return infinite;
    }
    public void setSystem(double[][] A, int f){
        figures = f ;
        system = new double[A.length][A.length+1] ;
        n = system.length ;
        for(int i=0 ; i<n ; i++){
            for(int j=0 ; j<n+1 ; j++){
                system[i][j] = round(A[i][j]) ;
            }
        }
    }

    public double[] solve(){
        long start = System.nanoTime();


        eliminate();
        subsitution();


        long endd = System.nanoTime();
        time = (endd-start) / 1e6 ;
        System.out.println("time");
        System.out.println(time);
        return solution ;

    }
    public double getTime(){
        return time ;
    }

    public void eliminate(){
        steps = new LinkedList<>();
        steps.add(copy(system)) ;
        for(int i=0 ; i< n ; i++){

            for(int k=i ; k< n ; k++){
                scaling(k);
            }
            pivoting(i,i);


            int i2 = i ;
            while (Math.abs(system[i][i2]) < es){
                if(i2==n){
                    check();
                    return;
                }
                i2 ++ ;
            }

            System.out.println("after pivoting");
            if(i != 0){
                steps.add(copy(system)) ;
                print();
            }

            for(int k=i+1 ; k<n ; k++){
                if(Math.abs(system[k][i2]) < es){
                    continue;
                }
                double f = round(system[k][i2]/ system[i][i2]) ;
                system[k][i2] = 0 ;
                for(int j=i2+1; j< n+1 ; j++){
                    system[k][j] =round(system[k][j]-f*system[i][j]) ;

                }
            }
        }
        check();

    }

    void check(){
        for (int i=0 ; i<n ; i++){
            if(Math.abs(system[i][i]) < es){
                int j = i ;
                while (Math.abs(system[i][j]) < es){
                    if(j==n){
                        hasSolution = true ;
                        infinite = true ;
                        return;
                    }
                    j ++ ;
                }
                // i, j is the last pivot
                for(int i2= i+1 ; i2<n ; i2++){
                    if(Math.abs(system[i2][n]) > es){
                        hasSolution = false ;
                        infinite = false ;
                        return;
                    }
                }
                hasSolution = true ;
                infinite = true ;
                return;

            }
        }
        hasSolution = true ;
        infinite = false ;
    }

    public LinkedList<double[][]> getSteps() {
        return steps;
    }

    private void pivoting(int i2,int j){
        int maxI = 0 ;
        double max = 0 ;
        for(int i=i2 ; i<n ; i++){
            if(Math.abs(system[i][j]) > Math.abs(max)) {
                max = system[i][j];
                maxI = i;
            }
        }
        swap(maxI, j);
    }
    private void swap(int first, int second){
        for(int i=0 ; i<n+1 ; i++){
            double s = system[first][i] ;
            system[first][i] = system[second][i] ;
            system[second][i] = s;
        }
    }
    private void scaling(int i){
        double max = 0;
        for(int j=0 ; j<n ; j++){
            if(Math.abs(system[i][j]) > Math.abs(max)) {
                max = system[i][j];
            }
        }
        for(int j=0 ; j<n+1 ; j++){

            system[i][j] = round(system[i][j]/max);
            System.out.println(system[i][j]);
        }
    }

    private void subsitution(){
        solution = new double[n];
        for(int i=n-1 ; i>= 0 ; i--){
            solution[i] = system[i][n] ;
            for(int j=i+1 ; j<n ; j++){
                solution[i] -= round(system[i][j]*solution[j]) ;
            }
            solution[i] = round(solution[i] / system[i][i] );
        }

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

    double[][] copy(double[][] matrix){
        double[][] n = new double[matrix.length][matrix[0].length] ;
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++)
                n[i][j]=matrix[i][j];
        }
        return n ;
    }

}
