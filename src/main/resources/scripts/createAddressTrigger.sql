/**
This is an example of calling a Lambda that itself will
modify rows on this table.  So when inserting rows ensure
a field is set to true, and the Lambda must always set the
value back to false on any update to the row to prevent 
the trigger from firing again and creating recursive calls
that will likely blow everything up.
*/
DROP TRIGGER IF EXISTS "AddressLambdaTrigger" ON address;
CREATE TRIGGER "AddressLambdaTrigger"
    AFTER INSERT OR UPDATE 
    ON address
    FOR EACH ROW
    WHEN (new.requires_geo_coding IS TRUE)
    EXECUTE FUNCTION record_change_lambda('PostgresAddressTrigger');

/**
This is an example of sending insert/update/deletes for processing 
on the Lambda which will not perform any changes to the data in this
table.  It will just write audit log entries
*/
DROP TRIGGER IF EXISTS "PostgresAuditLogTrigger" ON address;
CREATE TRIGGER "PostgresAuditLogTrigger"
    AFTER INSERT OR UPDATE OR DELETE
    ON address
    FOR EACH ROW
    EXECUTE FUNCTION record_change_lambda('PostgresAuditLogTrigger');


/**
This is an example of sending insert/update/deletes for processing 
on the Lambda which will not perform any changes to the data in this
table.  It will just write audit log entries via SQS Queue
*/
DROP TRIGGER IF EXISTS "PostgresAuditLogTriggerSQS" ON address;
CREATE TRIGGER "PostgresAuditLogTriggerSQS"
    AFTER INSERT OR UPDATE OR DELETE
    ON address
    FOR EACH ROW
    EXECUTE FUNCTION record_change_lambda('ForwardEventToSQS');