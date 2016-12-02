package jetbrains.buildServer.semanticVersioningBuildNumber.server;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Util {

    public static final String FEATURE_TYPE = "SemanticVersioningUpdater";
    public static final String PARAMETER_RELEASE_BRANCH = "versionUpdater.releaseBranch";
    public static final String DEFAULT_RELEASE_BRANCH = "master";

    public static String getReleaseBranch(@NotNull Map<String, String> params)
    {
        if (params == null) {
            throw new IllegalArgumentException("Argument 0 for @NotNull parameter of jetbrains/buildServer/semanticVersioningBuildNumber/Util.getReleaseBranch must not be null");
        }
        return params.get(PARAMETER_RELEASE_BRANCH);
    }

}
