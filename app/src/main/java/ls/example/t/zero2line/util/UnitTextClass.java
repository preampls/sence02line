package ls.example.t.zero2line.util;

public class UnitTextClass {

    private static UnitTextClass instance;

    public UnitTextClass (){

    }

    public static UnitTextClass getInstance(){
        if (instance==null){
            synchronized (UnitTextClass.class){
                if (instance==null){
                    instance=new UnitTextClass();
                }
            }
        }
        return instance;
    }


    public float UnitTestOne(){
        float index_first=1l;
        float random =(float) Math.random()*1l;
        float result = index_first / random;
        return result;
    }

}
