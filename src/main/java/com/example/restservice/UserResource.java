package com.example.restservice;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@Path("/users")
public class UserResource {
    private static final Map<Integer, User> DB = new HashMap<>();

    @GET
    @Produces("application/json")
    public Users getAllUsers(){
        Users users = new Users();
        users.setUsers(new ArrayList<>(DB.values()));
        return users;
    }

    @POST
    @Consumes("application/json")
    public Response createUser(User user) throws URISyntaxException
    {
        if(user.getFirstName() == null || user.getLastName() == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide all mandatory inputs").build();
        }
        user.setId(DB.values().size()+1);
        user.setUri("/user/"+user.getId());
        DB.put(user.getId(), user);
        return Response.status(Response.Status.CREATED).contentLocation(new URI(user.getUri())).build();
    }
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") int id) throws URISyntaxException {
        User user = DB.get(id);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(user).contentLocation(new URI("/user/"+id)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateUser(@PathParam("id") int id, User user) {
        User temp = DB.get(id);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        temp.setFirstName(user.getFirstName());
        temp.setLastName(user.getLastName());
        DB.put(temp.getId(), temp);
        return Response.status(Response.Status.OK).entity(temp).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) {
        User user = DB.get(id);
        if(user != null){
            DB.remove(user.getId());
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    static {
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("Raj");
        user1.setLastName("Bhardwaj");
        user1.setUri("/user/1");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Eva");
        user2.setLastName("Bhardwaj");
        user2.setUri("/user/2");

        DB.put(user1.getId(), user1);
        DB.put(user2.getId(), user2);
    }
}
