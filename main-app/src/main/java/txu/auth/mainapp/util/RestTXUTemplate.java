package txu.auth.mainapp.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTXUTemplate extends RestTemplate {

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {

        ResponseEntity<T> responseEntity = null;

        try {
            responseEntity = super.getForEntity(url, responseType, uriVariables);

        } catch (HttpStatusCodeException ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<T>(ex.getStatusCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {

        ResponseEntity<T> responseEntity = null;

        try {
            responseEntity = super.postForEntity(url, request, responseType, uriVariables);

        } catch (HttpStatusCodeException ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<T>(ex.getStatusCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    public ResponseEntity put1(String url, Object request, Object... uriVariables) throws RestClientException {

        ResponseEntity responseEntity = null;

        try {
            put(url, request, uriVariables);

        } catch (HttpStatusCodeException ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity(ex.getStatusCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity delete1(String url, Object... uriVariables) throws RestClientException {


        ResponseEntity responseEntity = null;

        try {
            delete(url, uriVariables);

        } catch (HttpStatusCodeException ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity(ex.getStatusCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
