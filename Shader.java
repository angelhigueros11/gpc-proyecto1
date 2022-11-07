// Angel Higueros - 20460
// Proyecto 1 

public abstract class Shader {
    public double u = 0.0;
    public double v = 0.0;
    public double w = 0.0;

    public double r = 0.0;
    public double g = 0.0;
    public double b = 0.0;

    public double[] tA;
    public double[] tB;
    public double[] tC;

    public Point3 nA;
    public Point3 nB;
    public Point3 nC;

    public double[] color ;

    public double[] triangleNormal;

    public Renderer renderer;
    public CustomMath math;

    public Shader (double u, double v, double w, double[] tA, double[] tB, double[] tC, double[] triangleNormal,  Renderer r,Point3[] normals){
        this.u = u;
        this.v = v;
        this.w = w;
        this.tA = tA;
        this.tB = tB;
        this.tC = tC;
        this.triangleNormal = triangleNormal;
        this.renderer = r;

        this.nA = normals[0];
        this.nB = normals[1];
        this.nC = normals[2];

        math = new CustomMath();
    }

    public void setAttributes(double u, double v, double w, double[] tA, double[] tB, double[] tC, double[] triangleNormal,  Renderer r, double[] color, Point3[] normals){
        this.u = u;
        this.v = v;
        this.w = w;
        this.tA = tA;
        this.tB = tB;
        this.tC = tC;
        this.triangleNormal = triangleNormal;
        this.renderer = r;
        this.color = color;
        this.r = (double)color[2];
        this.g = (double)color[1];
        this.b = (double)color[0];
        this.nA = normals[0];
        this.nB = normals[1];
        this.nC = normals[2];
    }

    public Shader(){
        math = new CustomMath();
    }

    // public double[] flatShader(){
    //     this.r /= 255.0;
    //     this.g /= 255.0;
    //     this.b /= 255.0;

    //     if(this.renderer.active_texture != null){
    //         double tU = this.u * tA[0] + this.v * tB[0] + this.w * tC[0];
    //         double tV = this.u * tA[1] + this.v * tB[1] + this.w * tC[1];

    //         byte[] color = this.renderer.active_texture.getColor(tU, tV);

    //         this.r *= color[0];
    //         this.g *= color[1];
    //         this.b *= color[2];
    //     }
    //     double[] light = new double[]{ this.renderer.dirLight.x, this.renderer.dirLight.y, this.renderer.dirLight.z };
    //     double intensity = math.dot_product(triangleNormal, math.negation(light));
    //     this.r *= intensity;
    //     this.g *= intensity;
    //     this.b *= intensity;

    //     if(intensity <= 0){
    //         this.r = 0;
    //         this.g = 0;
    //         this.b = 0;
    //     }

    //     return new double[]{this.r, this.g, this.b};

    // }

    public abstract double[] getColor();

//    setters
    public void setU(double u){
        this.u = u;
    }
    public void setV(double v){
        this.v = v;
    }
    public void setW(double w){
        this.w = w;
    }
    public void setR(double r){
        this.r = r;
    }
    public void setG(double g){
        this.g = g;
    }
    public void setB(double b){
        this.b = b;
    }
    public void setTA(double[] tA){
        this.tA = tA;
    }
    public void setTB(double[] tB){
        this.tB = tB;
    }
    public void setTC(double[] tC){
        this.tC = tC;
    }
    public void setTriangleNormal(double[] triangleNormal){
        this.triangleNormal = triangleNormal;
    }
    public void setRenderer(Renderer r){
        this.renderer = r;
    }

    // function to print all the attributes of the shader
    public void printAttributes(){
        System.out.println("u: " + this.u);
        System.out.println("v: " + this.v);
        System.out.println("w: " + this.w);
        System.out.println("r: " + this.r);
        System.out.println("g: " + this.g);
        System.out.println("b: " + this.b);
        System.out.println("tA: " + this.tA[0] + ", " + this.tA[1]);
        System.out.println("tB: " + this.tB[0] + ", " + this.tB[1]);
        System.out.println("tC: " + this.tC[0] + ", " + this.tC[1]);
        System.out.println("triangleNormal: " + this.triangleNormal[0] + ", " + this.triangleNormal[1] + ", " + this.triangleNormal[2]);
    }

}

