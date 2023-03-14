/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.CloudFormationCustomResourceEvent;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.lambda.powertools.cloudformation.AbstractCustomResourceHandler;
import software.amazon.lambda.powertools.cloudformation.Response;
import software.amazon.lambda.powertools.logging.Logging;

/**
 *
 * @author sjensen
 */
public class DBScripting extends AbstractCustomResourceHandler {

    // Initialize the Log4j logger.
   Logger log = LogManager.getLogger();
    
    @Override
    protected Response create(CloudFormationCustomResourceEvent cfcre, Context cntxt) {
        try {
            log.debug(cfcre);
            final var dsl = PostgresDataSource.getDSL();
            
            log.debug("Creating address table");
            dsl.execute("CREATE TABLE IF NOT EXISTS address\n"
                    + "(\n"
                    + "    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),\n"
                    + "    created timestamp without time zone NOT NULL DEFAULT timezone('utc'::text, now()),\n"
                    + "    updated timestamp without time zone,\n"
                    + "    address_1 text COLLATE pg_catalog.\"default\" NOT NULL,\n"
                    + "    address_2 text COLLATE pg_catalog.\"default\",\n"
                    + "    city text COLLATE pg_catalog.\"default\" NOT NULL,\n"
                    + "    district text COLLATE pg_catalog.\"default\" NOT NULL,\n"
                    + "    postal_code text COLLATE pg_catalog.\"default\",\n"
                    + "    address_entered text COLLATE pg_catalog.\"default\",\n"
                    + "    geo_coded timestamp without time zone,\n"
                    + "    geo_last_coding jsonb,\n"
                    + "    geo_latitude double precision,\n"
                    + "    geo_longitude double precision,\n"
                    + "    address_formatted text COLLATE pg_catalog.\"default\",\n"
                    + "    address_notes text COLLATE pg_catalog.\"default\",\n"
                    + "    external_id text COLLATE pg_catalog.\"default\",\n"
                    + "    requires_geo_coding boolean NOT NULL DEFAULT false,\n"
                    + "    CONSTRAINT address_pkey PRIMARY KEY (id),\n"
                    + ")");
        } catch (Exception e) {
            log.error("address", e);
        }
        return Response.builder()
                .value(Map.of("dboperations", "success"))
                .build();

    }

    @Override
    protected Response update(CloudFormationCustomResourceEvent cfcre, Context cntxt) {
        log.debug(cfcre);
        return null;
    }

    @Override
    protected Response delete(CloudFormationCustomResourceEvent cfcre, Context cntxt) {
        log.debug(cfcre);
        return null;
    }

}
