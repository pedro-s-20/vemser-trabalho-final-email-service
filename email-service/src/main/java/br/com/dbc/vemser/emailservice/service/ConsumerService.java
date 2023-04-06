package br.com.dbc.vemser.emailservice.service;

import br.com.dbc.vemser.emailservice.dto.AgendamentoEmailDTO;
import br.com.dbc.vemser.emailservice.dto.SolicitacaoEmailDTO;
import br.com.dbc.vemser.emailservice.dto.UsuarioEmailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            topicPartitions = {@TopicPartition(topic = "${kafka.topic}", partitions = {"0"})},
            containerFactory = "listenerContainerFactory1"
    )
    public void consumeUsuario(@Payload String mensagem,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Long partition) throws JsonProcessingException {
        try{
            UsuarioEmailDTO usuarioEmailDTO = objectMapper.readValue(mensagem, UsuarioEmailDTO.class);

            try {
                emailService.sendEmailUsuario(usuarioEmailDTO);
            } catch (MessagingException | TemplateException | IOException e) {
                log.error("Erro ao enviar o e-mail tipo: {} e nome de usuário: {}", usuarioEmailDTO.getTipoEmail(), usuarioEmailDTO.getNomeUsuario());
            }
            log.info("#### ({}) Email do tipo: {} e nome de usuário: {} enviado com sucesso", LocalDateTime.now(), usuarioEmailDTO.getTipoEmail(), usuarioEmailDTO.getNomeUsuario());
        }catch(JsonProcessingException | NullPointerException | KafkaException e){

        }
    }

    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            topicPartitions = {@TopicPartition(topic = "${kafka.topic}", partitions = {"1"})},
            containerFactory = "listenerContainerFactory1"
    )
    public void consumeAgendamento(@Payload String mensagem,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Long partition) throws JsonProcessingException {
        try{
            AgendamentoEmailDTO agendamentoEmailDTO = objectMapper.readValue(mensagem, AgendamentoEmailDTO.class);

            try {
                emailService.sendEmailAgendamento(agendamentoEmailDTO.getEmail(), agendamentoEmailDTO);
            } catch (MessagingException | TemplateException | IOException e) {
                log.error("Erro ao enviar o e-mail tipo: {} e id: {}", agendamentoEmailDTO.getTipoEmail(), agendamentoEmailDTO.getIdAgendamento());
            }
            log.info("#### ({}) Email do tipo: {} e id do agendamento: {} enviado com sucesso", LocalDateTime.now(), agendamentoEmailDTO.getTipoEmail(), agendamentoEmailDTO.getIdAgendamento());
        }catch(JsonProcessingException | NullPointerException | KafkaException e){

        }
    }

    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            topicPartitions = {@TopicPartition(topic = "${kafka.topic}", partitions = {"2"})},
            containerFactory = "listenerContainerFactory1"
    )
    public void consumeSolicitacao(@Payload String mensagem,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Long partition) throws JsonProcessingException {
        try{
            SolicitacaoEmailDTO solicitacaoEmailDTO = objectMapper.readValue(mensagem, SolicitacaoEmailDTO.class);

            try {
                emailService.sendEmailCliente(solicitacaoEmailDTO.getEmailCliente(), solicitacaoEmailDTO);
            } catch (MessagingException | TemplateException | IOException e) {
                log.error("Erro ao enviar o e-mail tipo: {} e id: {}", solicitacaoEmailDTO.getTipoEmail(), solicitacaoEmailDTO.getIdSoliciatacao());
            }
            log.info("#### ({}) Email do tipo: {} e id da solicitação: {} enviado com sucesso", LocalDateTime.now(), solicitacaoEmailDTO.getTipoEmail(), solicitacaoEmailDTO.getIdSoliciatacao());
        }catch(JsonProcessingException | NullPointerException | KafkaException e){

        }
    }

}
