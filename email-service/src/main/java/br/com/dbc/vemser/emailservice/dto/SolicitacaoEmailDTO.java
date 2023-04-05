package br.com.dbc.vemser.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoEmailDTO {
    private String idSoliciatacao;
    private String nomeCliente;
    private String emailCliente;
    private TipoEmail tipoEmail;

}
