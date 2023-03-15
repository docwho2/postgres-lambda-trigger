/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.CloudFormationCustomResourceEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.lambda.powertools.cloudformation.AbstractCustomResourceHandler;
import software.amazon.lambda.powertools.cloudformation.Response;

/**
 * Custom resource called from Cloud Formation after the RDS instance is done provisioning. This lambda is responsible
 * for running all the SQL files in the correct order to initialize the DB.
 *
 * @author sjensen
 */
public class CloudFormationCustomResource extends AbstractCustomResourceHandler {

    // Initialize the Log4j logger.
    Logger log = LogManager.getLogger();

    @Override
    protected Response create(CloudFormationCustomResourceEvent cfcre, Context cntxt) {
        try {
            log.debug("Received CREATE Event from Cloudformation",cfcre);
            final var dsl = PostgresDataSource.getDSL();
            final var task_root = System.getenv("LAMBDA_TASK_ROOT");

            final var sqlFiles = new LinkedList<String>();
            // Process the list in order
            sqlFiles.add("enableLambdaExtension.sql");
            sqlFiles.add("LambdaTriggerFuction.sql");
            sqlFiles.add("createAddressTable.sql");
            sqlFiles.add("createAddressTrigger.sql");

            for (var file : sqlFiles) {
                try {
                    dsl.execute(Files.readString(Path.of(task_root, "scripts", file)));
                } catch (Exception e) {
                    log.error("Error processing SQL file " + file, e);
                }
            }

        } catch (Exception e) {
            log.error("Could Not Process SQL Files", e);
        }
        return Response.builder()
                .value(Map.of("dboperations", "success"))
                .build();
    }

    @Override
    protected Response update(CloudFormationCustomResourceEvent cfcre, Context cntxt) {
        log.debug("Received UPDAATE Event from Cloudformation",cfcre);
        return null;
    }

    @Override
    protected Response delete(CloudFormationCustomResourceEvent cfcre, Context cntxt) {
        log.debug("Received DELETE Event from Cloudformation",cfcre);
        return null;
    }

}
