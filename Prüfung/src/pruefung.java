public class pruefung {
    public static void main(String[] args) {
        System.out.println("alle quersummen = 15 :\n");
        for (int i = 0; i < 1000; i++) {                        // aufgabe 2
            if (quersumme(i) == 15) {
                System.out.println(i);
            }
        }
        System.out.println("\n\n\n vielfaches von sieben :\n");
        for (int i = 0; i < 1000; i++) {                        // aufgabe 3
            if (quersumme(i) % 7 == 0) {
                System.out.println(i);
            }
        }
        System.out.println("\n\n\nHaeufigkeitsverteilung 1-1000 :");
        int most = 0;
        int num = 0;
        for (int i = 0; i < 27; i++) {
            int count = 0;
            for (int j = 0; j < 1000; j++) {
                if (quersumme(j) == i) {
                    count++;
                }
            }
            if (count > most) {
                most = count;
                num = i;
            }
        }
        System.out.println(String.format("Die quersumme %s kommt mit %s mal am haeufigsten vor.",num,most));

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
