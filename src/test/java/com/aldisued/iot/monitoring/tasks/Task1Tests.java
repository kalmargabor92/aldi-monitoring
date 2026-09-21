package com.aldisued.iot.monitoring.tasks;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorRepository;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/sql/task-1-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class Task1Tests extends IntegrationTestBase {

  private static final UUID ID_1 = UUID.fromString("e3242ea2-0514-46d3-aad8-b2012980c41a");
  private static final UUID ID_2 = UUID.fromString("e3242ea2-0514-46d3-aad8-b2012980c41b");
  private static final UUID ID_3 = UUID.fromString("e3242ea2-0514-46d3-aad8-b2012980c41c");

  @Autowired
  private SensorRepository sensorRepository;

  @Test
  public void verifySensorType() {
    Map.of(
        ID_1, SensorType.TEMPERATURE,
        ID_2, SensorType.ATMOSPHERIC_PRESSURE,
        ID_3, SensorType.HUMIDITY
    ).forEach(this::assertSensorType);
  }

  private void assertSensorType(final UUID id, final SensorType sensorType) {
    final var sensor = sensorRepository.findById(id).orElseThrow();
    Assertions.assertEquals(sensorType, sensor.getType());
  }
}
