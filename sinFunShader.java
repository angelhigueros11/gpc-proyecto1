public class sinFunShader extends Shader{
    
    public sinFunShader(double u, double v, double w, double[] tA, double[] tB, double[] tC, double[] triangleNormal, Renderer r, Point3[] normals) {
        super(u, v, w, tA, tB, tC, triangleNormal, r,normals);
    }
    public sinFunShader(){
        super();
    }
    
    @Override
    public double[] getColor(){

        super.g /= 255.0;

    
        if(super.renderer.active_texture != null){
            double tU = super.u * super.tA[0] + super.v * super.tB[0] + super.w * super.tC[0];
            double tV = super.u * super.tA[1] + super.v * super.tB[1] + super.w * super.tC[1];

    
            double[] color = super.renderer.active_texture.getColor(tU, tV);
            if(color == null)
                color = new double[]{0.0,0.0,0.0};
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
