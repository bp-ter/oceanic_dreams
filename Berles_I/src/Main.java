/*
* File: Main.java
* Author: Berta Péter
* Copyright: 2025, Berta Péter
* Group: Szoft II-2-N
* Date: 2025-05-29
* Github: https://github.com/bp-ter/
* Licenc: MIT
*/

import java.io.*;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Berles> berlesek = beolvasBerlesek("Berles_I/data/yacht_berlesek_2024.csv");

        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.print("Adjon meg egy hónapot (1-12): ");
        int honap = sc.nextInt();

        long haviBevetel = haviBevetel(berlesek, honap);
        long evesBevetel = berlesek.stream().mapToLong(Berles::getTotalPrice).sum();
        Berles maxBerles = berlesek.stream().max(Comparator.comparingLong(Berles::getTotalPrice)).orElse(null);
        long yachtokSzama = berlesek.stream().map(b -> b.yachtid).distinct().count();

        Map<String, Long> gyakorisag = berlesek.stream()
                .collect(Collectors.groupingBy(b -> b.name, Collectors.counting()));
        Map.Entry<String, Long> leggyakoribb = Collections.max(gyakorisag.entrySet(), Map.Entry.comparingByValue());

        double atlagNap = berlesek.stream().mapToLong(Berles::getNapokSzama).average().orElse(0);

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(' ');
        dfs.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,###", dfs);
        DecimalFormat formatterDecimal = new DecimalFormat("#,##0.00", dfs);

        System.out.println("A(z) " + honap + ". hónap bevétele: " + formatter.format(haviBevetel) + " euró");

        System.out.println("A teljes 2024-es éves bevétel: " + formatter.format(evesBevetel) + " euró");

        if (maxBerles != null) {
            System.out.println("A legdrágább bérlés az " + maxBerles.name + " yacht volt, teljes ár: " + formatter.format(maxBerles.getTotalPrice()) + " euró");
        }

        System.out.println("Összesen " + yachtokSzama + " különböző yachtot béreltek ki.");

        System.out.println("A legtöbbször bérelt yacht: " + leggyakoribb.getKey() + " (" + leggyakoribb.getValue() + " bérlés)");

        System.out.println("Átlagos bérlési időtartam: " + formatterDecimal.format(atlagNap) + " nap");

        sc.close();
    }

    public static List<Berles> beolvasBerlesek(String fajlnev) {
        List<Berles> lista = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fajlnev))) {
            br.readLine(); 
            String sor;
            while ((sor = br.readLine()) != null) {
                String[] t = sor.split(";");
                int uid = Integer.parseInt(t[0]);
                int yachtid = Integer.parseInt(t[1]);
                LocalDate start = LocalDate.parse(t[2], formatter);
                LocalDate end = LocalDate.parse(t[3], formatter);
                int price = Integer.parseInt(t[4]);
                String name = t[5];
                lista.add(new Berles(uid, yachtid, start, end, price, name));
            }
        } catch (IOException e) {
            System.err.println("Hiba a fájl olvasásakor: " + e.getMessage());
        }
        return lista;
    }

    public static long haviBevetel(List<Berles> berlesek, int honap) {
        return berlesek.stream()
                .filter(b -> b.startDate.getMonthValue() <= honap && b.endDate.getMonthValue() >= honap)
                .mapToLong(Berles::getTotalPrice)
                .sum();
    }
}
