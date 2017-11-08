package services;

/**
 * Created by Piotr Krzyminski on 05.11.2017
 * Model komends sprawdzający czy podany ciąg instrukcji nie powoduje błędów i jest dozwolony
 */
public class Commands {
    private int towerCount; /* ilość wieży */

    /**
     * Inicjalizuje wartość ilości wieży
     * @param towers ilość wieży
     */
    public Commands(int towers) { towerCount = towers; }

    /**
     * Wykonuje sprawdzenie instrukcji pod kątem błędów
     * Błędne są:
     * 1. instrukcja musi mieć format: liczba - spacja - liczba;
     * 2. numery wież nie mogą wskazywać na indeksy tablicy wież, które nie istnieją
     * @param line Pojedyncza linia z instrukcji
     * @return zwraca tablicę dwóch cyfr. są to indeksy wież na których należy wykonać przemiesienie
     * @throws InvalidCommandFormatException zwraca wyjątek jeżeli instrukcje nie spełniają kryteriów
     */
    public int[] parse(String line) throws InvalidCommandFormatException {
        int[] command = new int[2];

        /* Jeżeli linia ma niepoprawny format rzuca wyjątek */
        if(!line.matches("\\d+\\s\\d+$")) {
            throw new InvalidCommandFormatException("Instrukcja ma niepoprawny format.");
        }

        /* rozdziela linię na tablicę dwóch wartości */
        String[] commandString = line.split("\\s");

        /* konwertuje wartości tekstowe na liczby całkowite */
        int from = Integer.valueOf(commandString[0]) - 1;
        int to = Integer.valueOf(commandString[1]) - 1;

        /* jeżeli indeksy wież wskazują na nieistniejące miejsca w tablicy rzuca wyjątek */
        if((from<0 || from>towerCount) && (to<0 || to>towerCount)) {
            throw new InvalidCommandFormatException("Niepoprawne numery wież");
        }

        command[0] = from;
        command[1] = to;

        return command;
    }
}
