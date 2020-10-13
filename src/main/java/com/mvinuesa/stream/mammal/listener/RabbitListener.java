package com.mvinuesa.stream.mammal.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvinuesa.stream.mammal.model.MammalEvent;
import io.cloudevents.v02.CloudEventImpl;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

/**
 * Function composition and adaptation (e.g., composing imperative functions with reactive). Read
 * events (cad/dog events) and transform in a {@link com.mvinuesa.stream.mammal.model.Mammal}
 *
 * @since 1.0.0
 * @author mvinuesa
 */
@Configuration
@Slf4j
public class RabbitListener {

  @Bean
  public Function<Flux<Message<CloudEventImpl<JsonNode>>>, Flux<MammalEvent>> catEventProcessor(
      ObjectMapper objectMapper) {
    return mammalProcessor(objectMapper);
  }

  @Bean
  public Function<Flux<Message<CloudEventImpl<JsonNode>>>, Flux<MammalEvent>> dogEventProcessor(
      ObjectMapper objectMapper) {
    return mammalProcessor(objectMapper);
  }

  private Function<Flux<Message<CloudEventImpl<JsonNode>>>, Flux<MammalEvent>> mammalProcessor(
      ObjectMapper objectMapper) {
    return inbound ->
        inbound
            .map(
                message -> {
                  LOGGER.info(
                      "[mammalProcessor] Received cloud event with headers: {} and body: {}",
                      message.getHeaders(),
                      message.getPayload().getData());
                  return message;
                })
            .filter(cloudEventMessage -> cloudEventMessage.getPayload().getData().isPresent())
            .map(
                message ->
                    objectMapper.convertValue(message.getPayload().getData(), MammalEvent.class));
  }

  @Bean
  public Consumer<Flux<MammalEvent>> mammalConsumer() {
    return opportunityAnalytics -> opportunityAnalytics.subscribe(this::traceEvent);
  }

  public void traceEvent(MammalEvent mammalEvent) {
    LOGGER.info("[traceEvent] mammal event {}", mammalEvent);
  }
}
