import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestPregunta_Mock_Stub {

    @Mock
    private Pregunta preguntaMock;

    @Mock
    private Resposta respostaMock1;

    private int contadorRespostes;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        contadorRespostes = 0;

        //
        // -------- STUBS BÁSICOS -----------
        //

        when(preguntaMock.getText()).thenReturn("Pregunta stub");
        when(preguntaMock.getTipus()).thenReturn(2);
        when(preguntaMock.getOpcions())
                .thenReturn(Arrays.asList("Java", "Python", "C++"));
        when(preguntaMock.getNumOpcions()).thenReturn(3);


        //
        // -------- STUB COMPLEJO: addResposta() --------
        //
        when(preguntaMock.addResposta(any(), anyInt()))
                .thenAnswer(inv -> {
                    Resposta r = inv.getArgument(0);
                    int usuari = inv.getArgument(1);

                    // Caso: respuesta null -> aceptar
                    if (r == null) {
                        contadorRespostes++;
                        return 1;
                    }

                    // Caso: ya existe la respuesta duplicada
                    if (r == respostaMock1 && contadorRespostes > 0) {
                        return 0; // duplicada
                    }

                    // Caso: usuario id negativo -> se acepta igualmente
                    if (usuari < 0) {
                        contadorRespostes++;
                        return 1;
                    }

                    // Caso general
                    contadorRespostes++;
                    return 1;
                });

        // Para testear getNumRespostes dinámico
        when(preguntaMock.getNumRespostes()).thenAnswer(inv -> contadorRespostes);

        //
        // -------- STUB: setText() -----------
        //
        doAnswer(inv -> {
            String nuevo = inv.getArgument(0);
            when(preguntaMock.getText()).thenReturn(nuevo);
            return null;
        }).when(preguntaMock).setText(anyString());

        //
        // -------- STUB: comportamiento excepcional -----------
        //
        when(preguntaMock.getNumOpcions())
                .thenReturn(3)
                .thenThrow(new UnsupportedOperationException("Second call forbidden"));

        //
        // -------- STUB: toString() personalizado -----------
        //
        when(preguntaMock.toString()).thenReturn("Mock Pregunta: stub");
    }

    @Test
    public void testStubBasico() {
        assertThat(preguntaMock.getText(), equalTo("Pregunta stub"));
        assertThat(preguntaMock.getNumOpcions(), equalTo(3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testStubExcepcion() {
        preguntaMock.getNumOpcions(); // primera OK
        preguntaMock.getNumOpcions(); // segunda lanza excepción
    }

    @Test
    public void testStubAddResposta() {
        int r1 = preguntaMock.addResposta(respostaMock1, 1);
        int r2 = preguntaMock.addResposta(respostaMock1, 2); // duplicada

        assertThat(r1, equalTo(1));
        assertThat(r2, equalTo(0));
        assertThat(preguntaMock.getNumRespostes(), equalTo(1));
    }

    @Test
    public void testStubAddNull() {
        int r = preguntaMock.addResposta(null, 5);
        assertThat(r, equalTo(1));
        assertThat(preguntaMock.getNumRespostes(), equalTo(1));
    }

    @Test
    public void testStubAddNegativo() {
        int r = preguntaMock.addResposta(respostaMock1, -10);
        assertThat(r, equalTo(1));
    }

    @Test
    public void testStubSetText() {
        preguntaMock.setText("Nuevo Texto Stub");
        assertThat(preguntaMock.getText(), equalTo("Nuevo Texto Stub"));
    }
}
