package org.elhg;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;


@Path("/extensions")
public class ExtensionInfoApi {

    @Inject
    ExtensionInfo extensionInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String extensionInfoApi(){
        Set<PersonRestClient.Extension> extensions =
                extensionInfo.doSomething();

        StringBuilder serialized = new StringBuilder();
        for (PersonRestClient.Extension extension : extensions) {
            serialized.append("ID:").append(extension.id)
                    .append("Name: ").append(extension.name)
                    .append("ShortName: ").append(extension.shortName)
                    .append("Keywords: ").append(String.join(", ", extension.keywords));
        }

        return serialized.toString();

    }
}
