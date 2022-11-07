// Angel Higueros - 20460
// Proyecto 1 

public class CustomMath {

    static final int N = 4;

    public CustomMath(){};
    
    public double[] baryCoords(Point3 A,Point3 B,Point3 C,Point3 P){
        double areaPBC = (B.y - C.y) * (P.x - C.x) + (C.x - B.x) * (P.y - C.y);
        double areaPAC = (C.y - A.y) * (P.x - C.x) + (A.x - C.x) * (P.y - C.y);
        double areaABC = (B.y - C.y) * (A.x - C.x) + (C.x - B.x) * (A.y - C.y);

        try{
            double u = areaPBC / areaABC;
            double v = areaPAC / areaABC;
            double w = 1-u-v;
            return new double[]{u, v, w};
        }catch(Exception e){
            return new double[]{-1,-1,-1};
        }
    
    
    }

    public double[][] matrixMultiplication(double[][] A, double[][] B){
        double[][] C = new double[A.length][B[0].length];
        for(int i=0; i<A.length; i++){
            for(int j=0; j<B[0].length; j++){
                C[i][j] = 0.0;
                for(int k=0; k<A[0].length; k++){
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }


    public double[][] matrixSubtraction(double[][] A, double[][] B){
        double[][] C = new double[A.length][A[0].length];
        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    public double[] cross_product(double[] A, double[] B){
        double[] C = new double[3];
        C[0] = A[1]*B[2] - A[2]*B[1];
        C[1] = A[2]*B[0] - A[0]*B[2];
        C[2] = A[0]*B[1] - A[1]*B[0];
        return C;
    }

    public double[] negation(double[] A){
        double[] C = new double[3];
        C[0] = -A[0];
        C[1] = -A[1];
        C[2] = -A[2];
        return C;
    }

    // function to calculate the dot product
    public double dot_product(double[] A, double[] B){
        double product = 0;
        for(int i=0; i<A.length; i++){
            product += A[i]*B[i];
        }
        return product;
    }

    public double[][] invert(double a[][]) 
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) 
            b[i][i] = 1;
 
        gaussian(a, index);
 
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                    	    -= a[index[j]][i]*b[index[i]][k];
 
        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }
 
 
    private static void gaussian(double a[][], int index[]) 
    {
        int n = index.length;
        double c[] = new double[n];
 
        for (int i=0; i<n; ++i) 
            index[i] = i;
 
        for (int i=0; i<n; ++i) 
        {
            double c1 = 0;
            for (int j=0; j<n; ++j) 
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
 
        int k = 0;
        for (int j=0; j<n-1; ++j) 
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i) 
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) 
                {
                    pi1 = pi0;
                    k = i;
                }
            }
 
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) 	
            {
                double pj = a[index[i]][j]/a[index[j]][j];
 
                a[index[i]][j] = pj;
 
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }



    public double[] multiplyMatrixVector(double[][] A, double[] B){
        double[] C = new double[A.length];
        for(int i=0; i<A.length; i++){
            C[i] = 0.0;
            for(int j=0; j<A[0].length; j++){
                C[i] += A[i][j] * B[j];
            }
        }
        return C;
    }

    public void printMatrix(double[][] A){
        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
    }

}
