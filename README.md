# Quarkus Extension for Zanzibar Style Fine Grained Authorization

## Getting Started

Build & install `quarkus-openfga-client` first, followed by `quarkus-zanzibar`.

For your own project, include the `quarkus-zanzibar-openfga` extension in addition to either the `resteasy-reactive` or `resteasy` classic extensions (one or the other is required).

```xml
<dependency>
  <groupId>io.quarkiverse.zanzibar</groupId>
  <artifactId>quarkus-zanzibar-openfga</artifactId>
  <version>${project.version}</version>
</dependency>
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-resteasy-reactive</artifactId>
</dependency>
```

### Example

Look in `quarkus-zanzibar/zanzibar-openfga/integration-tests` for an example that shows how to use FGA JAX-RS annotations and how to use the OpenFGA client to update the authorization model.

## Overview

### Extensions

There are currently three extensions organized in a way that allows on of multple implementations of Zanzibar style FGA to be targeted. Currently only the OpenFGA implementation is suppored.

#### `quarkus-zanzibar`

This extension is the main FGA extension that provdides the "client level view" of using FGA. Currently it provides JAX-RS style authorization filters and related annotations to apply FGA to endpoints.

The extension provides two filters one supporting RestEasy Reactive and another supporting classic RestEasy. The implementation is selected based on whatever is included by the project.

#### `quarkus-zanzibar-openfga`

The OpenFGA "connector" for `quarkus-zanzibar`. This provides the required `Authorizer` the authorization filter useds to perform authorization checks. It depends on `quarkus-openfga-client` for all access to an OpenFGA instance.

#### `quarkus-openfga-client`

A reactive Vert.x based OpenFGA client. The client is a standalone extension that can be used without any integration into JAX-RS.

##### DevServices

The OpenFGA client supports DevServices and will start an unauthenticated OpenFGA server for dev and test.

OpenFGA DevServces supports initializing a store and an authorization model during startup. Using the following property:

```properties
quarkus.open-fga.devservices.authorization-model=<json authorization model>
```

### Annotations

There are currently only two annotations to apply FGA to resource endpoints. `RelationAllowed` determines what relation the endpoint requires and `ObjectQuery` which details how to extract the object from the current request.

The annotations are designed to be used much like the current RBAC annotations (e.g. `RolesAllowed`).

### `RelationAllowed`

This ia a simple annotation with a single string value. Note that unlike RBAC, Zanzibar style FGA queries target a single "relation". Given the query model, currently authorzation is limited to specifying a single realtion.

### `ObjectQuery`

For an FGA query the authorization check needs to know what "object" is beting targeted. This is done using the `ObjectQuery` annotation. 

`ObjectQuery` has three required properties:

* `sourceProperty` - The property on `source` that provides the object identity.
* `source` - The location `sourceProperty` is looked up on. Defaults to `PATH`.
* * 'PATH' - Source is from the current request path paramters.
* * 'QUERY' - Source is from the current request query parameters.
* * 'HEADER' - Source is from the current request headers.
* * 'REQUEST' - Source is from the current request properties.
* `type` - Type of object that is being looked up; freeform string that is defined in the authorization model.
