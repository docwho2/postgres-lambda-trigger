package demo;

import org.jooq.impl.DSL;

/**
 * Delete the last row from Address Table
 */
public class DeleteAddressFrontEnd extends AbstractActionFrontEnd {

    @Override
    protected void performAction() {
        // Delete the last row
        var id = DSL.field("id", Integer.class);
        var dsl = PostgresDataSource.getDSL();
        dsl.deleteFrom(ADDRESS_TABLE)
                .where(id.eq(DSL.select(DSL.max(id)).from(ADDRESS_TABLE)))
                .execute();
    }

}
