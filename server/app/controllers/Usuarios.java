package controllers;

import forms.validation.ValidaEmailUnico;
import models.Usuario;
import org.jboss.netty.util.internal.StringUtil;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by fernando on 11/12/15.
 */
public class Usuarios extends Controller {

    public Result index() {
        final List<Usuario> lista = Usuario.find.findList();
        return ok(Json.toJson(lista));
    }

    public Result create() {

        Form<CadastrarUsuarioForm> form = Form.form(CadastrarUsuarioForm.class).bindFromRequest();

        if(form.hasErrors()){
            return badRequest(Application.buildJsonErrorResponse("erro", form.errorsAsJson()));
        }

        CadastrarUsuarioForm newUser = form.get();

        Usuario u = new Usuario();
        u.setUsername(newUser.username);
        u.setEmail(newUser.email);
        u.setPassword(newUser.password);

        u.setDataCadastro(new Date(newUser.dataCadastro));
        u.isCliente = newUser.isCliente;
        u.save();

        return created(Application.buildJsonResponse("sucesso","Usuário salvo com sucesso"));
    }

    public Result update(Long id){

        Form<AtualizaUsuarioForm> form = Form.form(AtualizaUsuarioForm.class).bindFromRequest();
        if(form.hasErrors()){
            return badRequest(Application.buildJsonErrorResponse("erro", form.errorsAsJson()));
        }

        AtualizaUsuarioForm newUser = form.get();

        Usuario u = Usuario.find.byId(id);
        u.setUsername(newUser.username);
        u.setEmail(newUser.email);
        if(newUser != null || !newUser.password.isEmpty()) u.setPassword(newUser.password);

        u.setDataAtualizacao(new Date(newUser.dataAtualizacao));
        u.setDataCadastro(new Date(newUser.dataCadastro));
        u.isCliente = newUser.isCliente;
        u.update();

        return ok(Application.buildJsonResponse("sucesso","Atualizado com sucesso"));
    }

    public Result delete(Long id){
        try {
            Usuario user = Usuario.find.byId(id);
            user.delete();
        } catch (Exception e){
            return badRequest(Application.buildJsonErrorResponse("erro",Json.newObject().put("message",e.getMessage())));
        }
        return ok(Application.buildJsonResponse("sucesso","Removido com sucesso"));
    }

    public static class UsuarioForm {
        @Constraints.Required
        @Constraints.MaxLength(80)
        public String username;
        @Constraints.Required
        public Boolean isCliente = true;
    }

    public static class CadastrarUsuarioForm extends UsuarioForm {
        @Constraints.Required
        @Constraints.MaxLength(255)
        @Constraints.MinLength(6)
        public String password;
        @Constraints.Email
        @Constraints.MaxLength(120)
        @Constraints.ValidateWith(value=ValidaEmailUnico.class)
        public String email;
        @Constraints.Required
        public long dataCadastro = new Date().getTime();
    }

    public static class AtualizaUsuarioForm extends UsuarioForm {
        @Constraints.MaxLength(255)
        public String password;
        @Constraints.Email
        @Constraints.MaxLength(120)
        public String email;
        @Constraints.Required
        public long dataAtualizacao = new Date().getTime();
        @Constraints.Required
        public long dataCadastro = new Date().getTime();
    }



}
