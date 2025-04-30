import java.util.List;

public class VersionSpecificException extends Exception {
    public VersionSpecificException(String requestedVersion, List<String> availableVersions) {
        super("Operation not supported for version " + requestedVersion + ". Available versions: " + availableVersions);
    }
}
