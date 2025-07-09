package com.project.scrumbleserver.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_rowid")
    var rowid: Long = 0L,

    @Column(nullable = false, length = 100)
    var email: String,

    @Column(nullable = false, length = 50)
    var nickname: String = "",

    @Column(nullable = false, length = 30)
    var job: String = "",

    @Column(name = "profile_image_url", nullable = false, length = 1000)
    var profileImageUrl: String = "",
) {
    val isInfoEmpty
        get() = nickname.isEmpty() || job.isEmpty()
}