package com.aldisued.iot.monitoring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(final List<Double> values, final Double deviation) {
    if (deviation == null || deviation < 0 || deviation > 1) {
      throw new IllegalArgumentException("deviation must be between 0 and 1");
    }

    final var avg = getAverage(values);
    final var threshold = avg * deviation;
    final var acceptableMin = avg - threshold;
    final var acceptableMax = avg + threshold;

    return values.stream().filter(value -> value > acceptableMin && value < acceptableMax)
        .toList();
  }

  public List<Double> getMovingAverage(final List<Double> data, final int windowSize) {
    if (windowSize <= 0 || windowSize > data.size()) {
      throw new IllegalArgumentException();
    }

    final var movingAverage = new ArrayList<Double>();
    for (int i = 0; i < data.size(); i++) {
      if (windowSize + i > data.size()) {
        break;
      }
      final var subList = data.subList(i, windowSize + i);
      movingAverage.add(getAverage(subList));
    }

    return movingAverage;
  }

  private static Double getAverage(final List<Double> values) {
    return values.stream().mapToDouble(Double::intValue).summaryStatistics().getAverage();
  }
}
