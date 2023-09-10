package demo;

/**
 * Delete the last row from Address Table
 */
public class DeleteAuditLogFrontEnd extends AbstractActionFrontEnd {

    @Override
    protected void performAction() {
        // Clear out the audit log
        var dsl = PostgresDataSource.getDSL();
        dsl.truncate("audit_log").execute();
        dsl.truncate("audit_log_sqs").execute();
    }

}
