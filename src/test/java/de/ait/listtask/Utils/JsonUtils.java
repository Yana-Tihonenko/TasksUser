package de.ait.listtask.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class JsonUtils {

  public static JsonNode getJsonNode(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
    String responseBody = result.getResponse().getContentAsString();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    return jsonNode;
  }
}
