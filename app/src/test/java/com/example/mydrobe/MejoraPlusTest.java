package com.example.mydrobe;


import org.junit.Assert;
import org.junit.Test;

//Parte del TDD
public class MejoraPlusTest {
    @Test
    public void MejoraPlusTest(){
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        int num1 = usuario1.getValorClick();
        int num2 = usuario2.getValorClick();
        Assert.assertEquals(num1, num2,1);
        System.out.println(usuario1.getValorClick());
        System.out.println(usuario1.getValorClick());
        usuario1.aplicarMejoraClicksPlus();
        usuario2.aplicarMejoraClicksPlus();
        Assert.assertEquals(usuario1.getValorClick(), usuario2.getValorClick(),15);
        System.out.println(usuario1.getValorClick());
        System.out.println(usuario1.getValorClick());

    }
}
