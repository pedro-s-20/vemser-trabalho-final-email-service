package br.com.dbc.vemser.emailservice.service;

import br.com.dbc.vemser.emailservice.dto.AgendamentoEmailDTO;
import br.com.dbc.vemser.emailservice.dto.SolicitacaoEmailDTO;
import br.com.dbc.vemser.emailservice.dto.TipoEmail;
import br.com.dbc.vemser.emailservice.dto.UsuarioEmailDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@Component
@RequiredArgsConstructor
public class EmailService {

    private final Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

    public MimeMessageHelper buildEmail(String email, TipoEmail tipoEmail) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(tipoEmail.getAssunto());
        return mimeMessageHelper;
    }

    public String getUsuarioTemplate(UsuarioEmailDTO usuarioEmailDTO) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("usuario", usuarioEmailDTO);
        Template template;

        switch (usuarioEmailDTO.getTipoEmail()) {
            case USUARIO_CADASTRO -> template = fmConfiguration.getTemplate("usuario-cadastro.ftl");
            case USUARIO_SENHA_REDEFINIDA -> template = fmConfiguration.getTemplate("usuario-senha-redefinida.ftl");
            default -> template = fmConfiguration.getTemplate("usuario-cadastro.ftl");
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getUsuarioTemplateRedefinicao(UsuarioEmailDTO usuario) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("usuario", usuario);
        Template template = fmConfiguration.getTemplate("usuario-redefinir-senha.ftl");

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getAgendamentoTemplate(AgendamentoEmailDTO agendamento) throws IOException, TemplateException {
        Template template;
        Map<String, Object> dados = new HashMap<>();
        dados.put("agendamento", agendamento);
        dados.put("email", from);

        switch (agendamento.getTipoEmail()) {
            case AGENDAMENTO_CRIADO_CLIENTE -> template = fmConfiguration.getTemplate("agendamento-criado-cliente.ftl");

            case AGENDAMENTO_CRIADO_MEDICO -> template = fmConfiguration.getTemplate("agendamento-criado-medico.ftl");

            case AGENDAMENTO_EDITADO_CLIENTE ->
                    template = fmConfiguration.getTemplate("agendamento-editado-cliente.ftl");

            case AGENDAMENTO_EDITADO_MEDICO -> template = fmConfiguration.getTemplate("agendamento-editado-medico.ftl");

            case AGENDAMENTO_CANCELADO_CLIENTE ->
                    template = fmConfiguration.getTemplate("agendamento-cancelado-cliente.ftl");

            case AGENDAMENTO_CANCELADO_MEDICO ->
                    template = fmConfiguration.getTemplate("agendamento-cancelado-medico.ftl");
            default -> template = fmConfiguration.getTemplate("agendamento-criado-cliente.ftl");
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getClienteTemplateSolicitacao(SolicitacaoEmailDTO solicitacaoEmailDTO) throws IOException, TemplateException {
        Template template;
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("solicitacao", solicitacaoEmailDTO);

        switch (solicitacaoEmailDTO.getTipoEmail()) {
            case SOLICITACAO_CRIADA -> template = fmConfiguration.getTemplate("agendamento-solicitacao-criada.ftl");
            case SOLICITACAO_RECUSADA -> template = fmConfiguration.getTemplate("agendamento-solicitacao-recusada.ftl");
            default -> template = fmConfiguration.getTemplate("agendamento-solicitacao-criada.ftl");
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    // USUÁRIO
    public void sendEmailUsuario(UsuarioEmailDTO usuarioEmailDTO) throws MessagingException, TemplateException, IOException {

        MimeMessageHelper mimeMessageHelper = buildEmail(usuarioEmailDTO.getEmailUsuario(), usuarioEmailDTO.getTipoEmail());
        if (usuarioEmailDTO.getTipoEmail() == TipoEmail.USUARIO_REDEFINIR_SENHA) {
            mimeMessageHelper.setText(getUsuarioTemplateRedefinicao(usuarioEmailDTO), true);
        } else {
            mimeMessageHelper.setText(getUsuarioTemplate(usuarioEmailDTO), true);
        }

        emailSender.send(mimeMessageHelper.getMimeMessage());
    }

    // AGENDAMENTO
    public void sendEmailAgendamento(String email, AgendamentoEmailDTO agendamentoEmailDTO) throws MessagingException, TemplateException, IOException {
        MimeMessageHelper mimeMessageHelper = buildEmail(email, agendamentoEmailDTO.getTipoEmail());
        mimeMessageHelper.setText(getAgendamentoTemplate(agendamentoEmailDTO), true);

        emailSender.send(mimeMessageHelper.getMimeMessage());
    }

    // CLIENTE
    public void sendEmailCliente(String email, SolicitacaoEmailDTO solicitacaoEmailDTO) throws MessagingException, TemplateException, IOException {
        MimeMessageHelper mimeMessageHelper = buildEmail(email, solicitacaoEmailDTO.getTipoEmail());
        mimeMessageHelper.setText(getClienteTemplateSolicitacao(solicitacaoEmailDTO), true);

        emailSender.send(mimeMessageHelper.getMimeMessage());
    }


}