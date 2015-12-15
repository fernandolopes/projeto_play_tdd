package controllers;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;


/**
 * Created by fernando on 11/12/15.
 */
public class UsuariosTest {

    @Test
    public void testandoApi(){
        Result result = new Application().index();
        assertEquals(OK,result.status());
        assertEquals("application/json",result.contentType());
        assertEquals("utf-8", result.charset());
        assertTrue(contentAsString(result).contains("welcome"));
    }

    @Test
    public void testandoFalhaNaRota(){
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            RequestBuilder request = new RequestBuilder()
                    .method(GET)
                    .uri("/api/usuarios/not_found");

            Result result = route(request);
            assertEquals(NOT_FOUND, result.status());
        });
    }

    @Test
    public void testandoUmSucessoNaRota(){
        running(fakeApplication(inMemoryDatabase("test")), () ->{
            RequestBuilder request = new RequestBuilder()
                    .method(GET)
                    .uri("/api/usuarios");
            Result result = route(request);
            assertEquals(OK, result.status());
        });
    }

    @Test
    public void cadastrandoUsuario(){
        running(fakeApplication(inMemoryDatabase("test")), () ->{

            ObjectNode json = Json.newObject();
            json.put("username","Fernando");
            json.put("email","fernando@edu.com.br");
            json.put("password","qwe123*");

            RequestBuilder request = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json)
                    .uri("/api/usuarios");
            Result result = route(request);
            assertEquals(CREATED, result.status());
            assertTrue(contentAsString(result).contains("Usuário salvo com sucesso"));
        });
    }

    @Test
    public void erroNoCadastroUsuarioCamposNulos(){
        running(fakeApplication(inMemoryDatabase("test")), () ->{

            ObjectNode json = Json.newObject();

            RequestBuilder request = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json)
                    .uri("/api/usuarios");
            Result result = route(request);
            assertEquals(BAD_REQUEST, result.status());
            assertEquals(contentAsString(result), "{\"erro\":{\"password\":[\"Este campo é obrigatório\"],\"username\":[\"Este campo é obrigatório\"]}}");

        });
    }

    @Test
    public void testarDuplicidadeEmail(){
        running(fakeApplication(inMemoryDatabase("test")), () ->{

            ObjectNode json = Json.newObject();
            json.put("username","Fernando");
            json.put("email","fernandolopes@meu.com.br");
            json.put("password","qwe123*");

            RequestBuilder request = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json)
                    .uri("/api/usuarios");
            Result result = route(request);
            assertEquals(CREATED, result.status());
            assertTrue(contentAsString(result).contains("Usuário salvo com sucesso"));

            ObjectNode json2 = Json.newObject();
            json2.put("username","Fernando Lopes");
            json2.put("email","fernandolopes@meu.com.br");
            json2.put("password","123456");

            RequestBuilder request2 = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json2)
                    .uri("/api/usuarios");
            Result result2 = route(request2);
            assertEquals(BAD_REQUEST, result2.status());
            assertEquals(contentAsString(result2), "{\"erro\":{\"email\":[\"E-mail já cadastrado\"]}}");
        });
    }

    @Test
    public void listaDeUsuarios(){
        running(fakeApplication(inMemoryDatabase("test")), () ->{

            Date currentyDate = new Date();

            ObjectNode json = Json.newObject();
            json.put("username","Fernando");
            json.put("email","fernandolopes@meu.com.br");
            json.put("password","qwe123*");
            json.put("dataCadastro",currentyDate.getTime());

            RequestBuilder request = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json)
                    .uri("/api/usuarios");
            Result result = route(request);
            assertEquals(CREATED, result.status());
            assertTrue(contentAsString(result).contains("Usuário salvo com sucesso"));


            ObjectNode json2 = Json.newObject();
            json2.put("username","Fernando Lopes");
            json2.put("email","nando@mail.com.br");
            json2.put("password","123456");
            json2.put("dataCadastro",currentyDate.getTime());

            RequestBuilder request2 = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json2)
                    .uri("/api/usuarios");
            Result result2 = route(request2);
            assertEquals(CREATED, result2.status());
            assertTrue(contentAsString(result2).contains("Usuário salvo com sucesso"));

            RequestBuilder request3 = new RequestBuilder()
                    .method(GET)
                    .uri("/api/usuarios");
            Result result3 = route(request3);
            assertEquals(OK,result3.status());
            assertEquals(contentAsString(result3), "[{\"id\":1,\"username\":\"Fernando\",\"email\":\"fernandolopes@meu.com.br\",\"dataCadastro\":"+currentyDate.getTime()+",\"dataAtualizacao\":null,\"isCliente\":true},{\"id\":2,\"username\":\"Fernando Lopes\",\"email\":\"nando@mail.com.br\",\"dataCadastro\":"+currentyDate.getTime()+",\"dataAtualizacao\":null,\"isCliente\":true}]");
        });
    }

    @Test
    public void testarAtualizarUsuario(){
        running(fakeApplication(inMemoryDatabase("test")), () ->{

            Date currentyDate = new Date();

            ObjectNode json = Json.newObject();
            json.put("id",1L);
            json.put("username","Fernando");
            json.put("email","fernandolopes@meu.com.br");
            json.put("password","qwe123*");
            json.put("dataCadastro",currentyDate.getTime());

            RequestBuilder request = new RequestBuilder()
                    .method(POST)
                    .bodyJson(json)
                    .uri("/api/usuarios");
            Result result = route(request);
            assertEquals(CREATED, result.status());
            assertTrue(contentAsString(result).contains("Usuário salvo com sucesso"));

            ObjectNode json2 = Json.newObject();
            json2.put("id",1L);
            json2.put("username","Fernando Lopes");
            json2.put("email","fernandolopes@meu.com.br");
            json2.put("password","123456");
            json2.put("dataAtualizacao",currentyDate.getTime());
            json2.put("dataCadastro",currentyDate.getTime());

            RequestBuilder request2 = new RequestBuilder()
                    .method(PUT)
                    .bodyJson(json2)
                    .uri("/api/usuarios/"+1L);
            Result result2 = route(request2);
            assertEquals(OK, result2.status());
            assertTrue(contentAsString(result2).contains("Atualizado com sucesso"));

            RequestBuilder request3 = new RequestBuilder()
                    .method(GET)
                    .uri("/api/usuarios");
            Result result3 = route(request3);
            assertEquals(OK,result3.status());
            assertEquals(contentAsString(result3), "[{\"id\":1,\"username\":\"Fernando Lopes\",\"email\":\"fernandolopes@meu.com.br\",\"dataCadastro\":"+currentyDate.getTime()+",\"dataAtualizacao\":"+currentyDate.getTime()+",\"isCliente\":true}]");

            request = new RequestBuilder()
                    .method(DELETE)
                    .uri("/api/usuarios/"+1L);
            result = route(request);
            assertEquals(OK, result.status());
            assertTrue(contentAsString(result).contains("sucesso"));

            result3 = route(request3);
            assertEquals(OK,result3.status());
            assertTrue(contentAsString(result3).contains("[]"));

        });
    }
}
