package br.com.goin.feature.splash.model

object UpdateQueries {

    var HAS_UPDATES = "query GetVersionStatus(\$os: VersionType, \$version: String) {" +
            "  getVersionStatus(os: \$os, version: \$version) {\n" +
            "    status\n" +
            "  }" +
            "}"
}


