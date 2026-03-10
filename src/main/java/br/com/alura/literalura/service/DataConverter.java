package br.com.alura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T convert(String json, Class<T> clazz) {
              try {
                            return mapper.readValue(json, clazz);
              } catch (JsonProcessingException e) {
                            throw new RuntimeException("Erro ao converter JSON: " + e.getMessage(), e);
              }
    }
}
