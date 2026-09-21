package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

  private final SensorReadingRepository sensorReadingRepository;

  public MeasurementService(SensorReadingRepository sensorReadingRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
  }

  public List<Double> getMeasurementValuesBySensorType(final SensorType sensorType, final LocalDateTime from, final LocalDateTime to) {
    return sensorReadingRepository.findBySensorTypeAndTimestampGreaterThanEqualAndTimestampLessThanEqual(sensorType, from, to).stream()
        .sorted(Comparator.comparing(SensorReading::getTimestamp))
        .map(SensorReading::getValue)
        .toList();
  }

  public Optional<Double> getAverageTemperature(final LocalDateTime from, final LocalDateTime to) {
    final var optAverage = sensorReadingRepository.findBySensorTypeAndTimestampGreaterThanEqualAndTimestampLessThanEqual(SensorType.TEMPERATURE, from, to)
        .stream()
        .mapToDouble(SensorReading::getValue)
        .average();

    return optAverage.isEmpty() ? Optional.empty() : Optional.of(optAverage.getAsDouble());
  }

}
