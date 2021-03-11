package org.knhu.library.catalog.security

import org.knhu.library.catalog.cataloger.Book
import javax.annotation.security.RolesAllowed
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext
@Path("/api/books")
class BookResource {

    @GET
    @RolesAllowed(Role.CATALOGER, Role.EDITOR, Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    fun getBook(@PathParam("id") bookId: Long): Book =
        Book.findById(bookId) ?: throw NotFoundException()

    @GET
    @RolesAllowed(Role.CATALOGER)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/catalog")
    fun getBookCatalog(@Context securityContext: SecurityContext): List<Book>  {
        val user = User.findByUsername(securityContext.userPrincipal.name) ?: throw NotFoundException()
        return Book.findByCatalogedByAndStatus(user, false).list()
    }

    @POST
    @RolesAllowed(Role.CATALOGER, Role.EDITOR, Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun createBook(book: Book): Book {
        book.id = null
        book.persistAndFlush()
        return book
    }

    @PUT
    @RolesAllowed(Role.EDITOR, Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    fun review(@PathParam("id") bookId: Long, @Context securityContext: SecurityContext): Book {
        val book: Book? = Book.findById(bookId)
        val user: User? = User.findByUsername(securityContext.userPrincipal.name)
        if (book != null && user != null) {
            book.reviewed = true
            book.reviewedBy = user
            book.persistAndFlush()
        } else {
            throw NotFoundException()
        }
        return book
    }

    @PUT
    @RolesAllowed(Role.CATALOGER, Role.EDITOR, Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    fun cataloged(@PathParam("id") bookId: Long, @Context securityContext: SecurityContext): Book {
        val book: Book? = Book.findById(bookId)
        val user: User? = User.findByUsername(securityContext.userPrincipal.name)
        if (book == null || user == null) throw NotFoundException()

        if (user.role == Role.EDITOR || book.catalogedBy.username == user.username) {
            book.cataloged = true
            book.persistAndFlush()
            return book
        } else {
            throw NotAllowedException("${user.username} not allowed to modify this book")
        }
    }

    @Transactional
    @PUT
    @RolesAllowed(Role.EDITOR, Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun assignBooks(@FormParam("id") bookIds: Array<Long>, username: String) {
        val user: User = User.findByUsername(username) ?: throw NotFoundException("$username not found")
        bookIds.map { id -> Book.findById(id) ?: throw NotFoundException("book $id not found") }
            .map { book ->
                book.cataloged = false
                book.catalogedBy = user
                book.persist()
            }
    }
}

