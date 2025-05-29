/*
* File: YachtKoltseg.java
* Author: Berta Péter
* Copyright: 2025, Berta Péter
* Group: Szoft II-2-N
* Date: 2025-05-29
* Github: https://github.com/bp-ter/
* Licenc: MIT
*/

package com.example;

import java.time.LocalDate;

public class YachtKoltseg {
    private int id;
    private String yachtname;
    private LocalDate datum;
    private String kategoria;
    private double osszeg;
    private String megjegyzes;

    public YachtKoltseg(int id, String yachtname, LocalDate datum, String kategoria, double osszeg, String megjegyzes) {
        this.id = id;
        this.yachtname = yachtname;
        this.datum = datum;
        this.kategoria = kategoria;
        this.osszeg = osszeg;
        this.megjegyzes = megjegyzes;
    }

    public int getId() { return id; }
    public String getYachtname() { return yachtname; }
    public LocalDate getDatum() { return datum; }
    public String getKategoria() { return kategoria; }
    public double getOsszeg() { return osszeg; }
    public String getMegjegyzes() { return megjegyzes; }

}