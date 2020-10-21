import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomClassLoad extends ClassLoader {
    public static void main(String[] args) {
        try {
            Class<?> clazz = new CustomClassLoad().findClass("Hello");
            Object object = clazz.newInstance();
            Method method = clazz.getMethod("hello");
            method.invoke(object);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name){
        //"/Users/zhouguodong/Downloads/2020-10-17/Hello.xlass"
        String path = this.getClass().getResource("/Hello.xlass").getPath();
        File file =new File(path);
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        try {
            new FileInputStream(file).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0;i<bytes.length;i++){
            bytes[i]= (byte) (255-bytes[i]);
        }
        return defineClass(name,bytes,0,bytes.length);
    }

}
