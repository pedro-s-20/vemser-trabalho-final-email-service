package br.com.dbc.vemser.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEmailDTO {

    private String nomeUsuario;
    private String emailUsuario;
    private String codigoRecuperacao;
    private TipoEmail tipoEmail;

}
