public class Utility {

    // Retourne un nombre aléatoire entre min et max (exclu)
    public static double getRandomInRange(double min, double max) {
        return min + Math.random() * (max - min);
    }

    // Retourne un nombre aléatoire entier entre min et max (inclus)
    public static int getRandomInRange(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }
}
