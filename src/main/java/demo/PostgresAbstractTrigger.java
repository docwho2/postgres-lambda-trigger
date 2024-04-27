/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *  Base class for handling triggers from Postgres
 * 
 * @author sjensen
 */
public abstract class PostgresAbstractTrigger implements RequestStreamHandler {
    // Initialize the Log4j logger.

    final Logger log = LogManager.getLogger();

    final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public final void handleRequest(InputStream in, OutputStream out, Context cntxt) throws IOException {
        // Read in JSON Tree
        var json = mapper.readTree(in);
        log.debug("INPUT JSON is " + json.toPrettyString());

        try {
            final var operation = TG_OP.valueOf(json.findValue("TG_OP").asText());
            final var table_name = json.findValue("TG_TABLE_NAME").asText();
            final var old_record = json.findValue("old");
            final var new_record = json.findValue("new");

            processEvent(operation, table_name, old_record, new_record);
        } catch (Exception e) {
            log.error("Error Processing Event", e);
        }

        try (Writer w = new OutputStreamWriter(out, "UTF-8")) {
            w.write(mapper.createObjectNode().put("status", "OK").toString());
        }
    }

    /**
     *
     * @param operation
     * @param table_name
     * @param old_record
     * @param new_record
     */
    protected abstract void processEvent(TG_OP operation, String table_name, JsonNode old_record, JsonNode new_record);

    /**
     * Trigger Operations
     */
    public enum TG_OP {
        INSERT,
        UPDATE,
        DELETE
    }
}
