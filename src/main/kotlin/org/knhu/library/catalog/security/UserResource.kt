package org.knhu.library.catalog.security

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/api/users")
class UserResource {

    @GET
    @RolesAllowed(Role.USER, Role.ADMIN, Role.CATALOGER, Role.EDITOR)
    @Produces(MediaType.TEXT_PLAIN)
    fun me(@Context securityContext: SecurityContext): String =
        securityContext.userPrincipal.name


    @GET
    @RolesAllowed(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    fun listUsers(@Context securityContext: SecurityContext): List<User> =
        User.listAll()


}