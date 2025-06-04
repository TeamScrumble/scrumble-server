package com.project.scrumbleserver.domain.tag

enum class BasicTag(
    val title: String,
    val color: String,
) {
    FEATURE(
        title = "Feature",
        color = "#0E8A16"
    ),
    BUG(
        title = "Bug",
        color = "#D73A4A"
    ),
    IMPROVEMENT(
        title = "Improvement",
        color = "#1D76DB"
    ),
    REFACTOR(
        title = "Refactor",
        color = "#6F42C1"
    ),
    DOCS(
        title = "Docs",
        color = "#CFD3D7"
    ),
    DESIGN(
        title = "Design",
        color = "#FF69B4"
    ),
    TEST(
        title = "Test",
        color = "#A2EEEF"
    ),
    CHORE(
        title = "Chore",
        color = "#8B949E"
    ),
}