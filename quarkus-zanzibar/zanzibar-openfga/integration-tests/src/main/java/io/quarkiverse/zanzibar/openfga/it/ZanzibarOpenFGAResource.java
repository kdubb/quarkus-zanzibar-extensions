/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.zanzibar.openfga.it;

import static io.quarkiverse.zanzibar.jaxrs.annotations.ObjectQuery.Source.PATH;
import static io.quarkiverse.zanzibar.jaxrs.annotations.RelationAllowed.ANY;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkiverse.zanzibar.jaxrs.annotations.ObjectQuery;
import io.quarkiverse.zanzibar.jaxrs.annotations.RelationAllowed;
import io.smallrye.mutiny.Uni;

@ObjectQuery(source = PATH, sourceProperty = "id", type = "thing")
interface Things {
    @RelationAllowed(ANY)
    Uni<Void> authorize(String user, String relation, String object);
}

@Path("/openfga")
@ApplicationScoped
@RelationAllowed("reader")
public class ZanzibarOpenFGAResource implements Things {

    @Inject
    AuthorizationModelClient authorizationModelClient;

    @POST
    @Path("authorize/{user}")
    public Uni<Void> authorize(@PathParam("user") String user, @QueryParam("relation") String relation,
            @QueryParam("object") String object) {
        return authorizationModelClient.write(TupleKey.of("thing:" + object, relation, user));
    }

    @GET
    @Path("things/{id}")
    public String getThing(@PathParam("id") String id) {
        return "Thing " + id;
    }
}