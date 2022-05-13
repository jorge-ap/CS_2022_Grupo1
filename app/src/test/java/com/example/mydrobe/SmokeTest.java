package com.example.mydrobe;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.runner.RunWith;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


@RunWith(MockitoJUnitRunner.class)
public class SmokeTest {

    @Spy
    private ArrayList<String> spyUserNormalSentencePool;
    @Spy
    private ArrayList<String> spyMainNormalPool;
    @Mock
    private Usuario usuarioMock;
    @Mock
    private MainActivity mainMock;


    @Before
    public void setUp(){
        ArrayList<String> list = new ArrayList<>();
        spyUserNormalSentencePool = spy(list);
        spyMainNormalPool = spy(list);


        for (int i = 0; i < 5; i++) {
            spyUserNormalSentencePool.add("Bienvenido, esta es la frase " + i);
            spyMainNormalPool.add("Mensaje de ánimo nº " + i + 1);
        }

        spyUserNormalSentencePool.add("¡Elige a Bulbasur!");
        spyUserNormalSentencePool.add("¡El mejor pokemon es Pikachu!");
        spyUserNormalSentencePool.add("¡A mi Pokemon no me gusta!");
        spyUserNormalSentencePool.add("Digimon es genial");

        usuarioMock = mock(Usuario.class);
        mainMock = mock(MainActivity.class);
    }

    @Test
    public void crearFraseTest(){
        when(usuarioMock.pago(anyInt())).thenReturn(true);
        when(usuarioMock.getNormalSentencePool()).thenReturn(spyUserNormalSentencePool);
        doAnswer(invocation -> {
            String frase = (String) invocation.getArgument(1);

            spyUserNormalSentencePool.add(frase);
            return null;
        }).when(usuarioMock).anadirFrase(any(), anyString());

        int longitudLista = usuarioMock.getNormalSentencePool().size();
        String nuevaFrase = "¡Esta frase es nueva para el test!";

        if (usuarioMock.pago(50)){
            usuarioMock.anadirFrase(usuarioMock.getNormalSentencePool(), nuevaFrase);
        }

        assertEquals(longitudLista + 1, usuarioMock.getNormalSentencePool().size());
        verify(usuarioMock).pago(50);
        verify(usuarioMock, times(3)).anadirFrase(usuarioMock.getNormalSentencePool(), nuevaFrase);
    }

    @Test
    public void comprarFraseTest(){
        when(usuarioMock.pago(anyInt())).thenReturn(true);
        when(usuarioMock.getNormalSentencePool()).thenReturn(spyUserNormalSentencePool);
        when(mainMock.getPoolNormalSentences()).thenReturn(spyMainNormalPool);


        when(usuarioMock.yaEstaFrase(mainMock.getPoolNormalSentences(), usuarioMock.getNormalSentencePool())).thenReturn("Mensaje de ánimo nº 3");

        doAnswer(invocation -> {
            String frase = (String) invocation.getArgument(1);

            spyUserNormalSentencePool.add(frase);
            return null;
        }).when(usuarioMock).anadirFrase(any(), anyString());

        String frase = "";

        if (usuarioMock.pago(25)){
            frase = usuarioMock.yaEstaFrase(mainMock.getPoolNormalSentences(), usuarioMock.getNormalSentencePool());
            usuarioMock.anadirFrase(usuarioMock.getNormalSentencePool(), frase);
        }


        assertTrue(usuarioMock.getNormalSentencePool().contains(frase));

    }

}

