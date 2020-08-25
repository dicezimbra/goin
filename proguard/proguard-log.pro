# Remove native logs


# Remove timber logs
-assumenosideeffects class timber.log.Timber* {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static int w(...);
    public static int e(...);
}