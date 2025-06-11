import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class VersionSpecificException extends Exception {
    public VersionSpecificException(Collection<String> requestedVersion, String[] availableVersions) {
        super("Operation not supported for version " + requestedVersion + ". Available versions: " + Arrays.toString(availableVersions));
    }
}
