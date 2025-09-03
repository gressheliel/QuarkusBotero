package org.elhg;


import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Objects;

@Path("/api/contact")
@Produces("application/json")
@Consumes
public class ContactResource {

    private static  final Logger log = LoggerFactory.getLogger(ContactResource.class);

    @Inject
    EntityManager entityManager;

    @GET
    public Response getAllContacts(){
        log.info("Getting all contacts from the database. ");
        List<Contact> listContacts = entityManager
                .createQuery("select c from Contact c", Contact.class)
                .getResultList();
        return Response.ok(listContacts).build();
    }

    @GET
    @Path("/{id}")
    public Response getContactById(@PathParam("id") Long id){
        log.info("Getting contact with id: {} ", id);
        Contact contact = entityManager
                .find(Contact.class, id);
        if(Objects.isNull(contact)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(contact).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateContact(@PathParam("id") Long id, Contact contact){
        Contact contactInDB = entityManager
                            .find(Contact.class, id);
        if(Objects.isNull(contactInDB)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        contactInDB.setName(contact.getName());
        contactInDB.setEmail(contact.getEmail());
        contactInDB.setDataProtection(contact.isDataProtection());
        entityManager.merge(contactInDB);
        return Response.ok(contactInDB).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteContact(@PathParam("id") Long id){
        Contact contactInDB = entityManager
                .find(Contact.class, id);
        if(Objects.isNull(contactInDB)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entityManager.remove(contactInDB);
        return Response.noContent().build();
    }

    @PUT
    @Path("/email/{email}")
    @Transactional
    public Response updateContactByEmail(@PathParam("email") String email, Contact updatedContact){
        Contact contact = entityManager
                .createQuery("select c from Contact c where c.email = :email", Contact.class)
                .setParameter("email", email)
                .getSingleResult();
        if(Objects.isNull(contact)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        contact.setName(updatedContact.getName());
        contact.setEmail(updatedContact.getEmail());
        contact.setDataProtection(contact.isDataProtection());
        entityManager.merge(contact);
        return Response.ok(contact).build();
    }

    @GET
    @Path("/email/{email}")
    public Response getContactByEmail(@PathParam("email") String email) {
        Contact contact = entityManager
                .createQuery("select c from Contact c where c.email = :email", Contact.class)
                .setParameter("email", email)
                .getSingleResult();

        if (Objects.isNull(contact)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(contact).build();
    }

    @GET
    @Path("/search")
    public Response searchContacts(@QueryParam("email") String email) {
        List<Contact> contacts = entityManager
                .createQuery("select c from Contact c where c.email like :email", Contact.class)
                .setParameter("email", "%" + email + "%")
                .getResultList();
        return Response.ok(contacts).build();
    }

    @POST
    @Transactional
    public Response createContact(Contact contact) {
        entityManager.persist(contact);
        return Response.status(Response.Status.CREATED).entity(contact).build();
    }

}
