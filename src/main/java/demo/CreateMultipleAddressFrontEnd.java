package demo;

import org.jooq.impl.DSL;

/**
 * Handler for requests to Lambda function.
 */
public class CreateMultipleAddressFrontEnd extends AbstractActionFrontEnd {

    @Override
    protected void performAction() {
        // Insert 5 addresses in one transaction so they all hit at once
        dsl.transaction((configuration) -> {
            var dslT = configuration.dsl();
            dslT.insertInto(ADDRESS_TABLE)
                .set(DSL.field("address_1"), "One Microsoft Way")
                .set(DSL.field("city"), "Redmond")
                .set(DSL.field("district"), "WA")
                .set(DSL.field("address_notes"), "Batch Address #1")
                .execute();
            dslT.insertInto(ADDRESS_TABLE)
                .set(DSL.field("address_1"), "1 Tesla Road")
                .set(DSL.field("city"), "Austin")
                .set(DSL.field("district"), "TX")
                .set(DSL.field("address_notes"), "Batch Address #2")
                .execute();
            dslT.insertInto(ADDRESS_TABLE)
                .set(DSL.field("address_1"), "1 Rocket Road")
                .set(DSL.field("city"), "Hawthorne")
                .set(DSL.field("district"), "CA")
                .set(DSL.field("address_notes"), "Batch Address #3")
                .execute();
            dslT.insertInto(ADDRESS_TABLE)
                .set(DSL.field("address_1"), "1 State Farm Plaza")
                .set(DSL.field("city"), "Bloomington")
                .set(DSL.field("district"), "IL")
                .set(DSL.field("address_notes"), "Batch Address #4")
                .execute();
            dslT.insertInto(ADDRESS_TABLE)
                .set(DSL.field("address_1"), "1030 Delta Blvd")
                .set(DSL.field("city"), "Atlanta")
                .set(DSL.field("district"), "GA")
                .set(DSL.field("address_notes"), "Batch Address #5")
                .execute();
        });
    }

}
