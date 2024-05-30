package com.currentaccount.currentaccount.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ErrorGroup")
@SuppressWarnings("unused")
public record ErrorGroupDTO(List<ErrorDTO> errors) {}
