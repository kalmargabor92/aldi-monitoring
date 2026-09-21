package com.aldisued.iot.monitoring.controller.response;

import java.time.LocalDateTime;

public record AlertResponse(
    String sensorName,
    String message,
    LocalDateTime timestamp
) {}
