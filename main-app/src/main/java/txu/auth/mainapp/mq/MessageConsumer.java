package txu.auth.mainapp.mq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import txu.auth.mainapp.service.KeycloakService;
import txu.common.saga.contract.command.CreateKeycloakUserCommand;
import txu.common.saga.contract.command.DeleteUserKeycloakCommand;
import txu.common.saga.contract.command.SagaReplyEvent;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class MessageConsumer {
    private final JmsTemplate jmsTemplate;
    private final KeycloakService keycloakService;

    @JmsListener(destination = "keycloak.create.user.queue")
    public void createKeycloakUser(CreateKeycloakUserCommand cmd) {

        try {
            String keycloakUserId = keycloakService.createKeycloakUser(cmd.getUsername(), cmd.getEmail(), cmd.getLastName(), cmd.getFirstName());
            List<String> roles = cmd.getRoles();
            keycloakService.assignRealmRolesToUser(keycloakUserId, roles);
            log.info("Tạo keycloak user thành công!, sagaId: {}", cmd.getSagaId());
            SagaReplyEvent event = new SagaReplyEvent();
            event.setSagaId(cmd.getSagaId());
            event.setStep("KEYCLOAK_CREATE");
            event.setSuccess(true);
            event.setPayload(Map.of("sagaId", cmd.getSagaId(), "username", cmd.getUsername(), "email", cmd.getEmail(), "lastName", cmd.getLastName(),
                            "firstName", cmd.getFirstName(), "departmentId", cmd.getDepartmentId(), "keycloakUserId", keycloakUserId)
            );
            jmsTemplate.convertAndSend("saga.reply.queue", event, message -> {
                message.setStringProperty("_type", SagaReplyEvent.class.getName());
                return message;
            });
        } catch (Exception ex) {
            log.info("Lỗi khi tạo keycloak user: " + ex.getMessage());
            SagaReplyEvent event = new SagaReplyEvent();
            event.setSagaId(cmd.getSagaId());
            event.setStep("KEYCLOAK_CREATE");
            event.setSuccess(false);
            event.setError(ex.getMessage());
            event.setPayload(Map.of("sagaId", cmd.getSagaId()));
            jmsTemplate.convertAndSend("saga.reply.queue", event, message -> {
                message.setStringProperty("_type", SagaReplyEvent.class.getName());
                return message;
            });
        }
    }

    @JmsListener(destination = "keycloak.delete.user.queue")
    public void handleDeleteUserKeycloak(DeleteUserKeycloakCommand cmd) {
        try {
            keycloakService.deleteUserKeycloak(cmd.getKeycloakUserId());
            log.info("Da xoa keycloak user, sagaId: " + cmd.getSagaId());
            SagaReplyEvent event = new SagaReplyEvent();
            event.setSagaId(cmd.getSagaId());
            event.setStep("KEYCLOAK_DELETE");
            event.setSuccess(true);
            event.setPayload(Map.of("sagaId", cmd.getSagaId()));
            jmsTemplate.convertAndSend("saga.reply.queue", event, message -> {
                message.setStringProperty("_type", SagaReplyEvent.class.getName());
                return message;
            });

        } catch (Exception ex) {
            log.info("Lỗi khi xoa keycloak user: " + ex.getMessage());
            SagaReplyEvent event = new SagaReplyEvent();
            event.setSagaId(cmd.getSagaId());
            event.setStep("KEYCLOAK_DELETE");
            event.setSuccess(false);
            event.setError(ex.getMessage());
            event.setPayload(Map.of("sagaId", cmd.getSagaId(), "keycloakUserId", cmd.getKeycloakUserId()));
            jmsTemplate.convertAndSend("saga.reply.queue", event, message -> {
                message.setStringProperty("_type", SagaReplyEvent.class.getName());
                return message;
            });
        }
    }
}

