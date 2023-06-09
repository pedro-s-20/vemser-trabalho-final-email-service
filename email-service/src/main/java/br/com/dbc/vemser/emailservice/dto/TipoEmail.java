package br.com.dbc.vemser.emailservice.dto;

public enum TipoEmail {

    USUARIO_CADASTRO("Cadastro no sistema"),
    USUARIO_REDEFINIR_SENHA("Redefinição de senha"),
    USUARIO_SENHA_REDEFINIDA("Senha redefinida"),
    AGENDAMENTO_CRIADO_CLIENTE("Novo agendamento"),
    AGENDAMENTO_EDITADO_CLIENTE("Agendamento alterado"),
    AGENDAMENTO_CANCELADO_CLIENTE("Agendamento removido"),
    AGENDAMENTO_CRIADO_MEDICO("Novo agendamento"),
    AGENDAMENTO_EDITADO_MEDICO("Agendamento alterado"),
    AGENDAMENTO_CANCELADO_MEDICO("Agendamento removido"),
    SOLICITACAO_RECUSADA("Solicitação recusada"),
    SOLICITACAO_CRIADA("Solicitação criada");

    private String assunto;

    TipoEmail(String assunto){
        this.assunto = assunto;
    }

    public String getAssunto(){
        return assunto;
    }

}
