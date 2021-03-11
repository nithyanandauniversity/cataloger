package org.knhu.library.catalog.security

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import javax.persistence.*

@Entity
class User: PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null;
    lateinit var firstName: String
    lateinit var lastName: String

    @Column(unique = true)
    lateinit var email: String
    lateinit var password: String
    lateinit var username: String
    lateinit var role: String
    var enabled: Boolean = true

    companion object: PanacheCompanion<User> {
        fun findByEmail(email: String) = find("email", email).firstResult()
        fun findByUsername(username: String) = find("username", username).firstResult()
    }
}