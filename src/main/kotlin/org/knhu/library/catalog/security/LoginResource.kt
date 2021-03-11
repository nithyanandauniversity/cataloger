package org.knhu.library.catalog.security

import java.io.InputStream
import java.net.URI
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.servlet.ServletContext
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/")
class LoginResource {

    @GET
    @RolesAllowed(Role.USER, Role.ADMIN, Role.CATALOGER, Role.EDITOR)
    @Produces(MediaType.TEXT_HTML)
    fun login(): InputStream {
        //Response.status(200).location(URI.create("/catalog.html"))
        //Response.temporaryRedirect(URI.create("/catalog.html"))
        return javaClass.getResourceAsStream("/META-INF/resources/catalog.html")
    }
}