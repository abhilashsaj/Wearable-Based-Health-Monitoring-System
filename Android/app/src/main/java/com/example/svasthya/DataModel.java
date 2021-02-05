package com.example.svasthya;

public class DataModel {
    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getBronchi() {
        return bronchi;
    }

    public void setBronchi(String bronchi) {
        this.bronchi = bronchi;
    }

    public String getHypoxemia() {
        return hypoxemia;
    }

    public void setHypoxemia(String hypoxemia) {
        this.hypoxemia = hypoxemia;
    }

    public String getAsthma() {
        return asthma;
    }

    public void setAsthma(String asthma) {
        this.asthma = asthma;
    }

    public String getChd() {
        return chd;
    }

    public void setChd(String chd) {
        this.chd = chd;
    }

    public DataModel(String diabetes, String bronchi, String hypoxemia, String asthma, String chd, String time, String date) {
        this.diabetes = diabetes;
        this.bronchi = bronchi;
        this.hypoxemia = hypoxemia;
        this.asthma = asthma;
        this.chd = chd;
        this.time = time;
        this.date = date;
    }

    private String diabetes;
    private String bronchi;
    private String hypoxemia;
    private String asthma;
    private String chd ;
    private String time;

    public String getStress() {
        return stress;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public DataModel(String diabetes, String bronchi, String hypoxemia, String asthma, String chd, String time, String stress, String date) {
        this.diabetes = diabetes;
        this.bronchi = bronchi;
        this.hypoxemia = hypoxemia;
        this.asthma = asthma;
        this.chd = chd;
        this.time = time;
        this.stress = stress;
        this.date = date;
    }

    private String stress;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
}
