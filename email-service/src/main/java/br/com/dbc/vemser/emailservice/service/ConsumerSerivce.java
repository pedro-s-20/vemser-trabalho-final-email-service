package br.com.dbc.vemser.emailservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerSerivce {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            topicPartitions = {@TopicPartition(topic = "${kafka.topic}")},
            containerFactory = "listenerContainerFactory1"
    )
    public void consumeGeneral(@Payload String mensagem,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Long partition) throws JsonProcessingException {
        try{
            MensagemDTO mensagemDTO = objectMapper.readValue(mensagem, MensagemDTO.class);
            if(partition.equals(0)){
                log.info(mensagemDTO.getDataCriacao() + " [" + mensagemDTO.getUsuario() + "]:" + mensagemDTO.getMensagem());
            }else{
                log.info(mensagemDTO.getDataCriacao() + " [" + mensagemDTO.getUsuario() + "](privada):" + mensagemDTO.getMensagem());
            }
        }catch(JsonProcessingException | NullPointerException | KafkaException e){

        }
    }


}
