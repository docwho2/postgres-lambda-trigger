/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.jooq.JSONB;
import org.jooq.impl.DSL;

/*
 * Audit log trigger that takes incoming event and writes to audit log table

  CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    created timestamp without time zone NOT NULL DEFAULT timezone('utc'::text, now()),
    operation tg_op NOT NULL,
    table text NOT NULL,
    old_record jsonb,
    new_record jsonb
);
 * 
 * @author sjensen
 */
public class PostgresAuditLogTrigger extends PostgresAbstractTrigger {

    /**
     *
     * @param operation
     * @param table_name
     * @param old_record
     * @param new_record
     */
    @Override
    protected void processEvent(TG_OP operation, String table_name, JsonNode old_record, JsonNode new_record) {

        // Just insert an Audit row for the operation
        var dsl = PostgresDataSource.getDSL();
        dsl.insertInto(DSL.table(DSL.name("audit_log")))
                .set(DSL.field("operation"), operation.toString())
                .set(DSL.field("table_name"), table_name)
                .set(DSL.field("old_record",JSONB.class), old_record == null || old_record.isNull() ? null : JSONB.jsonb(old_record.toString()))
                .set(DSL.field("new_record",JSONB.class), new_record == null || new_record.isNull() ? null : JSONB.jsonb(new_record.toString()))
                .execute();
    }

}
