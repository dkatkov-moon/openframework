package eu.ibagroup.easyrpa.openframework.google.services.constants;

/**
 * The list of configuration parameter names which can be specified within RPA platform to provide necessary
 * for Google services authorization information.
 */
public class GServicesConfigParam {

    public static final String AUTH_SECRET = "google.services.auth.secret";

    public static final String AUTH_TOKEN_STORES_DIR = "google.services.auth.token.stores.dir";

    public static final String AUTH_VERIFICATION_CODE_RECEIVER = "google.services.auth.verification.code.receiver";

    private GServicesConfigParam() {
    }
}