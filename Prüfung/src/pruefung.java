public class pruefung {
    public static void main(String[] args) {
        System.out.println(quersumme(42543));
    }

    static int quersumme(int number) {
        int maxExpo = 1;
        int i = 0;
        while (i < number) {
            i = (int) Math.pow(10, maxExpo);
            maxExpo++;
        }
        int quersumme = 0;
        for (int j = maxExpo; j >= 0; j--) {
            int divis = (int) (number / (Math.pow(10, j)));
            number = (int) (number % Math.pow(10, j));
            quersumme = quersumme + divis;
        }
        return quersumme;
    }
}
