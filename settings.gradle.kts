rootProject.name = "SearchNewsBlog"
include(":app")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}
include(":data")
include(":domain")
