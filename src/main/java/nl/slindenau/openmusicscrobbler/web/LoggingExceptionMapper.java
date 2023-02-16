package nl.slindenau.openmusicscrobbler.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LoggingExceptionMapper implements ExceptionMapper<Throwable> {

    private final Logger logger = LoggerFactory.getLogger(LoggingExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        logger.error(exception.getMessage(), exception);
        exception.printStackTrace(System.err);
        return Response.serverError().build();
    }
}
