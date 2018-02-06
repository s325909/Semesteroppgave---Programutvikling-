public class HelloWorld {

    public static void main( String[] args ){
        int a = 7;
        int b = 5;
        int c = 6;

        boolean result = (a <= b && b == c) || c >= b;

        System.out.println(result);

        System.out.println("Hei");
    }
}
