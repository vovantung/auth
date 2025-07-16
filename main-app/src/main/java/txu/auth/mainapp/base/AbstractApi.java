package txu.auth.mainapp.base;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import txu.common.exception.TxException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class AbstractApi {

    private static final String HTTP_RESPONSE_ERROR_BODY_JSON_FORMAT = "{ \"errorType\": \"%s\", \"errorMessage\": \"%s\" }";


//    @ExceptionHandler(ForbiddenActionException.class)
//    @ResponseBody
//    public void handleForbiddenActionException(Throwable tr, HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        writeError(response, tr);
//    }
//
//    @ExceptionHandler({InvalidCredentialException.class, AuthenticationException.class})
//    @ResponseBody
//    public void handleInvalidCredentialException(Throwable tr, HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        writeError(response, tr);
//    }
//
//    @ExceptionHandler(ConflictException.class)
//    @ResponseBody
//    public void handleConflictParam(
//            Throwable exception, HttpServletResponse response) throws IOException {
//
//        response.setStatus(HttpServletResponse.SC_CONFLICT);
//        writeError(response, exception);
//    }

    /**
     * For missing required parameters.
     */
//    @ExceptionHandler(ServletRequestBindingException.class)
//    @ResponseBody
//    public void handleServletRequestBindingException(
//            ServletRequestBindingException exception, HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        writeError(response, exception);
//    }
//
//    /**
//     * For input wrong parameter value such as 'abc' for boolean, '12a' for integer.
//     */
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    @ResponseBody
//    public void handleArgumentTypeMismatchException(
//            MethodArgumentTypeMismatchException exception, HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        String message = StringUtils.join("Failed to convert value of parameter '", exception.getName(),
//                "' to type ", exception.getRequiredType().getSimpleName());
//        writeError(response, exception.getClass().getSimpleName(), message);
//    }
//
//    /**
//     * For missing required multipart parameter.
//     */
//    @ExceptionHandler(MissingServletRequestPartException.class)
//    @ResponseBody
//    public void handleMissingServletRequestPartException(
//            MissingServletRequestPartException exception, HttpServletResponse response) throws IOException{
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        writeError(response, exception);
//    }
//
//    /**
//     * For uploading too big file error (exceed 35MB)
//     */
//    @ExceptionHandler(MultipartException.class)
//    @ResponseBody
//    public void handleMultipartException(
//            MultipartException exception, HttpServletResponse response) throws IOException{
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        writeError(response, exception);
//    }
//
//    /**
//     * The json format is incorrect.
//     */
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseBody
//    public void handleHttpMessageNotReadableException(
//            HttpMessageNotReadableException exception, HttpServletResponse response) throws IOException {
//
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        writeError(response, exception);
//    }
    @ExceptionHandler(TxException.class)
    @ResponseBody
    public void handleTxRuntimeException(TxException exceptionRaised, HttpServletResponse response) throws IOException {
        if (exceptionRaised.getStatusCode() >= HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            log.error("Exception handled by spring, will return status code error " + exceptionRaised.getStatusCode(), exceptionRaised);
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Exception handled by spring, will return status code error " + exceptionRaised.getStatusCode(), exceptionRaised);
            }
        }
        response.setStatus(exceptionRaised.getStatusCode());
        writeError(response, exceptionRaised);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void handleException(Exception e, HttpServletResponse response) throws IOException {
        log.error("Exception handled by Spring, return status code 500: ", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeError(response, e);
    }

    private void writeError(HttpServletResponse response, Throwable throwable) throws IOException {
        writeError(response, throwable.getClass().getSimpleName(), throwable.getMessage());
    }

    private void writeError(HttpServletResponse response, String className, String message) throws IOException {
        String body = String.format(
                HTTP_RESPONSE_ERROR_BODY_JSON_FORMAT, className, message);
        response.setContentType("application/json;charset=UTF-8");
        IOUtils.write(body, response.getOutputStream(), StandardCharsets.UTF_8);
    }
}
