package eu.ibagroup.easyrpa.examples.email.message_sending_with_attachments;

import eu.ibagroup.easyrpa.engine.boot.ApModuleRunner;

public class LocalRunner {

    public static void main(String[] args) {
        ApModuleRunner.localLaunch(MessageSendingWithAttachmentsModule.class);
    }
}