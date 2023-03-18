package demo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * Handler for requests to Lambda function.
 */
public class CreateAddressFrontEnd implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

     // Initialize the Log4j logger.
    Logger log = LogManager.getLogger();
    
    final static DSLContext dsl = PostgresDataSource.getDSL();
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        log.debug(input);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cache-Control", "no-cache");
        
        // Redirect back to main page
        headers.put("Location", "/");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                // Redirect Temp
                .withStatusCode(307);
        try {
            
            
            // Just Insert One address and return
            var result = dsl.insertInto(DSL.table(DSL.name("address")))
                    .set(DSL.field("address_1"), "1 Apple Park Way")
                    .set(DSL.field("city"), "Cupertino")
                    .set(DSL.field("district"), "CA")
                    .set(DSL.field("address_notes"), "Apple HQ")
                    .execute();
            log.debug("Insert result for address = " + result);
            
            return response;
        } catch (Exception e) {
            log.error("Front End Error", e);
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }

}
