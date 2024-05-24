package org.ranapat.roots.tools

import okhttp3.MediaType

val MediaType.APPLICATION_JSON: String
    get() = "application/json"

val MediaType.value: String
    get() = "$type/$subtype"

val MediaType.isApplicationJson: Boolean
    get() = value == APPLICATION_JSON

