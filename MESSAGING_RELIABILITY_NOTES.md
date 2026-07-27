# Messaging Reliability Additions

## Kafka

- Auth producer uses `acks=all`, idempotence, bounded retries, and delivery/request timeouts.
- `EventPublisher.event` returns Kafka's `CompletableFuture` and records success or failure.
- Audit and notification consumers retry twice after the original attempt with a one-second backoff.
- Records that continue to fail are published to `<original-topic>.DLT`, for example `audit-events.DLT`.
- Consumers use record acknowledgment and disabled auto-commit.
- `AuditListener` already provides idempotent handling by checking `eventId` before saving.

## RabbitMQ

- Durable exchange: `notification.exchange`
- Durable queue: `notification.email.queue`
- Routing key: `notification.email`
- Dead-letter exchange: `notification.dlx`
- Dead-letter queue: `notification.email.dlq`
- Failed-message routing key: `notification.email.failed`
- Publisher confirms and returned-message callbacks are enabled.
- The consumer retries three total attempts with exponential backoff, then republishes to the DLQ.
- Prefetch is 10, with 2 initial consumers and up to 5 consumers.

## CompletableFuture

- Kafka publishing uses the `CompletableFuture` returned by `KafkaTemplate.send`.
- Rabbit publishing runs through a named Spring async executor rather than the common ForkJoinPool.
- Notification processing uses a separate bounded executor and propagates failures back to the Rabbit listener so retry/DLQ handling can occur.

## Testing failures

1. Start dependencies and services with `docker compose up --build`.
2. Open RabbitMQ Management at `http://localhost:15672` using `guest` / `guest`.
3. Publish a notification with an empty recipient to force processing failure.
4. After retries, inspect `notification.email.dlq`.
5. Throw an exception in a Kafka listener temporarily to observe the record in `audit-events.DLT`.
