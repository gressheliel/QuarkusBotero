package org.elhg.resteasyjackson;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.elhg.entities.Customer;
import org.elhg.entities.Product;
import org.elhg.repositories.CustomerRepository;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerApi {

    private static final String LOCALHOST = "localhost";
    private static final String PATH ="/product";
    private static final int PORT = 8082;

    @Inject
    CustomerRepository pr;

    @Inject
    Vertx vertx;

    private WebClient webClient;

    @PostConstruct
    void initialize() {
        WebClientOptions webClientOptions = new WebClientOptions()
                .setDefaultHost(LOCALHOST)
                .setDefaultPort(PORT)
                .setSsl(false)
                .setTrustAll(true);

        this.webClient = WebClient.create(vertx, webClientOptions);
    }


    @GET
    @Blocking
    public List<Customer> list() {
        return pr.listCustomer();
    }

    @GET
    @Path("/{Id}")
    @Blocking
    public Customer getById(@PathParam("Id") Long Id) {
        return pr.findCustomer(Id);
    }

    @GET
    @Path("/{id}/product")
    @Blocking
    public Uni<Customer> getByIdProduct(@PathParam("id") Long id) {
        return Uni.combine().all().unis(getCustomerReactive(id), getAllProducts())
                .asTuple()
                .map(tuple -> {
                    Customer customer = tuple.getItem1();
                    List<Product> products = tuple.getItem2();
                    customer.getProducts().forEach(product -> {
                        products.stream()
                                .filter(p -> Objects.equals(p.getId(), product.getProduct()))
                                .findFirst()
                                .ifPresent(p -> {
                                    product.setName(p.getName());
                                    product.setDescription(p.getDescription());
                                    product.setCode(p.getCode());
                                });
                    });
                    return customer;
                });
       /*return Uni.combine().all().unis(getCustomerReactive(Id),getAllProducts())
                .combinedWith((v1,v2) -> {
                    v1.getProducts().forEach(product -> {
                       v2.forEach(p -> {
                           if(product.getId().equals(p.getId())){
                               product.setName(p.getName());
                               product.setDescription(p.getDescription());
;                           }
                       });
                    });
                    return v1;
                });*/
    }

    @POST
    @Blocking
    public Response add(Customer c) {
        c.getProducts().forEach(p-> p.setCustomer(c));
        pr.createdCustomer(c);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{Id}")
    @Blocking
    public Response delete(@PathParam("Id") Long Id) {
        Customer customer = pr.findCustomer(Id);
        pr.deleteCustomer(customer);
        return Response.ok().build();
    }
    @PUT
    @Blocking
    public Response update(Customer p) {
        Customer customer = pr.findCustomer(p.getId());
        customer.setCode(p.getCode());
        customer.setAccountNumber(p.getAccountNumber());
        customer.setSurname(p.getSurname());
        customer.setPhone(p.getPhone());
        customer.setAddress(p.getAddress());
        customer.setProducts(p.getProducts());
        pr.updateCustomer(customer);
        return Response.ok().build();
    }


    Uni<Customer> getCustomerReactive(Long id){
        return Uni.createFrom()
            .item(()-> pr.findCustomer(id))
            .onItem().ifNull().failWith(()-> new WebApplicationException("No se encontró el cliente con Id : "+id, 404));
    }

    Uni<List<Product>> getAllProducts(){
        return webClient.get(PORT, LOCALHOST, PATH).send()
            .onFailure().invoke(res -> System.out.println("Error recuperando productos "+ res))
            .onItem().transform(getHttpResponseListFunction());
    }

    Function<HttpResponse<Buffer>, List<Product>> getHttpResponseListFunction() {
        return res -> {
            List<Product> lista = new ArrayList<>();
            JsonArray objects = res.bodyAsJsonArray();
            objects.forEach(p -> {
                System.out.println("See Objects: " + p);
                ObjectMapper objectMapper = new ObjectMapper();
                // Pass JSON string and the POJO class
                Product product = null;
                try {
                    product = objectMapper.readValue(p.toString(), Product.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                lista.add(product);
            });
            return lista;
        };
    }


}
