package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import forms.validation.ValidaEmailUnico;
import lombok.Getter;
import lombok.Setter;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.libs.F;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by fernando on 10/12/15.
 */

@Entity
@Table(name="users")
public class Usuario extends Model {

    @Id
    @Getter @Setter
    private Long id;
    @Column(length = 80, nullable = false)
    @Constraints.MaxLength(80)
    @Constraints.Required
    @Getter @Setter
    private String username;
    @Column(length = 255)
    @Constraints.MaxLength(255)
    @Getter @Setter
    @JsonIgnore
    private String password;
    @Column(length = 120)
    @Constraints.MaxLength(120)
    @Constraints.Email
    @Getter @Setter
    private String email;
    @Column(name="data_cadastro")
    @Getter @Setter
    private Date dataCadastro;
    @Column(name="data_atualizacao")
    @Getter @Setter
    private Date dataAtualizacao;
    @Column(name="is_cliente")
    @Constraints.Required()
    public Boolean isCliente;

    public static boolean isEmailUnique(String email){
        if(email == null)
            return true;
        int count = find.where().eq("email",email).findRowCount();
        return count > 0 ? false : true;
    }

    public static final Finder<Long, Usuario> find = new Finder<>(Usuario.class);
}
