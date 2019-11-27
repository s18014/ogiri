package com.example.ogiri

data class Tag(val id: Long,
               val name: String)

data class TagMapping(val mappingID: Long,
                      val themeID: Long,
                      val tagID: Long)
