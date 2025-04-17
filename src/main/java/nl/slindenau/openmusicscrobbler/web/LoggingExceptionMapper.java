package nl.slindenau.openmusicscrobbler.web;

import com.google.common.collect.Iterables;
import io.dropwizard.jersey.validation.JerseyViolationException;
import nl.slindenau.openmusicscrobbler.web.model.WebApplicationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public abstract class LoggingExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    private final Logger logger = LoggerFactory.getLogger(LoggingExceptionMapper.class);

    @Override
    public Response toResponse(T throwable) {
        logException(throwable);
        return buildResponse(throwable);
    }

    protected abstract Response buildResponse(T throwable);

    private void logException(Throwable exception) {
        logger.error(exception.getMessage(), exception);
        exception.printStackTrace(System.err);
    }

    protected WebApplicationResponse toWebApplicationResponse(String message) {
        return new WebApplicationResponse(false, message);
    }

    protected WebApplicationResponse.DetailResponse toWebApplicationDetailResponse(String message) {
        return new WebApplicationResponse.DetailResponse(false, message);
    }

    protected WebApplicationResponse.DetailResponse.DetailMessage toDetailMessage(String element, String message) {
        return new WebApplicationResponse.DetailResponse.DetailMessage(false, element, message);
    }

    public static class ThrowableExceptionMapper extends LoggingExceptionMapper<Throwable> {

        @Override
        public Response buildResponse(Throwable exception) {
            return Response
                    .serverError()
                    .entity(toWebApplicationResponse(exception.getMessage()))
                    .build();
        }
    }

    public static class WebApplicationExceptionMapper extends LoggingExceptionMapper<WebApplicationException> {

        @Override
        public Response buildResponse(WebApplicationException exception) {
            return Response
                    .status(exception.getResponse().getStatus())
                    .entity(toWebApplicationResponse(exception.getMessage()))
                    .build();
        }
    }

    public static class ConstraintViolationExceptionMapper extends LoggingExceptionMapper<JerseyViolationException> {

        @Override
        public Response buildResponse(JerseyViolationException exception) {
            WebApplicationResponse.DetailResponse detailResponse = toWebApplicationDetailResponse(exception.getMessage());

            exception.getConstraintViolations().stream()
                    .map(this::constraintViolationToDetailMessage)
                    .forEach(detailResponse::addDetail);

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(detailResponse)
                    .build();
        }

        private WebApplicationResponse.DetailResponse.DetailMessage constraintViolationToDetailMessage(ConstraintViolation<?> constraintViolation) {
            String element = Iterables.getLast(constraintViolation.getPropertyPath()).getName();
            String message = constraintViolation.getMessage();
            return toDetailMessage(element, message);
        }
    }
}
