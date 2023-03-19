package demo;

/**
 * Delete the last row from Address Table
 */
public class DeleteAuditLogFrontEnd extends AbstractActionFrontEnd {

    @Override
    protected void performAction() {
        // Clear out the audit log
        dsl.truncate("audit_log").execute();
    }

}
