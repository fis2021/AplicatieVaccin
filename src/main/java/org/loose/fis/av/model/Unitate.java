package org.loose.fis.av.model;

import org.dizitart.no2.objects.Id;
import org.loose.fis.av.model.Programat;

public class Unitate {

    private String nume;
    private String localitate;
    @Id
    private String cod_unit;
    private Programat progamati[] = new Programat[100];
    private int i = 0;

    public Unitate(String nume, String localitate,String cod){
        this.nume = nume;
        this.localitate = localitate;
        cod_unit = cod;
    }

    public Unitate(){}

    public void addProgramat(Programat prog){
        if(i < 100){
            progamati[i] = prog;
            i++;
        }
    }

    public String getNume() {
        return nume;
    }
    public Programat getProgramat(int j) {
        return progamati[j];
    }

    public String getLocalitate() {
        return localitate;
    }
    public String getCod_unit() {
        return cod_unit;
    }

    public int getContor() {
        return i;
    }
}
