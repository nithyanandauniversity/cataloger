package org.knhu.library.catalog.cataloger

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.knhu.library.catalog.security.User
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book: PanacheEntity() {
    lateinit var title: String
    lateinit var language: String
    lateinit var link: String

    @ManyToOne
    lateinit var catalogedBy: User
    var cataloged: Boolean = false

    @ManyToOne
    lateinit var reviewedBy: User
    var reviewed: Boolean = false

    companion object: PanacheCompanion<Book> {
        fun findByCatalogedBy(user: User) = find("catalogedBy", user).list()
        fun findByCatalogedByAndStatus(user: User, isCataloged: Boolean) =
            find("select b from Book where isCataloged = ? and catalogedBy = ?",
                isCataloged, user.id ?: -1)
    }
}