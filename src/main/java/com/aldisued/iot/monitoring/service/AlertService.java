package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.controller.handler.AlertNotFoundException;
import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;

  public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository,
      KafkaTemplate<String, AlertDto> kafkaTemplate) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  public Alert saveAlert(final AlertDto alertDto) {
    final var sensor = sensorRepository.findById(alertDto.sensorId())
        .orElseThrow(() -> new IllegalArgumentException("Sensor not found by id: " + alertDto.sensorId()));
    final var alert = new Alert(alertDto.message(), alertDto.timestamp(), sensor);
    alertRepository.save(alert);
    kafkaTemplate.send("alerts", alertDto);
    return alert;
  }

  public AlertDto findLastAlertBySensorId(final UUID sensorId) {
    final var alert = alertRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId).orElseThrow(AlertNotFoundException::new);
    return new AlertDto(alert.getSensor().getId(), alert.getMessage(), alert.getTimestamp());
  }
}
