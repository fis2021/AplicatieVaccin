package org.loose.fis.av.model;

public class ModelTable {
    String nume,locuri;

    public ModelTable(String nume,String locuri){
        this.nume = nume;
        this.locuri = locuri;
    }

    public String getNume(){
        return nume;
    }

    public String getLocuri(){
        return locuri;
    }
}
