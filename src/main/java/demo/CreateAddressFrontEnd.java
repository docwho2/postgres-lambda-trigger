package demo;

import org.jooq.impl.DSL;

/**
 * Handler for requests to Lambda function.
 */
public class CreateAddressFrontEnd extends AbstractActionFrontEnd {

    @Override
    protected void performAction() {
        // Just Insert One address and return
        dsl.insertInto(ADDRESS_TABLE)
                .set(DSL.field("address_1"), "1 Apple Park Way")
                .set(DSL.field("city"), "Cupertino")
                .set(DSL.field("district"), "CA")
                .set(DSL.field("address_notes"), "Apple HQ")
                .execute();
    }

}
