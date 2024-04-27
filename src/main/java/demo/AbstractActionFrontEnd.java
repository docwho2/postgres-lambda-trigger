package demo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

/**
 * Base class for performing some action (on the DB) and redirecting back to the main view
 */
public abstract class AbstractActionFrontEnd implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Initialize the Log4j logger.
    Logger log = LogManager.getLogger();


    protected final static Table<Record> ADDRESS_TABLE = DSL.table("address");
    
    final static Map<String, String> headers = new HashMap<>();
    
    static {
        headers.put("Cache-Control", "no-cache");

        // Redirect back to main page
        headers.put("Location", "/");
    }
    
    protected abstract void performAction();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        log.debug(input);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                // Redirect Temp
                .withStatusCode(307);
        try {
            performAction();
            return response;
        } catch (Exception e) {
            log.error("Front End Error", e);
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }

}
