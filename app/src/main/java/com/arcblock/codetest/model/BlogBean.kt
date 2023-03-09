package com.arcblock.codetest.model

/**
 * blog信息实体类
 */
data class BlogBeanItem(
    val frontmatter: Frontmatter
)

data class Frontmatter(
    val banner: Banner,
    val categories: List<String>,
    val date: String,
    val draft: Any,
    val language: String,
    val path: String,
    val tags: List<String>,
    val title: String
)

data class Banner(
    val childImageSharp: ChildImageSharp
)

data class ChildImageSharp(
    val fixed: Fixed
)

data class Fixed(
    val src: String
)