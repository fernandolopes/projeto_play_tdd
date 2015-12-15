package models;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.Date;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

/**
 * Created by fernando on 10/12/15.
 */
public class UsuarioTest  {

    private Usuario u;
    private Date dataAtual;

    @Before
    public void setup(){
        u = new Usuario();
        u.setId(1L);
        u.setUsername("Fernando");
        u.setPassword("qwe123*");
        dataAtual = new Date();
        u.setDataCadastro(dataAtual);
        u.setEmail("fernando@vrgerenciadora.com.br");
        u.isCliente = true;
    }

    @Test
    public void testadoClasse(){
        assertEquals(u.getId(), new Long(1L));
        assertEquals(u.getUsername(),"Fernando");
        assertEquals(u.getPassword(), "qwe123*");
        assertEquals(u.getDataCadastro(),dataAtual);
        assertEquals(u.getEmail(),"fernando@vrgerenciadora.com.br");
    }

    @Test(expected = PersistenceException.class)
    public void nomeUsuarioDeveDarErro(){
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                u.setUsername("Francisco José Maximus Germiniano Mathaus de Oliveira Batista Queiroz Lopes da Silva Junior");
                u.save();
            }
        });
    }

    @Test
    public void deveSalvarUsuario(){
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            @Override
            public void run() {
                u.setUsername("fernando");
                u.setPassword("qwe123*");
                u.setDataCadastro(new Date());
                u.setEmail("fernando@vrgerenciadora.com.br");
                u.save();

                assertNotNull(u.getId());
            }
        });
    }

    @Test
    public void verificaTipodeUsuario(){
        assertEquals(u.isCliente, true);
    }

    @Test
    public void naoDeveriaSerCliente(){
        assertNotEquals(u.isCliente, false);
    }

}
