// Angel Higueros - 20460
// Proyecto 1 

public class glowCustomShader extends Shader{
    
    public glowCustomShader(double u, double v, double w, double[] tA, double[] tB, double[] tC, double[] triangleNormal, Renderer r, Point3[] normals) {
        super(u, v, w, tA, tB, tC, triangleNormal, r,normals);
    }
    public glowCustomShader(){
        super();
    }
    
    @Override
    public double[] getColor(){

        super.r /= 255.0;
        super.g /= 255.0;
        super.b /= 255.0;

    
        if(super.renderer.active_texture != null){
            double tU = super.u * super.tA[0] + super.v * super.tB[0] + super.w * super.tC[0];
            double tV = super.u * super.tA[1] + super.v * super.tB[1] + super.w * super.tC[1];

    
            double[] color = super.renderer.active_texture.getColor(tU, tV);
            if(color == null)
                color = new double[]{1.0,1.0,1.0};
            super.r *= color[0];
            super.g *= color[1];
            super.b *= color[2];
        }
        double[] light = new double[]{ super.renderer.dirLight.x, super.renderer.dirLight.y, super.renderer.dirLight.z };
        super.triangleNormal = new double[]{
            super.nA.x * super.u + super.nB.x * super.v + super.nC.x * super.w,
            super.nA.y * super.u + super.nB.y * super.v + super.nC.y * super.w,
            super.nA.z * super.u + super.nB.z * super.v + super.nC.z * super.w
  
        };
        double intensity = math.dot_product(super.triangleNormal, math.negation(light));
        super.r *= intensity;
        super.g *= intensity;
        super.b *= intensity;

        double[] camForward = new double[]{ super.renderer.camMatrix[0][2],
            super.renderer.camMatrix[1][2],
            super.renderer.camMatrix[2][2], };

        double glowAmount = 1 - math.dot_product(super.triangleNormal, camForward);

        glowAmount = (glowAmount <= 0) ? 0 : glowAmount;

        double[] tempColor = new double[]{0,10,255};

        super.r += tempColor[0] * glowAmount;
        super.g += tempColor[1] * glowAmount;
        super.b += tempColor[2] * glowAmount;

        super.r = (super.r > 1) ? 1 : super.r;
        super.g = (super.g > 1) ? 1 : super.g;
        super.b = (super.b > 1) ? 1 : super.b;

        if(intensity>0)
            return new double[]{
                Math.sin(Math.toRadians(super.r))/2 + 0.5,
                super.g,
                Math.sin(Math.toRadians(super.b))/2 + 0.5,
            };
        else
            return new double[]{0.85, 0.85, 0.85};
    
    }
    

}
