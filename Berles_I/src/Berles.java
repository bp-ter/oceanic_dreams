/*
* File: Berles.java
* Author: Berta Péter
* Copyright: 2025, Berta Péter
* Group: Szoft II-2-N
* Date: 2025-05-29
* Github: https://github.com/bp-ter/
* Licenc: MIT
*/

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Berles {
    int uid;
    int yachtid;
    LocalDate startDate;
    LocalDate endDate;
    int dailyPrice;
    String name;

    public Berles(int uid, int yachtid, LocalDate startDate, LocalDate endDate, int dailyPrice, String name) {
        this.uid = uid;
        this.yachtid = yachtid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyPrice = dailyPrice;
        this.name = name;
    }

    public long getNapokSzama() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public long getTotalPrice() {
        return getNapokSzama() * dailyPrice;
    }
}
