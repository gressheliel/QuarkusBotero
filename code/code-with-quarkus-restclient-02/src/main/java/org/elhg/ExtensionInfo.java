package org.elhg;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Set;

@ApplicationScoped
public class ExtensionInfo {

    @Inject
    @RestClient
    PersonRestClient personRestClient;

    public Set<PersonRestClient.Extension> doSomething() {
          Set<PersonRestClient.Extension> restClientExtensions =
                  personRestClient.getExtensionsById("io.quarkus:quarkus-hibernate-validator");
            restClientExtensions.forEach(extension -> System.out.println("Extension: " + extension.name + " ID: " + extension.id));
            return restClientExtensions;
      }

}
