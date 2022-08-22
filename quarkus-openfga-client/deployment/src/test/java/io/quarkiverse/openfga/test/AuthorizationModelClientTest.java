package io.quarkiverse.openfga.test;

import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.StoresClient;
import io.quarkiverse.openfga.client.model.*;
import io.quarkus.test.QuarkusUnitTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

public class AuthorizationModelClientTest {

    // Start unit test with extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Inject
    StoresClient storesClient;

    Store store;
    StoreClient storeClient;

    AuthorizationModelClient authorizationModelClient;

    @BeforeEach
    public void createTestStoreAndModel() {
        store = storesClient.create("test").await().atMost(ofSeconds(10));
        storeClient = storesClient.store(store.getId());

        // ensure it has an auth model
        var documentTypeDef = new TypeDefinition("document", Map.of("reader", Userset.direct()));

        var authModelId = storeClient.authorizationModels().create(List.of(documentTypeDef))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();

        authorizationModelClient = storeClient.authorizationModels().model(authModelId);
    }

    @AfterEach
    public void deleteTestStore() {
        if (storeClient != null) {
            storeClient.delete().await().atMost(ofSeconds(10));
        }
    }

    @Test
    @DisplayName("Can Read & Write Tuples")
    public void canReadWriteTuples() {

        var tuples = List.of(
                TupleKey.of("document:123", "reader", "me"));
        var writes = authorizationModelClient.write(tuples, emptyList())
                .subscribe().withSerializedSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();
        assertThat(writes.entrySet(), hasSize(0));

        var foundTuples = storeClient.readAllTuples()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .stream().map(Tuple::getKey).collect(Collectors.toList());

        assertThat(foundTuples, equalTo(tuples));
    }

    @Test
    @DisplayName("Can Execute Checks")
    public void canExecuteChecks() {

        // ensure it has an auth model
        var documentTypeDef = new TypeDefinition("document", Map.of("reader", Userset.direct()));

        var authModelId = storeClient.authorizationModels().create(List.of(documentTypeDef))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();

        var tuple = TupleKey.of("document:123", "reader", "me");

        var writes = authorizationModelClient.write(List.of(tuple), emptyList())
                .subscribe().withSerializedSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();
        assertThat(writes.entrySet(), hasSize(0));

        var allowed = authorizationModelClient.check(tuple, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();
        assertThat(allowed, equalTo(true));
    }

}
