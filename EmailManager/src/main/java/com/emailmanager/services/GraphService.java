package com.emailmanager.services;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.emailmanager.models.EmailConfig;
import com.emailmanager.models.EmailRecord;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.MessageReplyParameterSet;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.MessageCollectionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GraphService {
    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private final GraphServiceClient graphClient;
    private final String userEmail;
    private static final List<String> SCOPES = List.of("https://graph.microsoft.com/.default");

    public GraphService(EmailConfig config) {
        this.userEmail = config.getUserEmail();
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .clientId(config.getClientId())
            .clientSecret(config.getClientSecret())
            .tenantId(config.getTenantId())
            .build();

        TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(SCOPES, credential);
        this.graphClient = GraphServiceClient.builder()
            .authenticationProvider(authProvider)
            .buildClient();
    }

    public List<EmailRecord> getEmailsAsync() throws Exception {
        logger.info("Fetching emails from Microsoft Graph API");
        List<EmailRecord> emails = new ArrayList<>();
        MessageCollectionRequest request = graphClient.users(userEmail)
            .messages()
            .buildRequest()
            .select("id,subject,receivedDateTime,from,internetMessageId,conversationId,internetMessageHeaders");

        MessageCollectionPage messages = request.get();
        while (messages != null) {
            for (Message message : messages.getCurrentPage()) {
                EmailRecord email = new EmailRecord(
                    message.id,
                    message.subject,
                    message.receivedDateTime,
                    message.from.emailAddress.address,
                    message.internetMessageId,
                    message.conversationId,
                    getReferencesHeader(message)
                );
                emails.add(email);
            }

            messages = messages.getNextPage() != null ? messages.getNextPage().buildRequest().get() : null;
        }

        logger.info("Retrieved {} emails", emails.size());
        return emails;
    }

    public void replyToEmail(String messageId, String replyContent) throws Exception {
        logger.info("Sending reply to message ID: {}", messageId);
        
        MessageReplyParameterSet replyParams = new MessageReplyParameterSet();
        Message reply = new Message();
        ItemBody body = new ItemBody();
        body.content = replyContent;
        body.contentType = BodyType.TEXT;
        reply.body = body;
        replyParams.message = reply;

        graphClient.users(userEmail)
            .messages(messageId)
            .reply(replyParams)
            .buildRequest()
            .post();

        logger.info("Reply sent successfully");
    }

    private String getReferencesHeader(Message message) {
        if (message.internetMessageHeaders != null) {
            return message.internetMessageHeaders.stream()
                .filter(header -> "References".equalsIgnoreCase(header.name))
                .findFirst()
                .map(header -> header.value)
                .orElse("");
        }
        return "";
    }
} 