/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.impl.DSL;

/*
 * Audit log trigger that takes incoming event and writes to audit log table

  CREATE TABLE IF NOT EXISTS audit_log_sqs (
    id BIGSERIAL PRIMARY KEY,
    created timestamp without time zone NOT NULL DEFAULT timezone('utc'::text, now()),
    operation tg_op NOT NULL,
    table text NOT NULL,
    old_record jsonb,
    new_record jsonb
);
 * 
 * Receive the SQS payload for Audit log entry
 */
public class PostgresAuditLogTriggerSQS implements RequestHandler<SQSEvent, Void> {

    final Logger log = LogManager.getLogger();

    final static DSLContext dsl = PostgresDataSource.getDSL();

    final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        try {
            // We will only ever get one message set on the trigger
            SQSEvent.SQSMessage mesg = event.getRecords().get(0);
            String body = mesg.getBody();

            if (body == null || body.isEmpty()) {
                log.error("Message Body is empty, ignoring message");
                return null;
            }
            // Read in JSON Tree 
            var json = mapper.readTree(body);
            log.debug("INPUT EVENT JSON is " + json.toPrettyString());

            final var operation = json.findValue("TG_OP").asText();
            final var table_name = json.findValue("TG_TABLE_NAME").asText();
            final var old_record = json.findValue("old");
            final var new_record = json.findValue("new");

            // Just insert an Audit row for the operation
            dsl.insertInto(DSL.table("audit_log_sqs"))
                    .set(DSL.field("operation"), operation)
                    .set(DSL.field("table_name"), table_name)
                    .set(DSL.field("old_record", JSONB.class), old_record == null || old_record.isNull() ? null : JSONB.jsonb(old_record.toString()))
                    .set(DSL.field("new_record", JSONB.class), new_record == null || new_record.isNull() ? null : JSONB.jsonb(new_record.toString()))
                    .execute();

        } catch (Exception e) {
            log.error("Error Processing Event", e);
        }

        return null;
    }

}
