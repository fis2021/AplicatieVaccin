package org.loose.fis.av.model;

import org.dizitart.no2.objects.Id;

public class Unitate {

    private String nume;
    private String localitate;
    private Programat progamati[] = new Programat[100];
    private int i = 0;

    public Unitate(String nume, String localitate, Programat prog){
        this.nume = nume;
        this.localitate = localitate;
        if(i < 100)
        {
            progamati[i] = prog;
            i++;
        }
    }


}
