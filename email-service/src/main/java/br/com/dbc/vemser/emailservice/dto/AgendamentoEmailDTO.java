package br.com.dbc.vemser.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AgendamentoEmailDTO {
    private Integer idAgendamento;
    private LocalDateTime dataHorario;
    private String nomeMedico;
    private String nomeCliente;
    private String email;
    private TipoEmail tipoEmail;

}