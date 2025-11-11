package ru.lipnin.itmohomework.feign.dwh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.dwh.DwhResponseDto;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@Slf4j
@Service
public class DwhDecoder implements Decoder {
    private final Decoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        log.info("Decoding response for type: {}, status: {}", type, response.status());

        // Если это не наш DTO, используем дефолтный декодер
        if (!type.getTypeName().equals(DwhResponseDto.class.getTypeName())) {
            return defaultDecoder.decode(response, type);
        }

        // Для HTTP ошибок используем стандартный декодер
        if (response.status() < 200 || response.status() >= 300) {
            return defaultDecoder.decode(response, type);
        }

        // Обрабатываем успешный ответ
        return processSuccessfulResponse(response);
    }

    //Проверяем и обрабатываем ответ
    private DwhResponseDto processSuccessfulResponse(Response response) throws IOException {
        Response.Body body = response.body();
        if (body == null) {
            throw new FeignException.BadRequest(
                    "Empty response body",
                    response.request(),
                    null,
                    response.headers()
            );
        }

        try (InputStream inputStream = body.asInputStream()) {
            DwhResponseDto dwhResponse = objectMapper.readValue(inputStream, DwhResponseDto.class);
            log.info("Response body is {}", dwhResponse);

            // Если ids пустой - вызываем retry
            if (dwhResponse.ids() == null || dwhResponse.ids().isEmpty()) {
                throw  new RetryableException(
                        response.status(),
                        "Коллекция в ответе пуста",
                        response.request().httpMethod(),
                        (Throwable) null,
                        (Long) null,
                        response.request()
                );
            }

            return dwhResponse;
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error", e);
            throw new FeignException.BadRequest(
                    "Invalid JSON response",
                    response.request(),
                    null,
                    response.headers()
            );
        }
    }
}